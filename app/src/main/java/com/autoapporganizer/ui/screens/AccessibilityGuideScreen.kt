package com.autoapporganizer.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.outlined.Accessibility
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Layers
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
    val icon: ImageVector,
    val accent: Color
)

private val guideSteps = listOf(
    GuideStep("开启无障碍服务", "允许「桌面整理」读取桌面节点并执行拖拽手势，这是自动分类的前提。", Icons.Outlined.Accessibility, AuroraCyan),
    GuideStep("开启悬浮窗权限", "Android 15 与部分机型需要悬浮窗权限才能正常派发拖拽手势。", Icons.Outlined.Layers, ElectricPurple),
    GuideStep("准备就绪", "权限配置完成，回到主控制台即可一键整理桌面。", Icons.Outlined.CheckCircle, NeonPink)
)

/**
 * 页面 5：无障碍引导 / Accessibility Guide
 *
 * 步骤引导式全页设计：3 步进度点 + 渐变插图（蓝→紫→粉）+ 底部固定操作。
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
    val glowColor by animateColorAsState(current.accent, animationSpec = tween(500), label = "glow")

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

            // 插图 + 文案（步骤切换转场 + 色相渐变）
            AnimatedContent(
                targetState = step,
                transitionSpec = { fadeIn(tween(350)) togetherWith fadeOut(tween(200)) },
                label = "guideStep"
            ) { s ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
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
                        Box(
                            modifier = Modifier
                                .size(88.dp)
                                .clip(RoundedCornerShape(24.dp))
                                .background(Brush.linearGradient(listOf(glowColor, glowColor.copy(alpha = 0.6f)))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = guideSteps[s].icon,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(44.dp)
                            )
                        }
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
