package com.autoapporganizer.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/**
 * 应用偏好封装 —— 集中管理所有可配置项，避免散落的 getString/getBoolean。
 *
 * ## 存储分层
 *
 * 偏好按敏感度分两层存储：
 *
 * 1. 普通字段（[prefs]）：明文 [SharedPreferences]，文件名 [PREFS_NAME]。
 *    存放 UI/行为开关（最小文件夹大小、不常用阈值、是否自动返回桌面、VLM 提供商/
 *    端点/模型名等）。这些字段不敏感，明文便于调试和备份。
 *
 * 2. 凭据字段（[securePrefs]）：[EncryptedSharedPreferences]，文件名 [SECURE_PREFS_NAME]。
 *    存放 [vlmApiKey] —— OpenAI/Gemini 等云端 API Key。明文存储会被 root 设备或启用了
 *    备份恢复的设备直接读取，加密后即使文件泄露也无法还原 Key。
 *
 * ## 升级迁移
 *
 * 旧版本（< 1.12）所有字段都写在 [PREFS_NAME] 明文文件里。新版本启动时调用
 * [migrateLegacyApiKeyOnce]：若发现旧明文 prefs 中存在非空 [KEY_VLM_API_KEY]，则把它
 * 复制到加密 prefs 并从旧文件删除，避免明文 Key 长期残留。迁移用 [KEY_MIGRATED]
 * 标记位防止重复执行。
 */
class PrefsManager(context: Context) {

    private val appContext = context.applicationContext

    private val prefs: SharedPreferences =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    /**
     * Lazily-initialised encrypted [SharedPreferences].
     *
     * Initialisation can throw on rare device-specific keystore failures (e.g. corrupted
     * master key after factory reset). When that happens we fall back to a plain
     * SharedPreferences instance so the app does not crash; [vlmApiKey] will then be stored
     * in plaintext, which is strictly worse than encrypted but better than the app being
     * unusable. The error is logged so it can be diagnosed.
     *
     * Migration of any legacy plaintext key is performed in the same lazy block to avoid
     * initialising EncryptedSharedPreferences twice.
     */
    private val securePrefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(appContext)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            val encrypted = EncryptedSharedPreferences.create(
                appContext,
                SECURE_PREFS_NAME,
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
            migrateLegacyApiKeyOnce(encrypted)
            encrypted
        } catch (e: Exception) {
            Log.e(TAG, "EncryptedSharedPreferences init failed, falling back to plaintext", e)
            appContext.getSharedPreferences(SECURE_PREFS_NAME_FALLBACK, Context.MODE_PRIVATE)
        }
    }

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
    /** VLM 提供商：none / openai / gemini / glm / local */
    var vlmProvider: String
        get() = prefs.getString(KEY_VLM_PROVIDER, DEFAULT_VLM_PROVIDER) ?: DEFAULT_VLM_PROVIDER
        set(value) = prefs.edit().putString(KEY_VLM_PROVIDER, value).apply()

    /**
     * VLM API Key —— 存储在 [securePrefs]（加密）。
     *
     * Getter 每次都从 [securePrefs] 读取，避免内存缓存被 dump 后泄露 Key。
     * setter 通过 [apply] 异步落盘，与原实现一致。
     */
    var vlmApiKey: String
        get() = securePrefs.getString(KEY_VLM_API_KEY, "") ?: ""
        set(value) = securePrefs.edit().putString(KEY_VLM_API_KEY, value).apply()

    /** VLM 端点（留空则用提供商默认） */
    var vlmEndpoint: String
        get() = prefs.getString(KEY_VLM_ENDPOINT, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VLM_ENDPOINT, value).apply()

    /** VLM 模型名 */
    var vlmModel: String
        get() = prefs.getString(KEY_VLM_MODEL, "") ?: ""
        set(value) = prefs.edit().putString(KEY_VLM_MODEL, value).apply()

    /** 整理策略：legacy / vision / hybrid */
    var organizeStrategy: String
        get() = prefs.getString(KEY_ORGANIZE_STRATEGY, "hybrid") ?: "hybrid"
        set(value) = prefs.edit().putString(KEY_ORGANIZE_STRATEGY, value).apply()

    /**
     * 一次性迁移：把旧版本明文存的 [vlmApiKey] 复制到 [target]（加密 prefs）并从明文
     * 文件删除。
     *
     * 幂等：用 [KEY_MIGRATED] 标记位确保只执行一次。即便用户从 1.11 升级到 1.12 又
     * 回滚到 1.11 再升级到 1.12，也只会迁移一次（标记位在普通 prefs 里，回滚不会
     * 删除它）。
     *
     * 边界情况：
     * - 旧 prefs 没有该 key（全新安装）→ 跳过，但仍写迁移标记
     * - 旧 prefs 有该 key 但值为空 → 同上
     * - 加密 prefs 初始化失败 → 调用方不会进入此方法（securePrefs 走 fallback 时
     *   不调本方法），下次启动 EncryptedSharedPreferences 可用时会重试。
     *
     * @param target 已成功初始化的加密 [SharedPreferences] 实例。
     */
    private fun migrateLegacyApiKeyOnce(target: SharedPreferences) {
        if (prefs.getBoolean(KEY_MIGRATED, false)) return

        val legacyKey = prefs.getString(KEY_VLM_API_KEY, null)
        if (!legacyKey.isNullOrEmpty()) {
            target.edit().putString(KEY_VLM_API_KEY, legacyKey).apply()
            Log.i(TAG, "Migrated legacy API key to EncryptedSharedPreferences")
        }
        // 无论是否迁移到 Key，都写标记位 + 清除明文 Key（避免明文 Key 残留）。
        // 即便 legacyKey 为空也写标记位，避免每次启动都查。
        prefs.edit()
            .remove(KEY_VLM_API_KEY)
            .putBoolean(KEY_MIGRATED, true)
            .apply()
    }

    companion object {
        private const val TAG = "PrefsManager"

        private const val PREFS_NAME = "auto_organizer_prefs"
        private const val SECURE_PREFS_NAME = "auto_organizer_prefs_secure"
        /** 当 EncryptedSharedPreferences 初始化失败时的兜底明文文件名（仅灾难恢复用）。 */
        private const val SECURE_PREFS_NAME_FALLBACK = "auto_organizer_prefs_secure_fallback"

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
        const val KEY_ORGANIZE_STRATEGY = "organize_strategy"
        const val DEFAULT_VLM_PROVIDER = "none"

        const val PROVIDER_NONE = "none"
        const val PROVIDER_OPENAI = "openai"
        const val PROVIDER_GEMINI = "gemini"
        const val PROVIDER_GLM = "glm"
        const val PROVIDER_LOCAL = "local"

        /** 标记旧版明文 API Key 是否已迁移到加密 prefs。 */
        private const val KEY_MIGRATED = "api_key_migrated_v1"
    }
}
