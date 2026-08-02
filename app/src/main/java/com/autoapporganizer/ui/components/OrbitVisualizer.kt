package com.autoapporganizer.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.MonoDisplayLarge
import com.autoapporganizer.ui.theme.NeonPink
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.orbitCenterGlow
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

/**
 * 主控制台中央的圆形可视化组件（240dp）。
 *
 * - 中心径向辉光（主渐变 20% 透明度）
 * - 全圈淡渐变光环（装饰，呈现主渐变色）
 * - [GradientCircularProgress] 亮度进度弧
 * - 圆周 8-12 个分类色光点，交错呼吸（scale 0.8→1.2，3s 循环）
 * - 圆心：待整理应用数（等宽 48sp 渐变文字）+ 说明小字
 */
@Composable
fun OrbitVisualizer(
    count: Int,
    countLabel: String,
    progress: Float,
    modifier: Modifier = Modifier,
    diameter: Int = 240
) {
    val isDark = LocalIsDark.current
    val dotColors = listOf(
        Color(0xFF3B82F6), Color(0xFFF59E0B), Color(0xFFEF4444), Color(0xFF8B5CF6),
        Color(0xFF64748B), Color(0xFF10B981), Color(0xFF0EA5E9), Color(0xFFEC4899),
        Color(0xFFF97316), Color(0xFF14B8A6), Color(0xFF22C55E), Color(0xFFD946EF)
    )

    Box(modifier = modifier.size(diameter.dp), contentAlignment = Alignment.Center) {
        // 中心辉光
        Box(Modifier.fillMaxSize().background(orbitCenterGlow(isDark)))

        // 装饰光环 + 呼吸光点
        AuraAndBreathingDots(
            colors = dotColors,
            modifier = Modifier.fillMaxSize()
        )

        // 进度弧
        GradientCircularProgress(
            modifier = Modifier.fillMaxSize(),
            progress = progress,
            strokeWidth = 6.dp
        )

        // 圆心文字
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            GradientText(
                text = count.toString(),
                style = MonoDisplayLarge
            )
            Text(
                text = countLabel,
                style = MaterialTheme.typography.labelSmall,
                color = if (isDark) TextSecondaryDark else TextSecondaryLight
            )
        }
    }
}

/** 装饰全圈淡渐变光环 + 圆周交错呼吸光点。 */
@Composable
private fun AuraAndBreathingDots(
    colors: List<Color>,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDark.current
    val auraColors = if (isDark) {
        listOf(AuroraCyan, ElectricPurple, NeonPink, AuroraCyan)
    } else {
        listOf(
            com.autoapporganizer.ui.theme.SkyCyan,
            com.autoapporganizer.ui.theme.Lavender,
            com.autoapporganizer.ui.theme.SoftPink,
            com.autoapporganizer.ui.theme.SkyCyan
        )
    }
    val transition = rememberInfiniteTransition(label = "breath")
    val phase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2f * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(3000, easing = LinearEasing)),
        label = "phase"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = 6.dp.toPx()
        val d = min(w, h) - stroke
        val topLeft = Offset((w - d) / 2f, (h - d) / 2f)
        val ringRadius = d / 2f
        val cx = w / 2f
        val cy = h / 2f

        // 装饰全圈淡渐变光环
        drawArc(
            brush = androidx.compose.ui.graphics.Brush.sweepGradient(auraColors, center = center),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = androidx.compose.ui.geometry.Size(d, d),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = stroke, cap = androidx.compose.ui.graphics.StrokeCap.Round),
            alpha = 0.22f
        )

        // 呼吸光点
        val n = colors.size
        val baseDot = 7.dp.toPx()
        for (i in 0 until n) {
            val angle = (i.toFloat() / n) * 2f * Math.PI.toFloat()
            val px = cx + ringRadius * cos(angle)
            val py = cy + ringRadius * sin(angle)
            val scale = 0.8f + 0.4f * (0.5f + 0.5f * sin(phase + i * (2f * Math.PI.toFloat() / n)))
            val r = baseDot * scale
            drawCircle(
                color = colors[i],
                radius = r,
                center = Offset(px, py),
                alpha = 0.95f
            )
            // 软光晕
            drawCircle(
                color = colors[i],
                radius = r * 2.2f,
                center = Offset(px, py),
                alpha = 0.12f
            )
        }
    }
}
