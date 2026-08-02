package com.autoapporganizer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.MutableTransitionState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.OrganizeResult
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.AppIconThumb
import com.autoapporganizer.ui.components.CategoryFolderIcon
import com.autoapporganizer.ui.components.CountUpText
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.GradientCircularProgress
import com.autoapporganizer.ui.components.GradientText
import com.autoapporganizer.ui.components.PrimaryGradientButton
import com.autoapporganizer.ui.components.bounceClick
import com.autoapporganizer.ui.components.gradientBorder
import com.autoapporganizer.ui.theme.AppCategory
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.primaryLinearGradient

/**
 * 页面 3：整理完成 / Result
 *
 * 成功动画：圆环收缩为一点 → 分类图标弹性爆发归位 → 结果摘要卡片从底部滑入。
 */
@Composable
fun ResultScreen(
    result: OrganizeResult,
    onComplete: () -> Unit,
    onUndo: () -> Unit
) {
    val isDark = LocalIsDark.current
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight

    // 圆环收缩动画（入场即播）
    val ringScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        ringScale.animateTo(0f, animationSpec = tween(400))
    }

    // 摘要卡片入场
    val cardState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        cardState.targetState = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ── 爆发动画区 ──
            Box(
                modifier = Modifier.fillMaxWidth().height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                // 收缩的圆环
                if (ringScale.value > 0.02f) {
                    GradientCircularProgress(
                        modifier = Modifier
                            .size(140.dp)
                            .graphicsLayer {
                                scaleX = ringScale.value
                                scaleY = ringScale.value
                                alpha = ringScale.value
                            },
                        progress = 1f,
                        strokeWidth = 6.dp,
                        glow = true
                    )
                }
                // 分类图标爆发归位
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    result.categoryList.take(12).forEachIndexed { i, (cat, _) ->
                        BurstIcon(category = cat, index = i)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── 结果摘要卡片 ──
            AnimatedVisibility(
                visibleState = cardState,
                enter = slideInVertically(
                    animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    initialOffsetY = { it / 2 }
                ) + fadeIn(animationSpec = tween(400))
            ) {
                GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 28) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            text = "整理完成 🎉",
                            style = MaterialTheme.typography.headlineMedium,
                            color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // 统计三列
                        Row(modifier = Modifier.fillMaxWidth()) {
                            StatColumn(result.folderCount, "个文件夹", Modifier.weight(1f))
                            StatColumn(result.appCount, "个应用", Modifier.weight(1f))
                            StatColumn(result.screensSaved, "节省屏数", Modifier.weight(1f))
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 分类列表
                        LazyColumn(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(result.categoryList, key = { it.first.name }) { (cat, count) ->
                                CategoryRow(category = cat, count = count)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        // 底部双按钮
                        PrimaryGradientButton(
                            text = "完成",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onComplete
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        RedOutlineButton(
                            text = "撤销整理",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = onUndo
                        )
                    }
                }
            }
        }
    }
}

/** 爆发归位的单个分类图标：scale 0→1 弹性，延迟入场。 */
@Composable
private fun BurstIcon(category: AppCategory, index: Int) {
    val state = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        // 等圆环收缩后再爆发
        state.targetState = true
    }
    AnimatedVisibility(
        visibleState = state,
        enter = scaleIn(
            animationSpec = spring(
                dampingRatio = Spring.DampingRatioMediumBouncy,
                stiffness = Spring.StiffnessMediumLow
            ),
            initialScale = 0.2f
        ) + fadeIn(animationSpec = tween(300, delayMillis = 0))
    ) {
        CategoryFolderIcon(category = category, tileSize = 44, showName = false)
    }
}

/** 统计列：CountUp 渐变数字 + 标签。 */
@Composable
private fun StatColumn(value: Int, label: String, modifier: Modifier = Modifier) {
    val isDark = LocalIsDark.current
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CountUpText(
            target = value,
            style = MaterialTheme.typography.headlineMedium,
            brush = primaryLinearGradient(isDark)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = if (isDark) TextSecondaryDark else TextSecondaryLight,
            textAlign = TextAlign.Center
        )
    }
}

/** 分类行：文件夹图标 + 名称 + 重叠应用缩略图。 */
@Composable
private fun CategoryRow(category: AppCategory, count: Int) {
    val isDark = LocalIsDark.current
    val textColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CategoryFolderIcon(category = category, tileSize = 44, showName = false)
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = category.label,
            style = MaterialTheme.typography.titleMedium,
            color = textColor,
            modifier = Modifier.weight(1f)
        )
        // 重叠应用缩略图（前 4 个，后压前 12dp）
        Row(horizontalArrangement = Arrangement.spacedBy((-12).dp)) {
            val shown = minOf(4, count)
            repeat(shown) { i ->
                AppIconThumb(
                    name = (i + 1).toString(),
                    categoryStart = category.start,
                    categoryEnd = category.end,
                    size = 28
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "${count}",
            style = MaterialTheme.typography.labelMedium,
            color = if (isDark) TextSecondaryDark else TextSecondaryLight
        )
    }
}

/** 红色渐变描边按钮（撤销）。 */
@Composable
private fun RedOutlineButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val isDark = LocalIsDark.current
    val shape = RoundedCornerShape(16.dp)
    val redBrush = Brush.linearGradient(listOf(ErrorRed, Color(0xFFF87171)))
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (isDark) Color(0x14FF5C5C) else Color(0x0AFF5C5C))
            .gradientBorder(brush = redBrush, width = 1.dp, shape = shape)
            .bounceClick(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = ErrorRed
        )
    }
}
