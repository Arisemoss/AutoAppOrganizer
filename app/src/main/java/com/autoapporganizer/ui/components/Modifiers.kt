package com.autoapporganizer.ui.components

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
import androidx.compose.ui.graphics.graphicsLayer
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
 * 毛玻璃视觉模拟 —— 不使用 RenderEffect（它会模糊卡片自身内容导致文字不可读），
 * 改为通过半透明渐变 + 边框 + 顶部光线在 [GlassCard] 内组合呈现毛玻璃质感。
 * 保留此修饰符作为 no-op 占位，避免破坏调用方签名。
 */
@Composable
fun Modifier.backdropBlur(radius: Dp = 32.dp): Modifier = this
