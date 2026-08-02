package com.autoapporganizer.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 「液态秩序」主题。
 *
 * - 深色为默认基调，浅色为镜像变体。
 * - Android 12+ 启用 [dynamicColor]，主色调跟随壁纸；同时主渐变会根据
 *   动态主色的色相做 ±30° 偏移（见 [LocalWallpaperHueShift]），保持融合感。
 * - 通过 [LocalIsDark] 暴露当前明暗模式，供渐变画笔与组件取色。
 */
@Composable
fun AutoAppOrganizerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val isDark = darkTheme

    // 动态取色：从系统动态色相提取一个偏移量（±30°），用于主渐变色相旋转。
    val wallpaperHueShift: Float = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val scheme = if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        deriveHueShift(scheme.primary, isDark)
    } else {
        0f
    }

    val baseScheme = if (isDark) DarkColorScheme else LightColorScheme
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    } else {
        baseScheme
    }.let {
        // 用品牌主色覆盖关键角色，保留「液态秩序」识别度
        it.copy(
            primary = if (isDark) AuroraCyan else SkyCyan,
            secondary = if (isDark) ElectricPurple else Lavender,
            tertiary = if (isDark) NeonPink else SoftPink
        )
    }

    CompositionLocalProvider(
        LocalIsDark provides isDark,
        LocalWallpaperHueShift provides wallpaperHueShift
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
            shapes = AppShapes,
            content = content
        )
    }
}

val DarkColorScheme = darkColorScheme(
    primary = AuroraCyan,
    onPrimary = TextPrimaryDark,
    primaryContainer = ElectricPurple,
    onPrimaryContainer = TextPrimaryDark,
    secondary = ElectricPurple,
    onSecondary = TextPrimaryDark,
    tertiary = NeonPink,
    onTertiary = TextPrimaryDark,
    background = DarkBgStart,
    onBackground = TextPrimaryDark,
    surface = DarkBgEnd,
    onSurface = TextPrimaryDark,
    surfaceVariant = Color(0xFF1C1C26),
    onSurfaceVariant = TextSecondaryDark,
    outline = BorderSubtleDark,
    outlineVariant = TextDisabledDark,
    error = ErrorRed,
    onError = TextPrimaryDark
)

val LightColorScheme = lightColorScheme(
    primary = SkyCyan,
    onPrimary = TextPrimaryLight,
    primaryContainer = Lavender,
    onPrimaryContainer = TextPrimaryLight,
    secondary = Lavender,
    onSecondary = TextPrimaryLight,
    tertiary = SoftPink,
    onTertiary = TextPrimaryLight,
    background = LightBgStart,
    onBackground = TextPrimaryLight,
    surface = LightBgEnd,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFE6E6EE),
    onSurfaceVariant = TextSecondaryLight,
    outline = BorderSubtleLight,
    outlineVariant = TextDisabledLight,
    error = ErrorRed,
    onError = TextPrimaryLight
)

/** 当前是否深色模式 —— 供渐变画笔取色。 */
val LocalIsDark = staticCompositionLocalOf { true }

/** 壁纸色相偏移（度）—— 动态取色时主渐变据此旋转色相。 */
val LocalWallpaperHueShift = compositionLocalOf { 0f }

/**
 * 从动态主色推导主渐变的色相偏移量。
 * 取动态主色与品牌基准色的色相差，裁剪到 ±30°。
 */
private fun deriveHueShift(dynamicPrimary: androidx.compose.ui.graphics.Color, isDark: Boolean): Float {
    val baseline = if (isDark) AuroraCyan else SkyCyan
    val dynHsv = FloatArray(3)
    val baseHsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (dynamicPrimary.red * 255).toInt(),
        (dynamicPrimary.green * 255).toInt(),
        (dynamicPrimary.blue * 255).toInt(),
        dynHsv
    )
    android.graphics.Color.RGBToHSV(
        (baseline.red * 255).toInt(),
        (baseline.green * 255).toInt(),
        (baseline.blue * 255).toInt(),
        baseHsv
    )
    var diff = dynHsv[0] - baseHsv[0]
    if (diff > 180f) diff -= 360f
    if (diff < -180f) diff += 360f
    return diff.coerceIn(-30f, 30f)
}
