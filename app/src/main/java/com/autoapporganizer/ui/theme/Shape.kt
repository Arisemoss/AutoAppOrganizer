package com.autoapporganizer.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * 「液态秩序」形状系统 —— 大圆角营造流体感。
 * 圆角档位与设计稿对应：胶囊 12dp、卡片 20-28dp、按钮 16dp。
 */
val AppShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(24.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

// 语义化快捷形状
val ShapeChip = RoundedCornerShape(12.dp)
val ShapeButton = RoundedCornerShape(16.dp)
val ShapeCard = RoundedCornerShape(24.dp)
val ShapeCardLarge = RoundedCornerShape(28.dp)
val ShapeNavPill = RoundedCornerShape(28.dp)
val ShapeCategoryTile = RoundedCornerShape(20.dp)
