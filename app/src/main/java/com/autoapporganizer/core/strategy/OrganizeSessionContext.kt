package com.autoapporganizer.core.strategy

import com.autoapporganizer.util.PrefsManager

/**
 * Context passed to every [OrganizeStrategy] run.
 *
 * It carries user preferences and progress callbacks while abstracting away
 * the Android service/activity lifecycle.
 */
data class OrganizeSessionContext(
    val prefs: PrefsManager,
    val onProgress: suspend (percent: Int, message: String) -> Unit = { _, _ -> },
    val onLog: suspend (tag: String, message: String) -> Unit = { _, _ -> }
)
