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
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

/**
 * 主界面 — 桌面整理 + 诊断日志 + 权限引导
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var backupManager: BackupManager

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
                    if (folderCount > 0) {
                        Snackbar.make(binding.root,
                            getString(R.string.organize_complete, folderCount),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.btn_undo) { undoOrganize() }.show()
                    } else {
                        Snackbar.make(binding.root, "桌面已经很整洁了 ✨", Snackbar.LENGTH_SHORT).show()
                    }
                    if (backupManager.hasBackup()) {
                        binding.btnUndo.visibility = View.VISIBLE
                    }
                } else {
                    // 失败时自动展开诊断日志
                    binding.layoutDiagnostics.visibility = View.VISIBLE
                    binding.btnToggleLog.text = getString(R.string.log_hide)
                    Snackbar.make(binding.root,
                        message + " — 查看下方诊断日志",
                        Snackbar.LENGTH_LONG
                    ).show()
                }
                refreshLogView()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        backupManager = BackupManager(this)

        setupViews()
        checkServiceStatus()
        observeDiagnostics()

        // 首次使用检测权限
        checkAllPermissions()
    }

    override fun onResume() {
        super.onResume()
        checkServiceStatus()
        if (backupManager.hasBackup()) {
            binding.btnUndo.visibility = View.VISIBLE
        }
        refreshLogView()
    }

    private fun setupViews() {
        binding.cardOrganize.setOnClickListener {
            when {
                !isAccessibilityServiceEnabled() -> showPermissionDialog()
                !hasOverlayPermission() -> showOverlayPermissionDialog()
                else -> startOrganize()
            }
        }

        binding.btnUndo.setOnClickListener { undoOrganize() }
        binding.btnRunDiagnostic.setOnClickListener { runDiagnostic() }
        binding.btnToggleLog.setOnClickListener { toggleLogPanel() }
        binding.btnCopyLog.setOnClickListener { copyLogToClipboard() }
        binding.btnClearLog.setOnClickListener {
            DiagnosticLogger.clear()
            refreshLogView()
            Toast.makeText(this, "日志已清空", Toast.LENGTH_SHORT).show()
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

    private fun refreshLogView() {
        val entries = DiagnosticLogger.entries.value
        if (entries.isEmpty()) {
            binding.tvLogContent.text = "暂无日志。\n点击「🔍 诊断桌面」扫描，或点击「一键自动分类」触发整理。"
            return
        }
        binding.tvLogContent.text = entries.joinToString("\n") { it.formatted }
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
        Toast.makeText(this, "日志已复制到剪贴板", Toast.LENGTH_SHORT).show()
    }

    private fun runDiagnostic() {
        val service = AutoAppOrganizerService.instance
        if (service == null) {
            Toast.makeText(this, "请先启用无障碍服务", Toast.LENGTH_SHORT).show()
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
                .setPositiveButton("去设置") { _, _ -> openAccessibilitySettings() }
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
            .setTitle("需要悬浮窗权限")
            .setMessage("Android 15 + 小米系统需要悬浮窗权限才能正常使用桌面整理功能。")
            .setPositiveButton("去开启") { _, _ ->
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

    private fun showProgress() {
        binding.layoutProgress.visibility = View.VISIBLE
        binding.cardOrganize.isEnabled = false
        binding.cardOrganize.alpha = 0.5f
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