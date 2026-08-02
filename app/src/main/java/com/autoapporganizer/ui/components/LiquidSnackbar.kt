package com.autoapporganizer.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.InfoBlue
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.SuccessGreen
import kotlinx.coroutines.delay

/** Snackbar 类型 —— 决定左侧竖条颜色。 */
enum class SnackType { SUCCESS, ERROR, INFO }

/**
 * 「液态秩序」Snackbar —— 底部悬浮、非全宽、圆角 16dp、毛玻璃背景、左侧状态渐变竖条。
 * 出现：从底部滑入 + fade in（200ms）；消失：向上滑出 + fade out。
 */
@Composable
fun LiquidSnackbarHost(
    message: String,
    type: SnackType,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    val isDark = LocalIsDark.current
    val visible = message.isNotBlank()

    // 自动消失
    LaunchedEffect(message) {
        if (message.isNotBlank()) {
            delay(2500)
            onDismiss()
        }
    }

    val barColors = when (type) {
        SnackType.SUCCESS -> listOf(SuccessGreen, Color(0xFF4ADE80))
        SnackType.ERROR -> listOf(ErrorRed, Color(0xFFF87171))
        SnackType.INFO -> listOf(InfoBlue, Color(0xFF60A5FA))
    }
    val textColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = visible,
            enter = slideInVertically(
                animationSpec = tween(200),
                initialOffsetY = { it / 2 }
            ) + fadeIn(animationSpec = tween(200)),
            exit = slideOutVertically(
                animationSpec = tween(200),
                targetOffsetY = { it / 2 }
            ) + fadeOut(animationSpec = tween(200))
        ) {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                cornerRadius = 16,
                blur = false
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 左侧 4dp 状态渐变竖条
                    Box(
                        modifier = Modifier
                            .width(4.dp)
                            .height(24.dp)
                            .clip(RoundedCornerShape(2.dp))
                            .background(Brush.verticalGradient(barColors))
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = textColor
                    )
                }
            }
        }
    }
}
