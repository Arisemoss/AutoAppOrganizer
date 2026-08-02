package com.autoapporganizer.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.BorderSubtleDark
import com.autoapporganizer.ui.theme.BorderSubtleLight
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.flowingPrimaryGradient
import com.autoapporganizer.ui.theme.primaryLinearGradient

/**
 * 主按钮 —— 全宽 56dp，主渐变背景；启用时渐变缓慢流动（4s 循环），
 * 按下 scale 0.97 + 白色涟漪。
 */
@Composable
fun PrimaryGradientButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isDark = LocalIsDark.current
    val transition = rememberInfiniteTransition(label = "primaryFlow")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(animation = tween(4000, easing = LinearEasing)),
        label = "shift"
    )
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .height(56.dp)
            .clip(shape)
            .drawBehind {
                val brush = if (enabled) {
                    flowingPrimaryGradient(isDark, shift)
                } else {
                    primaryLinearGradient(isDark)
                }
                drawRect(brush = brush)
                if (!enabled) drawRect(color = Color(0x55000000))
            }
            .bounceClick(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = if (enabled) Color.White else Color(0x99FFFFFF),
            textAlign = TextAlign.Center
        )
    }
}

/**
 * 次级描边按钮 —— 毛玻璃背景 + 1px 渐变边框（顶部高亮），48dp 高。
 */
@Composable
fun SecondaryOutlineButton(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isDark = LocalIsDark.current
    val shape = RoundedCornerShape(12.dp)
    val borderColor = if (isDark) BorderSubtleDark else BorderSubtleLight
    val topLight = primaryLinearGradient(isDark)
    val textColor = if (isDark) TextSecondaryDark else TextSecondaryLight
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (isDark) Color(0x14FFFFFF) else Color(0x0A000000))
            .gradientBorder(
                brush = Brush.verticalGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.03f))
                ),
                width = 1.dp,
                shape = shape
            )
            .topLightLine(topLight)
            .bounceClick(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = if (enabled) textColor else textColor.copy(alpha = 0.4f)
        )
    }
}
