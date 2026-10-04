package com.autoapporganizer.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.components.AdaptiveContainer
import com.autoapporganizer.ui.components.GhostButton
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.ProgressRing
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.Spacing

/**
 * 整理进度页 —— 轨道动画 + 进度环 + 当前步骤 + 取消按钮。
 *
 * 取消走 service.cancelOrganize()（协程取消 → 回调「整理已取消」），
 * 不再出现"返回首页但任务仍在跑"的黑洞路径。
 */
@Composable
fun OrganizingScreen(
    windowSize: WindowSize,
    progress: Int,
    message: String,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProgress by animateFloatAsState(
        targetValue = progress / 100f,
        animationSpec = spring(stiffness = 120f),
        label = "organizeProgress"
    )

    AdaptiveContainer(windowSize = windowSize, modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            GlassCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Spacing.xxxl, vertical = Spacing.xxl),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(Spacing.xl)
                ) {
                    OrbitProgress(progress = animatedProgress)
                    Text(
                        text = "${progress.coerceIn(0, 100)}%",
                        style = MaterialTheme.typography.displaySmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = message.ifBlank { "正在整理桌面…" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "整理过程中请停留在桌面，不要切换应用",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                    )
                    GhostButton(
                        text = "取消整理",
                        onClick = onCancel,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** 轨道可视化 —— 进度环 + 环绕运转的光点，表达"正在处理"的动态。 */
@Composable
private fun OrbitProgress(progress: Float) {
    val transition = rememberInfiniteTransition(label = "orbit")
    val angle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(tween(2400, easing = LinearEasing), RepeatMode.Restart),
        label = "orbitAngle"
    )

    Box(contentAlignment = Alignment.Center) {
        ProgressRing(
            progress = progress,
            modifier = Modifier.size(180.dp),
            stroke = 12f
        )
        Canvas(modifier = Modifier.size(220.dp)) {
            val radius = size.minDimension / 2f
            val rad = Math.toRadians(angle.toDouble())
            val cx = center.x + radius * kotlin.math.cos(rad).toFloat()
            val cy = center.y + radius * kotlin.math.sin(rad).toFloat()
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(ElectricPurple, Color.Transparent),
                    center = Offset(cx, cy),
                    radius = 28f
                ),
                radius = 28f,
                center = Offset(cx, cy)
            )
            val rad2 = Math.toRadians((angle + 180).toDouble())
            val cx2 = center.x + radius * kotlin.math.cos(rad2).toFloat()
            val cy2 = center.y + radius * kotlin.math.sin(rad2).toFloat()
            drawCircle(
                color = AuroraCyan.copy(alpha = 0.8f),
                radius = 10f,
                center = Offset(cx2, cy2)
            )
        }
    }
}
