package com.autoapporganizer.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.autoapporganizer.R
import com.autoapporganizer.databinding.ActivitySettingsBinding
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.HistoryManager
import com.autoapporganizer.util.PrefsManager

/**
 * 设置页 —— 分类阈值、不常用阈值、自动回桌面、数据清理。
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var prefs: PrefsManager
    private lateinit var historyManager: HistoryManager
    private lateinit var backupManager: BackupManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PrefsManager(this)
        historyManager = HistoryManager(this)
        backupManager = BackupManager(this)

        binding.btnBack.setOnClickListener { finish() }

        setupSteppers()
        setupSwitch()
        setupStrategySelector()
        setupDataActions()
    }

    private fun setupSteppers() {
        // 文件夹最少图标数
        renderFolderSize()
        binding.btnDecrFolderSize.setOnClickListener {
            prefs.minFolderSize = prefs.minFolderSize - 1
            renderFolderSize()
        }
        binding.btnIncrFolderSize.setOnClickListener {
            prefs.minFolderSize = prefs.minFolderSize + 1
            renderFolderSize()
        }

        // 不常用阈值（分钟）
        renderRarelyUsed()
        binding.btnDecrRarely.setOnClickListener {
            // 步长 1，下限 0（0=禁用）
            prefs.rarelyUsedMinutes = (prefs.rarelyUsedMinutes - 1).coerceAtLeast(0)
            renderRarelyUsed()
        }
        binding.btnIncrRarely.setOnClickListener {
            prefs.rarelyUsedMinutes = prefs.rarelyUsedMinutes + 1
            renderRarelyUsed()
        }
    }

    private fun renderFolderSize() {
        binding.tvFolderSize.text = prefs.minFolderSize.toString()
    }

    private fun renderRarelyUsed() {
        val v = prefs.rarelyUsedMinutes
        binding.tvRarelyUsed.text = if (v == 0) "关" else v.toString()
    }

    private fun setupSwitch() {
        binding.switchAutoHome.isChecked = prefs.autoReturnHome
        binding.switchAutoHome.setOnCheckedChangeListener { _, checked ->
            prefs.autoReturnHome = checked
        }
    }

    private fun setupStrategySelector() {
        val strategyKeys = listOf("legacy", "vision", "hybrid")
        val strategyLabels = listOf(
            getString(R.string.settings_strategy_legacy),
            getString(R.string.settings_strategy_vision),
            getString(R.string.settings_strategy_hybrid)
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, strategyLabels)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerStrategy.adapter = adapter

        // Set current selection
        val currentIdx = strategyKeys.indexOf(prefs.organizeStrategy).coerceAtLeast(2) // default to hybrid
        binding.spinnerStrategy.setSelection(currentIdx)

        binding.spinnerStrategy.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                prefs.organizeStrategy = strategyKeys[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        // VLM config button
        binding.btnVlmConfig.setOnClickListener {
            startActivity(Intent(this, VlmConfigActivity::class.java))
        }
    }

    private fun setupDataActions() {
        binding.btnClearHistory.setOnClickListener {
            if (historyManager.loadAll().isEmpty()) {
                Toast.makeText(this, R.string.settings_nothing_to_clear, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_pref_clear_history)
                .setMessage(R.string.settings_pref_clear_history_desc)
                .setPositiveButton(R.string.settings_pref_clear_history) { _, _ ->
                    historyManager.clear()
                    Toast.makeText(this, R.string.settings_cleared, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }

        binding.btnClearBackup.setOnClickListener {
            if (!backupManager.hasBackup()) {
                Toast.makeText(this, R.string.settings_nothing_to_clear, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            AlertDialog.Builder(this)
                .setTitle(R.string.settings_pref_clear_backup)
                .setMessage(R.string.settings_pref_clear_backup_desc)
                .setPositiveButton(R.string.settings_pref_clear_backup) { _, _ ->
                    backupManager.deleteBackup()
                    Toast.makeText(this, R.string.settings_cleared, Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(android.R.string.cancel, null)
                .show()
        }
    }
}
