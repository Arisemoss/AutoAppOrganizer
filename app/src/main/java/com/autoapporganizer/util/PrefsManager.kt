package com.autoapporganizer.util

import android.content.Context
import android.content.SharedPreferences

/**
 * 应用偏好封装 —— 集中管理所有可配置项，避免散落的 getString/getBoolean。
 */
class PrefsManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /** 同类应用达到此数量才创建文件夹 */
    var minFolderSize: Int
        get() = prefs.getInt(KEY_MIN_FOLDER_SIZE, DEFAULT_MIN_FOLDER_SIZE)
        set(value) = prefs.edit().putInt(KEY_MIN_FOLDER_SIZE, value.coerceIn(2, 10)).apply()

    /** 「不常用」阈值（分钟）：7 天内前台时长低于此值的应用归入不常用 */
    var rarelyUsedMinutes: Int
        get() = prefs.getInt(KEY_RARELY_USED_MIN, DEFAULT_RARELY_USED_MIN)
        set(value) = prefs.edit().putInt(KEY_RARELY_USED_MIN, value.coerceIn(0, 1440)).apply()

    /** 整理前是否自动返回桌面 */
    var autoReturnHome: Boolean
        get() = prefs.getBoolean(KEY_AUTO_HOME, true)
        set(value) = prefs.edit().putBoolean(KEY_AUTO_HOME, value).apply()

    // P1 视觉 Agent：VLM 配置
    /** VLM 提供商：none / openai / gemini / glm */
    var vlmProvider: String
        get() = prefs.getString(KEY_VLM_PROVIDER, DEFAULT_VLM_PROVIDER) ?: DEFAULT_VLM_PROVIDER
        set(value) = prefs.edit().putString(KEY_VLM_PROVIDER, value).apply()

    /** VLM API Key */
    var vlmApiKey: String
        get() = prefs.getString(KEY_VLM_API_KEY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VLM_API_KEY, value).apply()

    /** VLM 端点（留空则用提供商默认） */
    var vlmEndpoint: String
        get() = prefs.getString(KEY_VLM_ENDPOINT, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VLM_ENDPOINT, value).apply()

    /** VLM 模型名 */
    var vlmModel: String
        get() = prefs.getString(KEY_VLM_MODEL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VLM_MODEL, value).apply()

    companion object {
        private const val PREFS_NAME = "auto_organizer_prefs"

        const val KEY_MIN_FOLDER_SIZE = "min_folder_size"
        const val KEY_RARELY_USED_MIN = "rarely_used_minutes"
        const val KEY_AUTO_HOME = "auto_return_home"

        const val DEFAULT_MIN_FOLDER_SIZE = 2
        const val DEFAULT_RARELY_USED_MIN = 1 // 分钟

        // VLM keys
        const val KEY_VLM_PROVIDER = "vlm_provider"
        const val KEY_VLM_API_KEY = "vlm_api_key"
        const val KEY_VLM_ENDPOINT = "vlm_endpoint"
        const val KEY_VLM_MODEL = "vlm_model"
        const val DEFAULT_VLM_PROVIDER = "none"
    }
}
