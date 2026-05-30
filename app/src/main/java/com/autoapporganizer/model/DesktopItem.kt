package com.autoapporganizer.model

import android.graphics.Rect

/**
 * 桌面项数据模型
 */
data class DesktopItem(
    val type: ItemType,
    val name: String? = null,
    val packageName: String? = null,
    val bounds: Rect? = null,
    val screen: Int = 0,
    val folderName: String? = null,
    val children: List<DesktopItem>? = null
) {
    enum class ItemType {
        APP,
        WIDGET,
        FOLDER,
        UNKNOWN
    }
}
