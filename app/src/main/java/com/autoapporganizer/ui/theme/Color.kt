package com.autoapporganizer.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.geometry.Offset

// ════════════════════════════════════════════════════════
// 「液态秩序 Liquid Order」色彩系统
// ════════════════════════════════════════════════════════

// ── 深色模式背景层 ──
val DarkBgStart = Color(0xFF0A0A0F)   // 深空炭黑
val DarkBgEnd = Color(0xFF12121A)     // 极暗蓝灰
val DarkSurface = Color(0xB812121A)   // rgba(18,18,26,0.72)

// ── 浅色模式背景层 ──
val LightBgStart = Color(0xFFFAFAFA)  // 霜白
val LightBgEnd = Color(0xFFF0F0F5)    // 珍珠灰
val LightSurface = Color(0xD9FFFFFF)  // rgba(255,255,255,0.85)

// ── 主渐变：极光 ──
val AuroraCyan = Color(0xFF06B6D4)
val ElectricPurple = Color(0xFF8B5CF6)
val NeonPink = Color(0xFFEC4899)

// ── 浅色主渐变 ──
val SkyCyan = Color(0xFF0EA5E9)
val Lavender = Color(0xFFA78BFA)
val SoftPink = Color(0xFFF472B6)

// ── 辅助渐变：琥珀 ──
val AmberGold = Color(0xFFF59E0B)
val CoralOrange = Color(0xFFFB923C)

// ── 文字 ──
val TextPrimaryDark = Color(0xFFF1F5F9)
val TextSecondaryDark = Color(0xFF94A3B8)
val TextDisabledDark = Color(0xFF475569)

val TextPrimaryLight = Color(0xFF0F172A)
val TextSecondaryLight = Color(0xFF64748B)
val TextDisabledLight = Color(0xFF94A3B8)

// ── 边框 ──
val BorderSubtleDark = Color(0x0FFFFFFF)   // rgba(255,255,255,0.06)
val BorderSubtleLight = Color(0x0A000000)  // rgba(0,0,0,0.04)

// ── 状态色 ──
val SuccessGreen = Color(0xFF22C55E)
val ErrorRed = Color(0xFFEF4444)
val InfoBlue = Color(0xFF3B82F6)
val WarningAmber = Color(0xFFF59E0B)

// ════════════════════════════════════════════════════════
// 渐变画笔构造器
// ════════════════════════════════════════════════════════

/** 主渐变（左→右）。深色用极光三色，浅色用柔色三色。 */
fun primaryLinearGradient(isDark: Boolean): Brush.LinearGradient =
    Brush.linearGradient(
        colors = if (isDark) {
            listOf(AuroraCyan, ElectricPurple, NeonPink)
        } else {
            listOf(SkyCyan, Lavender, SoftPink)
        }
    )

/**
 * 主按钮「活着」的流动渐变。
 * [shift] ∈ [0,1]，由无限动画驱动，让渐变沿水平方向缓慢位移。
 */
fun flowingPrimaryGradient(isDark: Boolean, shift: Float): Brush.LinearGradient {
    val colors = if (isDark) {
        listOf(AuroraCyan, ElectricPurple, NeonPink, ElectricPurple, AuroraCyan)
    } else {
        listOf(SkyCyan, Lavender, SoftPink, Lavender, SkyCyan)
    }
    val w = 1.2f
    val s = (shift % 1f)
    return Brush.linearGradient(
        colors = colors,
        start = Offset(s * w, 0f),
        end = Offset(s * w + 1f, 0f),
        tileMode = androidx.compose.ui.graphics.TileMode.Mirror
    )
}

/** 琥珀渐变（警告 / 备份相关）。 */
fun amberLinearGradient(): Brush.LinearGradient =
    Brush.linearGradient(colors = listOf(AmberGold, CoralOrange))

/** 径向背景渐变：中心偏上 30% 最亮，向外淡化。 */
fun backgroundRadialGradient(isDark: Boolean): Brush.RadialGradient {
    val center = if (isDark) DarkBgEnd else LightBgEnd
    val edge = if (isDark) DarkBgStart else LightBgStart
    return Brush.radialGradient(
        colors = listOf(center, edge),
        center = Offset(0.5f, 0.3f),
        radius = 1200f
    )
}

/** 圆环可视化中心的径向辉光（主渐变 20% 透明度）。 */
fun orbitCenterGlow(isDark: Boolean): Brush.RadialGradient {
    val base = if (isDark) AuroraCyan else SkyCyan
    return Brush.radialGradient(
        colors = listOf(base.copy(alpha = 0.20f), Color.Transparent)
    )
}

/** 动态取色：根据壁纸色相偏移 ±30°，对主渐变三色做色相旋转。 */
fun dynamicShiftedGradient(isDark: Boolean, wallpaperHueShift: Float): Brush.LinearGradient {
    val (a, b, c) = if (isDark) {
        Triple(AuroraCyan, ElectricPurple, NeonPink)
    } else {
        Triple(SkyCyan, Lavender, SoftPink)
    }
    return Brush.linearGradient(
        colors = listOf(
            a.shiftHue(wallpaperHueShift),
            b.shiftHue(wallpaperHueShift),
            c.shiftHue(wallpaperHueShift)
        )
    )
}

/** 对 Color 做色相旋转（度数），保持明度饱和度。 */
private fun Color.shiftHue(degrees: Float): Color {
    if (degrees == 0f) return this
    val hsv = FloatArray(3)
    android.graphics.Color.RGBToHSV(
        (red * 255).toInt(), (green * 255).toInt(), (blue * 255).toInt(), hsv
    )
    hsv[0] = (hsv[0] + degrees + 360f) % 360f
    val rgb = android.graphics.Color.HSVToColor(hsv)
    return Color(
        red = ((rgb shr 16) and 0xFF) / 255f,
        green = ((rgb shr 8) and 0xFF) / 255f,
        blue = (rgb and 0xFF) / 255f,
        alpha = alpha
    )
}
