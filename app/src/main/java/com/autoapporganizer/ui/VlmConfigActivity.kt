package com.autoapporganizer.ui

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
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
 * - provider = local 时连接本地 Ollama / llama.cpp 等 OpenAI 兼容服务
 */
class VlmConfigActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVlmConfigBinding
    private lateinit var prefs: PrefsManager

    /** 提供商选项（顺序与 [PrefsManager] 中的 provider 值对应） */
    private val providerValues = listOf(
        PrefsManager.PROVIDER_NONE,
        PrefsManager.PROVIDER_OPENAI,
        PrefsManager.PROVIDER_GEMINI,
        PrefsManager.PROVIDER_GLM,
        PrefsManager.PROVIDER_LOCAL
    )
    private val providerLabels by lazy {
        listOf(
            getString(R.string.vlm_provider_none),
            getString(R.string.vlm_provider_openai),
            getString(R.string.vlm_provider_gemini),
            getString(R.string.vlm_provider_glm),
            getString(R.string.vlm_provider_local)
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
        binding.spinnerProvider.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    updateInputHints(providerValues.getOrElse(position) { PrefsManager.PROVIDER_NONE })
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    updateInputHints(PrefsManager.PROVIDER_NONE)
                }
            }
    }

    private fun loadConfig() {
        val providerIdx = providerValues.indexOf(prefs.vlmProvider).coerceAtLeast(0)
        binding.spinnerProvider.setSelection(providerIdx)
        binding.etApiKey.setText(prefs.vlmApiKey)
        binding.etEndpoint.setText(prefs.vlmEndpoint)
        binding.etModel.setText(prefs.vlmModel)
        updateInputHints(prefs.vlmProvider)
    }

    private fun saveConfig() {
        val providerIdx = binding.spinnerProvider.selectedItemPosition
        prefs.vlmProvider = providerValues.getOrElse(providerIdx) { PrefsManager.PROVIDER_NONE }
        prefs.vlmApiKey = binding.etApiKey.text.toString().trim()
        prefs.vlmEndpoint = binding.etEndpoint.text.toString().trim()
        prefs.vlmModel = binding.etModel.text.toString().trim()

        Toast.makeText(this, R.string.vlm_saved, Toast.LENGTH_SHORT).show()
        finish()
    }

    /**
     * 根据所选提供商动态切换端点/模型输入框的 hint，降低用户配置门槛。
     */
    private fun updateInputHints(provider: String) {
        when (provider) {
            PrefsManager.PROVIDER_LOCAL -> {
                binding.etEndpoint.hint = getString(R.string.vlm_hint_endpoint_local)
                binding.etModel.hint = "llava"
            }
            PrefsManager.PROVIDER_GEMINI -> {
                binding.etEndpoint.hint = getString(R.string.vlm_hint_endpoint)
                binding.etModel.hint = "gemini-2.0-flash"
            }
            PrefsManager.PROVIDER_GLM -> {
                binding.etEndpoint.hint = getString(R.string.vlm_hint_endpoint)
                binding.etModel.hint = "glm-4v"
            }
            PrefsManager.PROVIDER_OPENAI -> {
                binding.etEndpoint.hint = getString(R.string.vlm_hint_endpoint)
                binding.etModel.hint = "gpt-4o"
            }
            else -> {
                binding.etEndpoint.hint = getString(R.string.vlm_hint_endpoint)
                binding.etModel.hint = getString(R.string.vlm_hint_model)
            }
        }
    }
}
