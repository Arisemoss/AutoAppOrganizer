package com.autoapporganizer.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.primaryLinearGradient

/** 底部导航项数据。 */
data class NavItem(
    val label: String,
    val icon: ImageVector,
    val target: Any
)

/**
 * 「液态秩序」底部导航 —— 悬浮胶囊式，非全宽、居中、圆角 28dp、margin 底部 24dp。
 * 选中态：图标上方 4dp 渐变圆点指示器（弹性 scale 0→1 动画），图标颜色渐变填充。
 * 未选中：灰色图标。
 */
@Composable
fun BottomNavPill(
    items: List<NavItem>,
    current: Any,
    onSelect: (NavItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDark.current
    val bgColor = if (isDark) Color(0xE612121A) else Color(0xE6FFFFFF)

    Row(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .padding(bottom = 24.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(bgColor)
            .padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEach { item ->
            NavPillItem(
                item = item,
                isSelected = item.target == current,
                isDark = isDark,
                onClick = { onSelect(item) }
            )
        }
    }
}

@Composable
private fun NavPillItem(
    item: NavItem,
    isSelected: Boolean,
    isDark: Boolean,
    onClick: () -> Unit
) {
    // 指示器弹性 scale
    val indicatorScale by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "navIndicator"
    )
    val iconColor = if (isSelected) {
        if (isDark) Color(0xFF06B6D4) else Color(0xFF0EA5E9)
    } else {
        if (isDark) TextSecondaryDark else TextSecondaryLight
    }
    val labelColor = if (isSelected) {
        if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    } else {
        if (isDark) TextSecondaryDark else TextSecondaryLight
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .bounceClick(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // 渐变圆点指示器（scale 0→1 弹性）
        if (indicatorScale > 0.01f) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(width = 16.dp, height = 4.dp)
                    .graphicsLayer { scaleX = indicatorScale; alpha = indicatorScale }
                    .clip(RoundedCornerShape(2.dp))
                    .background(primaryLinearGradient(isDark))
            )
        } else {
            Spacer(modifier = Modifier.height(4.dp))
        }
        Spacer(modifier = Modifier.height(4.dp))
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = iconColor,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = item.label,
            color = labelColor,
            style = MaterialTheme.typography.labelSmall
        )
    }
}
