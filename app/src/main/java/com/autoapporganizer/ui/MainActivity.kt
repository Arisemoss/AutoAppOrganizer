package com.autoapporganizer.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.autoapporganizer.R
import com.autoapporganizer.databinding.ActivityMainBinding
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.HistoryManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 主界面 — 桌面整理 + 概览 + 操作网格 + 历史 + 诊断日志 + 权限引导
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var backupManager: BackupManager
    private lateinit var historyManager: HistoryManager

    private val organizeCallback = object : AutoAppOrganizerService.OrganizeCallback {
        override fun onProgress(progress: Int, message: String) {
            runOnUiThread {
                binding.progressBar.progress = progress
                binding.tvProgress.text = message
            }
        }

        override fun onComplete(success: Boolean, folderCount: Int, message: String) {
            runOnUiThread {
                hideProgress()

                if (success) {
                    // 直接使用服务返回的 message —— 它对「整理」和「撤销」都准确描述，
                    // 避免撤销时仍显示「已为你创建 N 个文件夹」这类误导文案。
                    val sb = Snackbar.make(binding.root, message, Snackbar.LENGTH_LONG)
                    // 仅在创建了文件夹（整理流程）时才提供「撤销」入口
                    if (folderCount > 0) {
                        sb.setAction(R.string.btn_undo) { undoOrganize() }
                    }
                    sb.show()
                } else {
                    // 失败时自动展开诊断日志
                    binding.layoutDiagnostics.visibility = View.VISIBLE
                    binding.btnToggleLog.text = getString(R.string.log_hide)
                    Snackbar.make(binding.root,
                        message + " — 查看下方诊断日志",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                refreshStats()
                refreshLogView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backupManager = BackupManager(this)
        historyManager = HistoryManager(this)

        setupViews()
        checkServiceStatus()
        observeDiagnostics()

        // 首次使用检测权限
        checkAllPermissions()
    }

    override fun onResume() {
        super.onResume()
        checkServiceStatus()
        refreshStats()
        refreshLogView()
    }

    private fun setupViews() {
        // 主操作
        binding.cardOrganize.setOnClickListener {
            when {
                !isAccessibilityServiceEnabled() -> showPermissionDialog()
                !hasOverlayPermission() -> showOverlayPermissionDialog()
                else -> startOrganize()
            }
        }

        // 操作网格
        // P1 视觉 Agent：视觉整理 + 模型配置
        binding.actionVisionOrganize.setOnClickListener { startVisionOrganize() }
        binding.actionModelConfig.setOnClickListener {
            startActivity(Intent(this, VlmConfigActivity::class.java))
        }
        binding.actionDiagnose.setOnClickListener { runDiagnostic() }
        binding.actionHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }
        binding.actionUndo.setOnClickListener { undoOrganize() }
        binding.actionSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        binding.btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // 最近历史卡片 → 跳转历史页
        binding.cardRecentHistory.setOnClickListener {
            startActivity(Intent(this, HistoryActivity::class.java))
        }

        // 日志面板
        binding.btnToggleLog.setOnClickListener { toggleLogPanel() }
        binding.btnCopyLog.setOnClickListener { copyLogToClipboard() }
        binding.btnClearLog.setOnClickListener {
            DiagnosticLogger.clear()
            refreshLogView()
            Toast.makeText(this, R.string.log_clear, Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeDiagnostics() {
        lifecycleScope.launch {
            DiagnosticLogger.entries.collectLatest {
                if (binding.layoutDiagnostics.visibility == View.VISIBLE) {
                    refreshLogView()
                }
            }
        }
    }

    // ──────────────────────────────────────────────
    // 统计与历史预览
    // ──────────────────────────────────────────────

    private fun refreshStats() {
        val latest = historyManager.latest()
        val sessions = historyManager.totalSessions()

        binding.tvStatSessions.text = sessions.toString()
        binding.tvStatLast.text = if (latest != null) {
            SimpleDateFormat("MM-dd HH:mm", Locale.getDefault()).format(Date(latest.timestamp))
        } else {
            getString(R.string.stat_never)
        }
        binding.tvStatApps.text = (latest?.appCount ?: 0).toString()
        binding.tvStatFolders.text = (latest?.folderCount ?: 0).toString()

        // 最近历史预览卡片
        binding.tvRecentHistory.text = if (latest != null) {
            val time = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                .format(Date(latest.timestamp))
            val cats = latest.sortedCategories.take(4).joinToString(" · ") { "${it.key} ${it.value}" }
            getString(R.string.history_created, latest.folderCount) + "\n$time" +
                    if (cats.isNotBlank()) "\n$cats" else ""
        } else {
            getString(R.string.history_empty)
        }

        // 状态胶囊
        updateStatusPill()
    }

    private fun updateStatusPill() {
        val ready = isAccessibilityServiceEnabled() && hasOverlayPermission()
        if (ready) {
            binding.chipStatus.background = getDrawable(R.drawable.bg_chip_active)
            // 用独立 drawable,避免对共享 drawable 设 colorFilter 污染其它视图
            binding.dotStatus.background = getDrawable(R.drawable.bg_dot_primary)
            binding.tvStatus.text = getString(R.string.status_ready)
            binding.tvStatus.setTextColor(getColor(R.color.primary))
        } else {
            binding.chipStatus.background = getDrawable(R.drawable.bg_chip_warning)
            binding.dotStatus.background = getDrawable(R.drawable.bg_dot_error)
            binding.tvStatus.text = getString(R.string.status_not_ready)
            binding.tvStatus.setTextColor(getColor(R.color.error))
        }
    }

    private fun refreshLogView() {
        val entries = DiagnosticLogger.entries.value
        binding.tvLogContent.text = if (entries.isEmpty()) {
            getString(R.string.log_empty)
        } else {
            entries.joinToString("\n") { it.formatted }
        }
    }

    private fun toggleLogPanel() {
        if (binding.layoutDiagnostics.visibility == View.VISIBLE) {
            binding.layoutDiagnostics.visibility = View.GONE
            binding.btnToggleLog.text = getString(R.string.log_show)
        } else {
            binding.layoutDiagnostics.visibility = View.VISIBLE
            binding.btnToggleLog.text = getString(R.string.log_hide)
            refreshLogView()
        }
    }

    private fun copyLogToClipboard() {
        val text = DiagnosticLogger.dumpAll()
        val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboard.setPrimaryClip(ClipData.newPlainText("诊断日志", text))
        Toast.makeText(this, R.string.log_copy, Toast.LENGTH_SHORT).show()
    }

    private fun runDiagnostic() {
        val service = AutoAppOrganizerService.instance
        if (service == null) {
            Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show()
            return
        }
        binding.layoutDiagnostics.visibility = View.VISIBLE
        binding.btnToggleLog.text = getString(R.string.log_hide)
        service.runDiagnostic()
        binding.root.postDelayed({ refreshLogView() }, 1500)
    }

    private fun checkServiceStatus() {
        if (isAccessibilityServiceEnabled()) {
            AutoAppOrganizerService.organizeCallback = organizeCallback
        }
    }

    private fun isAccessibilityServiceEnabled(): Boolean {
        return AutoAppOrganizerService.instance != null
    }

    // ──────────────────────────────────────────────
    // 三重权限检测
    // ──────────────────────────────────────────────

    /** 检查悬浮窗权限（Android 15 小米需要） */
    private fun hasOverlayPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Settings.canDrawOverlays(this)
        } else true
    }

    /** 检查使用统计权限 */
    @Suppress("unused")
    private fun hasUsageStatsPermission(): Boolean {
        return try {
            val appOps = getSystemService(Context.APP_OPS_SERVICE) as android.app.AppOpsManager
            val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                appOps.unsafeCheckOpNoThrow(
                    android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    packageName
                )
            } else {
                @Suppress("DEPRECATION")
                appOps.checkOpNoThrow(
                    android.app.AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(),
                    packageName
                )
            }
            mode == android.app.AppOpsManager.MODE_ALLOWED
        } catch (e: Exception) {
            false
        }
    }

    /** 首次使用全权限检测 + 小米专属提示 */
    private fun checkAllPermissions() {
        val issues = mutableListOf<String>()

        if (!isAccessibilityServiceEnabled()) {
            issues.add("「无障碍服务」未开启")
        }
        if (!hasOverlayPermission()) {
            issues.add("「悬浮窗权限」未开启（小米必需）")
        }
        if (!hasUsageStatsPermission()) {
            issues.add("「使用情况访问权限」未开启（智能分类需要）")
        }

        if (issues.isNotEmpty()) {
            val isXiaomi = Build.MANUFACTURER.lowercase().contains("xiaomi") ||
                    Build.BRAND.lowercase().contains("redmi")

            val extraTip = if (isXiaomi) {
                "\n\n📱 小米手机额外步骤：\n" +
                "1. 设置 → 应用设置 → 应用管理 → 桌面整理\n" +
                "2. 开启「自启动」\n" +
                "3. 开启「后台弹出界面」（显示悬浮窗）\n" +
                "4. 省电策略 → 选择「无限制」"
            } else ""

            AlertDialog.Builder(this)
                .setTitle("需要权限")
                .setMessage(issues.joinToString("\n") + extraTip)
                .setPositiveButton(R.string.go_to_settings) { _, _ -> openAccessibilitySettings() }
                .setNegativeButton("稍后", null)
                .show()
        }
    }

    private fun showPermissionDialog() {
        val isXiaomi = Build.MANUFACTURER.lowercase().contains("xiaomi") ||
                Build.BRAND.lowercase().contains("redmi")

        val msg = if (isXiaomi) {
            "请在设置中启用「桌面整理」的无障碍服务\n\n" +
            "📱 小米用户注意：\n" +
            "• 设置 → 更多设置 → 无障碍 → 已安装的服务 → 桌面整理\n" +
            "• 确保开关已打开\n" +
            "• 同时需要开启悬浮窗权限"
        } else {
            "请在设置中启用「桌面整理」的无障碍服务"
        }

        AlertDialog.Builder(this)
            .setTitle(R.string.permission_needed)
            .setMessage(msg)
            .setPositiveButton(R.string.go_to_settings) { _, _ -> openAccessibilitySettings() }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun showOverlayPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_overlay_title)
            .setMessage(R.string.permission_overlay_msg)
            .setPositiveButton(R.string.go_to_overlay_settings) { _, _ ->
                val intent = Intent(
                    Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:$packageName")
                )
                startActivity(intent)
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    // ──────────────────────────────────────────────
    // 操作
    // ──────────────────────────────────────────────

    private fun startOrganize() {
        val service = AutoAppOrganizerService.instance ?: return
        showProgress()
        service.startOrganize()
    }

    private fun undoOrganize() {
        val service = AutoAppOrganizerService.instance ?: return
        showProgress()
        binding.tvProgress.text = "正在还原…"
        service.undoOrganize()
    }

    private fun startVisionOrganize() {
        val service = AutoAppOrganizerService.instance ?: run {
            Toast.makeText(this, R.string.permission_needed, Toast.LENGTH_SHORT).show()
            return
        }
        showProgress()
        binding.tvProgress.text = "视觉整理中…"
        service.startVisionOrganize()
    }

    private fun showProgress() {
        binding.layoutProgress.visibility = View.VISIBLE
        binding.cardOrganize.isEnabled = false
        binding.cardOrganize.alpha = 0.6f
    }

    private fun hideProgress() {
        binding.layoutProgress.visibility = View.GONE
        binding.cardOrganize.isEnabled = true
        binding.cardOrganize.alpha = 1.0f
    }

    override fun onDestroy() {
        super.onDestroy()
        AutoAppOrganizerService.organizeCallback = null
    }
}
