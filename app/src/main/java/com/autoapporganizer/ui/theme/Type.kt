package com.autoapporganizer.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

/**
 * 等宽字体 —— 用于统计数字 / 时间戳，营造精密工具感。
 *
 * JetBrains Mono 优先；设备未内置时回退到系统 monospace。
 * 这里用 FontFamily.Monospace 作为零资源依赖回退，避免引入额外字体文件；
 * 若后续内置 JetBrains Mono ttf，可改为 FontFamily(Font(R.font.jetbrains_mono))。
 */
val MonoFontFamily: FontFamily = FontFamily.Monospace

/** 正文字体族。 */
val SansFontFamily: FontFamily = FontFamily.Default

/** 「液态秩序」排版系统 —— 基于 Material3 Typography 扩展。 */
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 48.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        letterSpacing = (-0.02).sp,
        lineHeight = 40.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelLarge = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = SansFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 14.sp
    )
)

/** 等宽数字样式 —— 圆心大数字 / 进度百分比 / 时间戳。 */
val MonoDisplayLarge = TextStyle(
    fontFamily = MonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 48.sp,
    lineHeight = 56.sp
)

val MonoDisplayMedium = TextStyle(
    fontFamily = MonoFontFamily,
    fontWeight = FontWeight.Bold,
    fontSize = 32.sp,
    lineHeight = 40.sp
)

val MonoLabel = TextStyle(
    fontFamily = MonoFontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp
)
