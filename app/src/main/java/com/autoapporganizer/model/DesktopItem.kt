package com.autoapporganizer.model

import android.graphics.Rect

/**
 * 桌面项数据模型
 *
 * @param type 桌面项类型（APP/WIDGET/FOLDER/UNKNOWN）
 * @param name 显示名称
 * @param packageName 包名（应用图标才有）
 * @param bounds 屏幕坐标矩形
 * @param screen 屏幕索引
 * @param folderName 所属文件夹名称（如果有）
 * @param children 子项列表（仅文件夹类型有）
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

    /** 是否为应用图标 */
    val isApp: Boolean
        get() = type == ItemType.APP

    /** 是否为小组件 */
    val isWidget: Boolean
        get() = type == ItemType.WIDGET

    /** 是否为文件夹 */
    val isFolder: Boolean
        get() = type == ItemType.FOLDER

    /** 是否有包名 */
    val hasPackageName: Boolean
        get() = !packageName.isNullOrEmpty()

    /** 是否有显示名称 */
    val hasName: Boolean
        get() = !name.isNullOrEmpty()

    /** 获取显示标签（优先名称，其次包名） */
    val displayLabel: String
        get() = name ?: packageName ?: "未知"

    /** 中心 X 坐标 */
    val centerX: Int
        get() = bounds?.centerX() ?: 0

    /** 中心 Y 坐标 */
    val centerY: Int
        get() = bounds?.centerY() ?: 0

    /** 宽度 */
    val width: Int
        get() = bounds?.width() ?: 0

    /** 高度 */
    val height: Int
        get() = bounds?.height() ?: 0

    /** 子项数量（仅文件夹有效） */
    val childCount: Int
        get() = children?.size ?: 0

    /**
     * 判断坐标是否在边界内
     */
    fun contains(x: Int, y: Int): Boolean {
        return bounds?.contains(x, y) ?: false
    }

    /**
     * 计算到另一个桌面项的距离
     */
    fun distanceTo(other: DesktopItem): Double {
        val dx = centerX - other.centerX
        val dy = centerY - other.centerY
        return Math.sqrt((dx * dx + dy * dy).toDouble())
    }

    companion object {
        /**
         * 创建应用图标
         */
        fun app(name: String, packageName: String, bounds: Rect, screen: Int = 0): DesktopItem {
            return DesktopItem(
                type = ItemType.APP,
                name = name,
                packageName = packageName,
                bounds = bounds,
                screen = screen
            )
        }

        /**
         * 创建小组件
         */
        fun widget(bounds: Rect, screen: Int = 0): DesktopItem {
            return DesktopItem(
                type = ItemType.WIDGET,
                bounds = bounds,
                screen = screen
            )
        }

        /**
         * 创建文件夹
         */
        fun folder(name: String, bounds: Rect, children: List<DesktopItem> = emptyList(), screen: Int = 0): DesktopItem {
            return DesktopItem(
                type = ItemType.FOLDER,
                name = name,
                bounds = bounds,
                screen = screen,
                children = children
            )
        }
    }
}
