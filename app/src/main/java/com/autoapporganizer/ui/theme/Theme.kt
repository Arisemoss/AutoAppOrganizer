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
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * 「液态秩序」主题。
 *
 * - 深色为默认基调，浅色为镜像变体。
 * - Android 12+ 启用动态取色，主色调跟随壁纸；同时用品牌主色覆盖关键角色，
 *   保留「液态秩序」识别度。
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

    // 动态取色只调用一次，避免重复系统资源读取
    val baseScheme = if (isDark) DarkColorScheme else LightColorScheme
    val colorScheme = if (dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        (if (isDark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context))
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
        LocalIsDark provides isDark
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
