package com.autoapporganizer.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.NeonPink
import com.autoapporganizer.ui.theme.SkyCyan

/**
 * 渐变圆环进度 —— 主渐变 sweep 描边，已完成部分带微弱发光。
 * 用于整理进度页与主控制台的圆环可视化。
 *
 * @param progress 0..1
 * @param strokeWidth 描边宽度
 * @param glow 是否绘制发光层（进度页）
 */
@Composable
fun GradientCircularProgress(
    modifier: Modifier = Modifier,
    progress: Float,
    strokeWidth: Dp = 6.dp,
    glow: Boolean = false,
    trackAlpha: Float = 0.12f
) {
    val isDark = LocalIsDark.current
    val colors = if (isDark) {
        listOf(AuroraCyan, ElectricPurple, NeonPink, AuroraCyan)
    } else {
        listOf(SkyCyan, com.autoapporganizer.ui.theme.Lavender, com.autoapporganizer.ui.theme.SoftPink, SkyCyan)
    }

    // 发光层轻微脉动
    val transition = rememberInfiniteTransition(label = "glowPulse")
    val pulse by transition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Canvas(modifier = modifier) {
        val stroke = strokeWidth.toPx()
        val diameter = minOf(size.width, size.height) - stroke
        val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
        val arcSize = Size(diameter, diameter)

        // 轨道
        drawArc(
            color = Color.White.copy(alpha = trackAlpha),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
        )

        val sweep = Brush.sweepGradient(colors, center = center)
        val clamped = progress.coerceIn(0f, 1f)

        // 发光：在主弧下方叠一层更宽、更透明的同色弧
        if (glow && clamped > 0f) {
            rotate(-90f) {
                drawArc(
                    brush = sweep,
                    startAngle = 0f,
                    sweepAngle = 360f * clamped,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        width = stroke * 2.4f,
                        cap = StrokeCap.Round
                    ),
                    alpha = 0.18f * pulse
                )
            }
        }

        // 主弧
        rotate(-90f) {
            drawArc(
                brush = sweep,
                startAngle = 0f,
                sweepAngle = 360f * clamped,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = StrokeCap.Round)
            )
        }
    }
}
