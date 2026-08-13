package com.autoapporganizer.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.PrimaryGradientButton
import com.autoapporganizer.ui.components.SecondaryOutlineButton
import com.autoapporganizer.ui.components.bounceClick
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.NeonPink
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.primaryLinearGradient

private data class GuideStep(
    val title: String,
    val desc: String,
    val accent: Color,
    val illustration: StepIllustration
)

/** 插图类型 —— 决定 Canvas 手绘内容。 */
private enum class StepIllustration { ACCESSIBILITY, LAYERS, CHECK }

private val guideSteps = listOf(
    GuideStep("开启无障碍服务", "允许「桌面整理」读取桌面节点并执行拖拽手势，这是自动分类的前提。", AuroraCyan, StepIllustration.ACCESSIBILITY),
    GuideStep("开启悬浮窗权限", "Android 15 与部分机型需要悬浮窗权限才能正常派发拖拽手势。", ElectricPurple, StepIllustration.LAYERS),
    GuideStep("准备就绪", "权限配置完成，回到主控制台即可一键整理桌面。", NeonPink, StepIllustration.CHECK)
)

/**
 * 页面 5：无障碍引导 / Accessibility Guide
 *
 * 步骤引导式全页设计：3 步进度点 + 渐变线条插图（蓝→紫→粉）+ 底部固定操作。
 * 步骤切换使用 Shared Axis 水平滑入转场。
 */
@Composable
fun AccessibilityGuideScreen(
    onBack: () -> Unit,
    onOpenSettings: () -> Unit,
    onComplete: () -> Unit
) {
    val isDark = LocalIsDark.current
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight
    val titleColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    var step by remember { mutableIntStateOf(0) }
    val current = guideSteps[step]
    // 插图色彩随步骤渐变（蓝→紫→粉）
    val glowColor = current.accent

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            // 顶栏
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                    contentDescription = "返回",
                    tint = titleColor,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(50))
                        .bounceClick(onClick = onBack)
                        .padding(8.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "权限引导", style = MaterialTheme.typography.headlineMedium, color = titleColor)
            }

            // 进度点
            StepDots(total = guideSteps.size, current = step)

            Spacer(modifier = Modifier.weight(1f))

            // 插图 + 文案 —— Shared Axis 水平转场 + 色相渐变
            AnimatedContent(
                targetState = step,
                transitionSpec = {
                    // Shared Axis：新步从右滑入，旧步向左滑出
                    (slideInHorizontally(tween(350)) { w -> w / 3 } + fadeIn(tween(350))) togetherWith
                        (slideOutHorizontally(tween(250)) { w -> -w / 3 } + fadeOut(tween(250)))
                },
                label = "guideStep"
            ) { s ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // 渐变线条插图（Canvas 手绘）
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.radialGradient(
                                    listOf(glowColor.copy(alpha = 0.30f), Color.Transparent)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        GradientLineIllustration(
                            type = guideSteps[s].illustration,
                            color = glowColor,
                            modifier = Modifier.size(88.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = guideSteps[s].title,
                        style = MaterialTheme.typography.headlineMedium,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = guideSteps[s].desc,
                        style = MaterialTheme.typography.bodyMedium,
                        color = secondaryText,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // 底部固定操作
            PrimaryGradientButton(
                text = if (step == guideSteps.lastIndex) "完成" else "下一步",
                modifier = Modifier.fillMaxWidth()
            ) {
                if (step == guideSteps.lastIndex) onComplete() else step++
            }
            Spacer(modifier = Modifier.height(12.dp))
            SecondaryOutlineButton(
                text = "去开启",
                modifier = Modifier.fillMaxWidth(),
                onClick = onOpenSettings
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

/**
 * 渐变线条风格插图 —— 用 Canvas 手绘渐变描边线条，替代实色 Material 图标。
 */
@Composable
private fun GradientLineIllustration(
    type: StepIllustration,
    color: Color,
    modifier: Modifier = Modifier
) {
    val brush = Brush.linearGradient(listOf(color, color.copy(alpha = 0.5f)))
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height
        val stroke = Stroke(width = 3f, cap = StrokeCap.Round)
        when (type) {
            StepIllustration.ACCESSIBILITY -> {
                // 人形：头 + 身体 + 双臂 + 双腿
                drawCircle(brush = brush, radius = w * 0.08f, center = Offset(w * 0.5f, h * 0.22f), style = stroke)
                // 身体
                drawLine(brush, Offset(w * 0.5f, h * 0.32f), Offset(w * 0.5f, h * 0.62f), strokeWidth = 3f, cap = StrokeCap.Round)
                // 双臂
                drawLine(brush, Offset(w * 0.28f, h * 0.42f), Offset(w * 0.72f, h * 0.42f), strokeWidth = 3f, cap = StrokeCap.Round)
                // 双腿
                drawLine(brush, Offset(w * 0.5f, h * 0.62f), Offset(w * 0.32f, h * 0.85f), strokeWidth = 3f, cap = StrokeCap.Round)
                drawLine(brush, Offset(w * 0.5f, h * 0.62f), Offset(w * 0.68f, h * 0.85f), strokeWidth = 3f, cap = StrokeCap.Round)
            }
            StepIllustration.LAYERS -> {
                // 三层堆叠
                for (i in 0..2) {
                    val y = h * (0.25f + i * 0.22f)
                    drawLine(brush, Offset(w * 0.2f, y), Offset(w * 0.8f, y), strokeWidth = 3f, cap = StrokeCap.Round)
                    // 层间斜线
                    if (i < 2) {
                        drawLine(brush, Offset(w * 0.8f, y), Offset(w * 0.2f, y + h * 0.22f), strokeWidth = 2f, cap = StrokeCap.Round)
                    }
                }
            }
            StepIllustration.CHECK -> {
                // 对勾
                drawLine(brush, Offset(w * 0.25f, h * 0.5f), Offset(w * 0.42f, h * 0.67f), strokeWidth = 4f, cap = StrokeCap.Round)
                drawLine(brush, Offset(w * 0.42f, h * 0.67f), Offset(w * 0.75f, h * 0.3f), strokeWidth = 4f, cap = StrokeCap.Round)
            }
        }
    }
}

/** 3 步进度点：当前主渐变填充，已完成渐变描边，未完成灰色。 */
@Composable
private fun StepDots(total: Int, current: Int) {
    val isDark = LocalIsDark.current
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
    ) {
        for (i in 0 until total) {
            val isActive = i == current
            val isDone = i < current
            val boxModifier = when {
                isActive -> Modifier
                    .size(24.dp)
                    .clip(RoundedCornerShape(50))
                    .background(primaryLinearGradient(isDark))
                isDone -> Modifier
                    .size(14.dp)
                    .clip(RoundedCornerShape(50))
                    .border(1.dp, primaryLinearGradient(isDark), CircleShape)
                else -> Modifier
                    .size(10.dp)
                    .clip(RoundedCornerShape(50))
                    .background(if (isDark) Color(0xFF334155) else Color(0xFFCBD5E1))
            }
            Box(modifier = boxModifier)
        }
    }
}
