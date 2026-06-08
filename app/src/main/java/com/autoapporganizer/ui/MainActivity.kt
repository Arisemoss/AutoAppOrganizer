package com.autoapporganizer.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.autoapporganizer.R
import com.autoapporganizer.databinding.ActivityMainBinding
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.util.BackupManager
import com.google.android.material.snackbar.Snackbar

/**
 * 主界面 — 桌面整理
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
                    Snackbar.make(
                        binding.root,
                        message,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
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
    }

    override fun onResume() {
        super.onResume()
        checkServiceStatus()

        if (backupManager.hasBackup()) {
            binding.btnUndo.visibility = View.VISIBLE
        }
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