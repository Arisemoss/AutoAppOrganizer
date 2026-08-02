package com.autoapporganizer.ui

import com.autoapporganizer.ui.theme.AppCategory

/** 顶层导航目标。 */
enum class Screen { Home, Organizing, Result, Backup, Accessibility }

/** 一次整理的结果摘要，用于整理完成页与状态回传。 */
data class OrganizeResult(
    val success: Boolean,
    val folderCount: Int,
    val appCount: Int,
    val categories: Map<String, Int>,
    val message: String
) {
    /** 估算节省的屏幕数：每屏约 24 个图标位，整理后文件夹占位更少。 */
    val screensSaved: Int
        get() = ((appCount - folderCount).coerceAtLeast(0)) / 24

    /** 分类按数量降序，映射为 [AppCategory] 与计数。 */
    val categoryList: List<Pair<AppCategory, Int>>
        get() = categories.entries
            .sortedByDescending { it.value }
            .map { AppCategory.fromLabel(it.key) to it.value }
}

/** 备份/历史条目 —— 由 [com.autoapporganizer.util.HistoryManager] 会话映射而来。 */
data class BackupEntry(
    val timestamp: Long,
    val folderCount: Int,
    val appCount: Int,
    val fresh: Boolean
) {
    /** 备份体积估算（仅展示用）。 */
    val sizeLabel: String
        get() = "~" + String.format(java.util.Locale.US, "%.1f KB", (appCount * 0.2).coerceAtLeast(0.1))
}
