package com.autoapporganizer.model

/**
 * 桌面备份数据模型
 *
 * @param timestamp 备份时间戳（毫秒）
 * @param screen 屏幕索引（0 = 主屏，支持多屏桌面扩展）
 * @param items 备份的桌面项列表
 */
data class DesktopBackup(
    val timestamp: Long = System.currentTimeMillis(),
    val screen: Int = 0,
    val items: List<DesktopItem> = emptyList()
) {
    /** 应用图标数量 */
    val appCount: Int
        get() = items.count { it.type == DesktopItem.ItemType.APP }

    /** 小组件数量 */
    val widgetCount: Int
        get() = items.count { it.type == DesktopItem.ItemType.WIDGET }

    /** 文件夹数量 */
    val folderCount: Int
        get() = items.count { it.type == DesktopItem.ItemType.FOLDER }

    /** 备份是否为空 */
    val isEmpty: Boolean
        get() = items.isEmpty()

    /** 获取所有应用图标 */
    val apps: List<DesktopItem>
        get() = items.filter { it.type == DesktopItem.ItemType.APP }

    /** 获取所有小组件 */
    val widgets: List<DesktopItem>
        get() = items.filter { it.type == DesktopItem.ItemType.WIDGET }

    /** 获取所有文件夹 */
    val folders: List<DesktopItem>
        get() = items.filter { it.type == DesktopItem.ItemType.FOLDER }

    /**
     * 按分类统计应用数量
     */
    fun getCategoryStats(categoryMatcher: com.autoapporganizer.util.CategoryMatcher): Map<String, Int> {
        val stats = mutableMapOf<String, Int>()
        for (item in apps) {
            val key = item.packageName ?: item.name ?: continue
            val category = categoryMatcher.matchCategory(key)
            stats[category] = (stats[category] ?: 0) + 1
        }
        return stats
    }

    /**
     * 根据包名查找桌面项
     */
    fun findByPackageName(packageName: String): DesktopItem? {
        return items.firstOrNull { it.packageName == packageName }
    }

    /**
     * 根据名称查找桌面项
     */
    fun findByName(name: String): DesktopItem? {
        return items.firstOrNull { it.name == name }
    }

    /**
     * 获取备份摘要信息
     */
    fun getSummary(): String {
        return "备份于 ${java.text.SimpleDateFormat("yyyy-MM-dd HH:mm", java.util.Locale.getDefault()).format(java.util.Date(timestamp))}: " +
            "${appCount} 个应用, ${widgetCount} 个小组件, ${folderCount} 个文件夹"
    }
}
