package com.autoapporganizer.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
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
 * 主界面 — 桌面整理 + 诊断日志
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
                        Snackbar.make(
                            binding.root,
                            getString(R.string.organize_complete, folderCount),
                            Snackbar.LENGTH_LONG
                        ).setAction(R.string.btn_undo) {
                            undoOrganize()
                        }.show()
                    } else {
                        Snackbar.make(
                            binding.root,
                            "桌面已经很整洁了 ✨",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }

                    if (backupManager.hasBackup()) {
                        binding.btnUndo.visibility = View.VISIBLE
                    }
                } else {
                    // 失败时自动展开诊断日志
                    binding.layoutDiagnostics.visibility = View.VISIBLE
                    binding.btnToggleLog.text = getString(R.string.log_hide)
                    Snackbar.make(
                        binding.root,
                        message + " — 查看下方诊断日志",
                        Snackbar.LENGTH_LONG
                    ).show()
                }

                // 每次完成后刷新日志
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
            if (!isAccessibilityServiceEnabled()) {
                showPermissionDialog()
            } else {
                startOrganize()
            }
        }

        binding.btnUndo.setOnClickListener {
            undoOrganize()
        }

        // 诊断按钮
        binding.btnRunDiagnostic.setOnClickListener {
            runDiagnostic()
        }

        // 展开/收起日志面板
        binding.btnToggleLog.setOnClickListener {
            toggleLogPanel()
        }

        // 复制日志
        binding.btnCopyLog.setOnClickListener {
            copyLogToClipboard()
        }

        // 清空日志
        binding.btnClearLog.setOnClickListener {
            DiagnosticLogger.clear()
            refreshLogView()
            Toast.makeText(this, "日志已清空", Toast.LENGTH_SHORT).show()
        }
    }

    /** 订阅诊断日志流，自动刷新 */
    private fun observeDiagnostics() {
        lifecycleScope.launch {
            DiagnosticLogger.entries.collectLatest {
                // 仅在日志面板可见时刷新，减少开销
                if (binding.layoutDiagnostics.visibility == View.VISIBLE) {
                    refreshLogView()
                }
            }
        }
    }

    private fun refreshLogView() {
        val entries = DiagnosticLogger.entries.value
        if (entries.isEmpty()) {
            binding.tvLogContent.text = "暂无日志。\n点击「🔍 诊断」按钮扫描桌面，或点击「一键自动分类」触发整理。"
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
        // 1.5秒后刷新日志
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

    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_needed)
            .setMessage("请在设置中启用「桌面整理」的无障碍服务")
            .setPositiveButton(R.string.go_to_settings) { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton("取消", null)
            .show()
    }

    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }

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