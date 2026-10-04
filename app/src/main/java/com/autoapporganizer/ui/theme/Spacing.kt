package com.autoapporganizer.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 4dp 基准间距刻度 —— 全应用统一的节奏体系。
 *
 * 使用约定：
 * - 卡片内边距 [xl]（20dp），列表项间 [m]（12dp），分区之间 [xxl]（24dp）
 * - 屏幕水平留白 [ScreenHorizontalPadding]（20dp），平板/桌面保持同一节奏
 * - 图标与文字间 [s]（8dp），徽标内边距 [xs]（4dp）
 */
object Spacing {
    val xs: Dp = 4.dp
    val s: Dp = 8.dp
    val m: Dp = 12.dp
    val l: Dp = 16.dp
    val xl: Dp = 20.dp
    val xxl: Dp = 24.dp
    val xxxl: Dp = 32.dp
}

/** 屏幕水平留白：手机 20dp；Medium/Expanded 由内容最大宽度约束接管。 */
val ScreenHorizontalPadding: Dp = 20.dp

/** Medium 断点下内容列的最大宽度（居中）。 */
val MediumContentMaxWidth: Dp = 640.dp

/** Expanded 断点下双栏布局的最大总宽。 */
val ExpandedContentMaxWidth: Dp = 1100.dp
