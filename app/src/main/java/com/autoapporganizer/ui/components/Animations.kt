package com.autoapporganizer.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * 打字机文字 —— 逐字出现，每字 [charDelayMs] 毫秒。
 * 用于整理进度页的状态描述。
 */
@Composable
fun TypewriterText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    color: Color = Color.Unspecified,
    charDelayMs: Long = 55
) {
    var shown by remember(text) { mutableIntStateOf(0) }
    LaunchedEffect(text) {
        shown = 0
        text.forEachIndexed { index, _ ->
            delay(charDelayMs)
            shown = index + 1
        }
    }
    Text(
        text = text.take(shown),
        modifier = modifier,
        style = style,
        color = color,
        textAlign = TextAlign.Center
    )
}

/**
 * CountUp 数字 —— 从 0 弹性增长到 [target]，时长约 800ms。
 * 用于统计数字变化。
 */
@Composable
fun CountUpText(
    target: Int,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.headlineLarge,
    color: Color = Color.Unspecified,
    brush: Brush? = null
) {
    val anim = remember { Animatable(0f) }
    LaunchedEffect(target) {
        anim.snapTo(0f)
        anim.animateTo(
            targetValue = target.toFloat(),
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessLow
            )
        )
    }
    Text(
        text = anim.value.roundToInt().toString(),
        modifier = modifier,
        style = if (brush != null) style.copy(brush = brush) else style,
        color = if (brush != null) Color.Unspecified else color,
        textAlign = TextAlign.Center
    )
}
