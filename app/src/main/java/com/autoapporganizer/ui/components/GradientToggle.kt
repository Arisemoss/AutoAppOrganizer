package com.autoapporganizer.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.primaryLinearGradient

/**
 * 自定义渐变开关 —— 关闭时轨道 #334155，开启时主渐变；
 * 滑块白色带 2dp 阴影，移动用 spring 物理动画，轨道颜色 200ms 渐变过渡。
 */
@Composable
fun GradientToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    width: Int = 52,
    height: Int = 30
) {
    val isDark = LocalIsDark.current
    val trackOff = if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1)
    val gradAlpha by animateFloatAsState(
        targetValue = if (checked) 1f else 0f,
        animationSpec = tween(200),
        label = "toggleGradAlpha"
    )
    val thumbPadding = 3.dp
    val thumbSize = (height - 6).dp
    val thumbX by animateDpAsState(
        targetValue = if (checked) (width - height).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "toggleThumb"
    )

    Box(
        modifier = modifier
            .size(width = width.dp, height = height.dp)
            .clip(RoundedCornerShape(50))
            .background(trackOff)
            .bounceClick { onCheckedChange(!checked) }
    ) {
        // 渐变轨道叠加层
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(primaryLinearGradient(isDark))
                .graphicsLayer { alpha = gradAlpha }
        )
        // 滑块
        Box(
            modifier = Modifier
                .offset(x = thumbX + thumbPadding, y = thumbPadding)
                .size(thumbSize)
                .clip(CircleShape)
                .background(Color.White)
                .shadow(2.dp, CircleShape)
        )
    }
}
