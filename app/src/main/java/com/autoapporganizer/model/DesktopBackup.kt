package com.autoapporganizer.model

/**
 * 桌面备份数据模型
 */
data class DesktopBackup(
    val timestamp: Long = System.currentTimeMillis(),
    val screen: Int = 0,
    val items: List<DesktopItem> = emptyList()
)
