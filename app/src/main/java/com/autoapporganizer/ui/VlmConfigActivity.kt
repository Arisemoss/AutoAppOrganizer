package com.autoapporganizer.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.autoapporganizer.R
import com.autoapporganizer.databinding.ActivityVlmConfigBinding
import com.autoapporganizer.util.PrefsManager

/**
 * VLM 配置页 —— 选择提供商、填写 API Key / 端点 / 模型名。
 *
 * - provider = none 时，视觉整理退化为纯无障碍模式
 * - provider = openai 时使用 OpenAI 兼容接口（也适用于第三方代理）
 * - provider = gemini / glm 分别对应 Google Gemini 与智谱 GLM-4V
 */
class VlmConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVlmConfigBinding
    private lateinit var prefs: PrefsManager

    /** 提供商选项（顺序与 [PrefsManager] 中的 provider 值对应） */
    private val providerValues = listOf("none", "openai", "gemini", "glm")
    private val providerLabels by lazy {
        listOf(
            getString(R.string.vlm_provider_none),
            getString(R.string.vlm_provider_openai),
            getString(R.string.vlm_provider_gemini),
            getString(R.string.vlm_provider_glm)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVlmConfigBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = PrefsManager(this)

        binding.btnBack.setOnClickListener { finish() }

        setupProviderSpinner()
        loadConfig()

        binding.btnSave.setOnClickListener { saveConfig() }
    }

    private fun setupProviderSpinner() {
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            providerLabels
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerProvider.adapter = adapter
    }

    private fun loadConfig() {
        val providerIdx = providerValues.indexOf(prefs.vlmProvider).coerceAtLeast(0)
        binding.spinnerProvider.setSelection(providerIdx)
        binding.etApiKey.setText(prefs.vlmApiKey)
        binding.etEndpoint.setText(prefs.vlmEndpoint)
        binding.etModel.setText(prefs.vlmModel)
    }

    private fun saveConfig() {
        val providerIdx = binding.spinnerProvider.selectedItemPosition
        prefs.vlmProvider = providerValues.getOrElse(providerIdx) { "none" }
        prefs.vlmApiKey = binding.etApiKey.text.toString().trim()
        prefs.vlmEndpoint = binding.etEndpoint.text.toString().trim()
        prefs.vlmModel = binding.etModel.text.toString().trim()

        Toast.makeText(this, R.string.vlm_saved, Toast.LENGTH_SHORT).show()
        finish()
    }
}
