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

    companion object {
        private const val PREFS_NAME = "auto_organizer_prefs"

        const val KEY_MIN_FOLDER_SIZE = "min_folder_size"
        const val KEY_RARELY_USED_MIN = "rarely_used_minutes"
        const val KEY_AUTO_HOME = "auto_return_home"

        const val DEFAULT_MIN_FOLDER_SIZE = 2
        const val DEFAULT_RARELY_USED_MIN = 1 // 分钟
    }
}
