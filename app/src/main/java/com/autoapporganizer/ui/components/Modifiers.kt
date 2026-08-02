package com.autoapporganizer.ui.components

import android.graphics.RenderEffect as AndroidRenderEffect
import android.graphics.Shader
import android.os.Build
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asComposeRenderEffect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 统一的「按下回弹 + 涟漪」点击修饰。
 * 按下时 scale 0.97（100ms），释放弹性回弹（150ms），涟漪为 Material3 默认。
 */
@Composable
fun Modifier.bounceClick(
    enabled: Boolean = true,
    scalePressed: Float = 0.97f,
    onClick: () -> Unit
): Modifier {
    val interaction = remember { MutableInteractionSource() }
    val pressed by interaction.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) scalePressed else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "bounceScale"
    )
    return this
        .graphicsLayer { scaleX = scale; scaleY = scale }
        .clickable(
            interactionSource = interaction,
            indication = ripple(),
            enabled = enabled,
            onClick = onClick
        )
}

/** 1px 渐变边框。 */
fun Modifier.gradientBorder(
    brush: Brush,
    width: Dp = 1.dp,
    shape: Shape
): Modifier = this.border(width = width, brush = brush, shape = shape)

/** 顶部 1px 渐变光线，模拟自上而下的环境光照。 */
fun Modifier.topLightLine(brush: Brush): Modifier = this.drawWithContent {
    drawContent()
    drawLine(
        brush = brush,
        start = Offset(0f, 1f),
        end = Offset(size.width, 1f),
        strokeWidth = 2f
    )
}

/**
 * 毛玻璃模糊（Android 12+ 真实 RenderEffect；低版本回退为无操作，
 * 由 [GlassCard] 的半透明渐变 + 边框兜底视觉）。
 */
@Composable
fun Modifier.backdropBlur(radius: Dp = 32.dp): Modifier {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) return this
    val px = with(LocalDensity.current) { radius.toPx() }
    return this.graphicsLayer {
        renderEffect = AndroidRenderEffect
            .createBlurEffect(px, px, Shader.TileMode.CLAMP)
            .asComposeRenderEffect()
    }
}
