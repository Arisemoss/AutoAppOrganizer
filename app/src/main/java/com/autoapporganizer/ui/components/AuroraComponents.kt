package com.autoapporganizer.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.BorderSubtleDark
import com.autoapporganizer.ui.theme.BorderSubtleLight
import com.autoapporganizer.ui.theme.DarkSurface
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.ExpandedContentMaxWidth
import com.autoapporganizer.ui.theme.LightSurface
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.MediumContentMaxWidth
import com.autoapporganizer.ui.theme.MonoFontFamily
import com.autoapporganizer.ui.theme.ShapeButton
import com.autoapporganizer.ui.theme.ShapeCard
import com.autoapporganizer.ui.theme.ShapeChip
import com.autoapporganizer.ui.theme.Spacing
import com.autoapporganizer.ui.theme.SuccessGreen
import com.autoapporganizer.ui.theme.primaryLinearGradient

/**
 * 断点窗口尺寸：手机（Compact < 600dp）、平板竖屏/小窗（Medium 600–840dp）、
 * 平板横屏/桌面窗口（Expanded > 840dp）。
 */
enum class WindowSize { Compact, Medium, Expanded }

/** 按窗口宽度推断断点，驱动各屏的自适应布局。 */
@Composable
fun rememberWindowSize(): WindowSize {
    val widthDp = LocalConfiguration.current.screenWidthDp
    return when {
        widthDp < 600 -> WindowSize.Compact
        widthDp < 840 -> WindowSize.Medium
        else -> WindowSize.Expanded
    }
}

/**
 * 玻璃卡片 —— Aurora Glass 的基础容器。
 *
 * 深色下半透明面板 + 1dp 细描边；亮色下为高不透明白卡。
 * 所有屏的内容区块统一由它承载，保证视觉一致与层次清晰。
 */
@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isDark = LocalIsDark.current
    Surface(
        modifier = modifier
            .clip(ShapeCard)
            .border(
                width = 1.dp,
                color = if (isDark) BorderSubtleDark else BorderSubtleLight,
                shape = ShapeCard
            ),
        shape = ShapeCard,
        color = if (isDark) DarkSurface else LightSurface,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        content()
    }
}

/**
 * 渐变主按钮 —— 唯一的主行动入口样式（紫→青极光渐变）。
 * 一屏至多一个，避免 CTA 稀释。
 */
@Composable
fun AuroraButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Box(
        modifier = modifier
            .clip(ShapeButton)
            .background(primaryLinearGradient(isDark = LocalIsDark.current))
            .clickable(enabled = enabled, onClick = onClick)
            .heightIn(min = 52.dp)
            .padding(horizontal = Spacing.xxl, vertical = Spacing.l),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )
    }
}

/** 次级按钮 —— 描边幽灵样式，用于主 CTA 之外的辅助动作。 */
@Composable
fun GhostButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.heightIn(min = 52.dp),
        shape = ShapeButton
    ) {
        Text(text)
    }
}

/** 统计胶囊 —— 等宽字体数字 + 小标签，用于统计行（数字对齐）。 */
@Composable
fun StatChip(
    value: String,
    label: String,
    modifier: Modifier = Modifier
) {
    GlassCard(modifier = modifier) {
        Column(
            modifier = Modifier.padding(horizontal = Spacing.l, vertical = Spacing.m),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontFamily = MonoFontFamily,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    }
}

/** 分区标题 —— 小型弱化标签，建立内容层次。 */
@Composable
fun SectionTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        modifier = modifier.padding(bottom = Spacing.m)
    )
}

/** 进度环 —— 整理进度可视化（底环 + 极光渐变进度弧）。 */
@Composable
fun ProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    stroke: Float = 10f
) {
    Canvas(modifier = modifier) {
        val inset = stroke
        val arcSize = size.width - inset * 2
        drawArc(
            color = Color.White.copy(alpha = 0.08f),
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(arcSize, arcSize),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        drawArc(
            brush = Brush.linearGradient(listOf(ElectricPurple, AuroraCyan)),
            startAngle = -90f,
            sweepAngle = 360f * progress.coerceIn(0f, 1f),
            useCenter = false,
            topLeft = Offset(inset, inset),
            size = Size(arcSize, arcSize),
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}

/** 状态徽章 —— success=true 绿 / false 红 / null 中性灰。 */
@Composable
fun StatusBadge(success: Boolean?, text: String, modifier: Modifier = Modifier) {
    val color = when (success) {
        true -> SuccessGreen
        false -> ErrorRed
        null -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    }
    Row(
        modifier = modifier
            .clip(ShapeChip)
            .background(color.copy(alpha = 0.14f))
            .padding(horizontal = Spacing.m, vertical = Spacing.xs),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.xs)
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

/**
 * 自适应内容容器 —— Compact 全宽、Medium 居中限宽 640dp、Expanded 限宽 1100dp。
 * 所有屏的内容统一经它包裹，保证平板/桌面窗口下的阅读节奏一致。
 */
@Composable
fun AdaptiveContainer(
    windowSize: WindowSize,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val maxWidth = when (windowSize) {
        WindowSize.Compact -> null
        WindowSize.Medium -> MediumContentMaxWidth
        WindowSize.Expanded -> ExpandedContentMaxWidth
    }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        val inner = if (maxWidth != null) {
            Modifier.fillMaxWidth().widthIn(max = maxWidth)
        } else {
            Modifier.fillMaxWidth()
        }
        Box(modifier = inner) {
            content()
        }
    }
}
