package com.autoapporganizer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.BorderSubtleDark
import com.autoapporganizer.ui.theme.BorderSubtleLight
import com.autoapporganizer.ui.theme.DarkSurface
import com.autoapporganizer.ui.theme.LightSurface
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.primaryLinearGradient

/**
 * 全屏径向渐变背景 —— 中心偏上 30% 最亮，向外淡化。
 * 可叠加 [blobs] 漂移渐变光斑（用于沉浸式页面）。
 */
@Composable
fun AppBackground(
    modifier: Modifier = Modifier,
    blobs: List<@Composable () -> Unit> = emptyList()
) {
    val isDark = LocalIsDark.current
    val centerColor = if (isDark) DarkBgEnd else LightBgEnd
    val edgeColor = if (isDark) DarkBgStart else LightBgStart
    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                drawRect(
                    brush = Brush.radialGradient(
                        colors = listOf(centerColor, edgeColor),
                        center = Offset(size.width * 0.5f, size.height * 0.3f),
                        radius = size.maxDimension
                    )
                )
            }
    ) {
        blobs.forEach { it() }
    }
}

/** 漂移渐变光斑 —— 大半径软边色块，配合动画位移营造流动感。 */
@Composable
fun DriftingBlob(
    brush: Brush,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(50))
            .background(brush)
    )
}

/**
 * 毛玻璃卡片：半透明渐变表面 + 极细边框 + 顶部光线 +（S+）内容模糊。
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    cornerRadius: Int = 24,
    blur: Boolean = true,
    content: @Composable () -> Unit
) {
    val isDark = LocalIsDark.current
    val shape = RoundedCornerShape(cornerRadius.dp)
    val surfaceColor = if (isDark) DarkSurface else LightSurface
    val borderColor = if (isDark) BorderSubtleDark else BorderSubtleLight
    val topLight = primaryLinearGradient(isDark)
    Box(
        modifier = modifier
            .clip(shape)
            .then(if (blur) Modifier.backdropBlur(32.dp) else Modifier)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        surfaceColor.copy(alpha = 0.72f),
                        surfaceColor.copy(alpha = if (isDark) 0.65f else 0.55f)
                    )
                )
            )
            .gradientBorder(
                brush = Brush.verticalGradient(
                    colors = listOf(borderColor, borderColor.copy(alpha = 0.02f))
                ),
                width = 1.dp,
                shape = shape
            )
            .topLightLine(topLight)
    ) {
        content()
    }
}

/** 渐变文字 —— background-clip: text 效果，用主渐变（或自定义）裁切文字。 */
@Composable
fun GradientText(
    text: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    brush: Brush? = null,
    textAlign: TextAlign? = null
) {
    val isDark = LocalIsDark.current
    val resolved = brush ?: primaryLinearGradient(isDark)
    Text(
        text = text,
        style = style.copy(brush = resolved),
        modifier = modifier,
        textAlign = textAlign
    )
}

/** 胶囊状态标签 —— 半透明背景，11sp 文字，可带左侧色点。 */
@Composable
fun StatusChip(
    text: String,
    modifier: Modifier = Modifier,
    leadingColor: Color? = null
) {
    val isDark = LocalIsDark.current
    val bg = if (isDark) Color(0x0FFFFFFF) else Color(0x0A000000)
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (leadingColor != null) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .clip(RoundedCornerShape(50))
                    .background(leadingColor)
            )
            Spacer(modifier = Modifier.width(6.dp))
        }
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDark) Color(0xFFCBD5E1) else Color(0xFF475569)
        )
    }
}

/**
 * 应用图标缩略图 —— 无真实图标时用分类渐变 + 首字母占位，
 * 用于结果页分类列表中重叠排列的图标缩略图。
 */
@Composable
fun AppIconThumb(
    name: String,
    categoryStart: Color,
    categoryEnd: Color,
    modifier: Modifier = Modifier,
    size: Int = 32
) {
    val initial = name.firstOrNull { it.isLetterOrDigit() }?.toString() ?: "·"
    Box(
        modifier = modifier
            .size(size.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Brush.linearGradient(listOf(categoryStart, categoryEnd))),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = initial,
            style = MaterialTheme.typography.labelMedium,
            color = Color.White
        )
    }
}
