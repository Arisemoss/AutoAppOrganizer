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
 * 主界面
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
                            "桌面已经很整洁了",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                    
                    // 显示撤销按钮
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
        
        // 检查是否有备份
        if (backupManager.hasBackup()) {
            binding.btnUndo.visibility = View.VISIBLE
        }
    }
    
    private fun setupViews() {
        // 整理按钮点击
        binding.cardOrganize.setOnClickListener {
            if (!isAccessibilityServiceEnabled()) {
                showPermissionDialog()
            } else {
                startOrganize()
            }
        }
        
        // 撤销按钮
        binding.btnUndo.setOnClickListener {
            undoOrganize()
        }
    }
    
    /**
     * 检查服务状态
     */
    private fun checkServiceStatus() {
        if (!isAccessibilityServiceEnabled()) {
            // 服务未启用
        } else {
            // 设置回调
            AutoAppOrganizerService.organizeCallback = organizeCallback
        }
    }
    
    /**
     * 检查无障碍服务是否启用
     */
    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = AutoAppOrganizerService.instance
        return service != null
    }
    
    /**
     * 显示权限对话框
     */
    private fun showPermissionDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.permission_needed)
            .setMessage("请在设置中启用\"桌面整理\"的无障碍服务")
            .setPositiveButton(R.string.go_to_settings) { _, _ ->
                openAccessibilitySettings()
            }
            .setNegativeButton("取消", null)
            .show()
    }
    
    /**
     * 打开无障碍设置
     */
    private fun openAccessibilitySettings() {
        val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
        startActivity(intent)
    }
    
    /**
     * 开始整理
     */
    private fun startOrganize() {
        val service = AutoAppOrganizerService.instance ?: return
        
        showProgress()
        service.startOrganize()
    }
    
    /**
     * 撤销整理
     */
    private fun undoOrganize() {
        val service = AutoAppOrganizerService.instance ?: return
        
        showProgress()
        binding.tvProgress.text = "正在还原..."
        service.undoOrganize()
    }
    
    /**
     * 显示进度
     */
    private fun showProgress() {
        binding.progressBar.visibility = View.VISIBLE
        binding.tvProgress.visibility = View.VISIBLE
        binding.cardOrganize.isEnabled = false
        binding.cardOrganize.alpha = 0.5f
    }
    
    /**
     * 隐藏进度
     */
    private fun hideProgress() {
        binding.progressBar.visibility = View.GONE
        binding.tvProgress.visibility = View.GONE
        binding.cardOrganize.isEnabled = true
        binding.cardOrganize.alpha = 1.0f
    }
    
    override fun onDestroy() {
        super.onDestroy()
        AutoAppOrganizerService.organizeCallback = null
    }
}
