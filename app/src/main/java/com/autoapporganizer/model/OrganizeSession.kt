package com.autoapporganizer.model

/**
 * 一次桌面整理会话的记录。
 *
 * @param timestamp 整理完成时间（ms）
 * @param folderCount 创建的文件夹数
 * @param appCount 识别并整理的图标总数
 * @param categories 分类明细，例如 {"社交": 5, "购物": 3}
 * @param launcher 整理时的桌面包名（便于排查兼容性）
 */
data class OrganizeSession(
    val timestamp: Long = System.currentTimeMillis(),
    val folderCount: Int = 0,
    val appCount: Int = 0,
    val categories: Map<String, Int> = emptyMap(),
    val launcher: String? = null
) {
    /** 分类名按数量降序排列，用于历史展示 */
    val sortedCategories: List<Map.Entry<String, Int>>
        get() = categories.entries.sortedByDescending { it.value }
}
