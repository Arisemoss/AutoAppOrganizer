package com.autoapporganizer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autoapporganizer.ui.OrganizeResult
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.AppIconThumb
import com.autoapporganizer.ui.components.CategoryFolderIcon
import com.autoapporganizer.ui.components.CountUpText
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.GradientCircularProgress
import com.autoapporganizer.ui.components.PrimaryGradientButton
import com.autoapporganizer.ui.components.bounceClick
import com.autoapporganizer.ui.components.gradientBorder
import com.autoapporganizer.ui.theme.AppCategory
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.primaryLinearGradient
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * 页面 3：整理完成 / Result
 *
 * 成功动画：圆环收缩为一点 → 延迟 400ms 后 12 个分类图标从中心向四周径向散开（600ms elastic out，错峰）。
 * 结果摘要卡片从底部滑入。
 */
@Composable
fun ResultScreen(
    result: OrganizeResult,
    onComplete: () -> Unit,
    onUndo: () -> Unit
) {
    val isDark = LocalIsDark.current
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight

    // 缓存分类列表，避免每次重组重排序
    val categoryList = remember(result) { result.categoryList }

    // 圆环收缩动画
    val ringScale = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        ringScale.animateTo(0f, animationSpec = tween(400))
    }

    // 摘要卡片入场
    val cardState = remember { MutableTransitionState(false) }
    LaunchedEffect(Unit) {
        delay(300)
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

            // ── 径向散射爆发动画区 ──
            Box(
                modifier = Modifier.fillMaxWidth().height(160.dp),
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
                // 分类图标从中心向四周径向散开
                val burstItems = categoryList.take(12)
                burstItems.forEachIndexed { i, (cat, _) ->
                    BurstIcon(
                        category = cat,
                        index = i,
                        total = burstItems.size
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // ── 结果摘要卡片 ──
            AnimatedVisibility(
                visibleState = cardState,
                enter = slideInVertically(
                    animationSpec = tween(400),
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

                        // 分类列表 —— weight(1f) 约束高度，保证底部按钮可见
                        LazyColumn(
                            modifier = Modifier.weight(1f, fill = false).fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(categoryList, key = { it.first.name }) { (cat, count) ->
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

/**
 * 径向散射爆发图标：从中心向圆周位置位移 + scale 0→1 弹性。
 * 延迟 [400ms + index*50ms] 启动，等圆环收缩完成后爆发。
 */
@Composable
private fun BurstIcon(category: AppCategory, index: Int, total: Int) {
    val angle = (index.toFloat() / total) * 2f * Math.PI.toFloat()
    val radius = 120f // 散开半径（dp）
    val targetX = (cos(angle) * radius)
    val targetY = (sin(angle) * radius * 0.7f) // 椭圆散射，纵向略压

    val scale = remember { Animatable(0f) }
    val x = remember { Animatable(0f) }
    val y = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        delay(400L + index * 50L)
        launch {
            scale.animateTo(
                1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            x.animateTo(
                targetX,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            y.animateTo(
                targetY,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    Box(
        modifier = Modifier.graphicsLayer {
            translationX = x.value
            translationY = y.value
            scaleX = scale.value
            scaleY = scale.value
            alpha = scale.value
        }
    ) {
        CategoryFolderIcon(category = category, tileSize = 44, showName = false)
    }
}

/** 统计列：CountUp 渐变数字 + 标签（12sp）。 */
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
            style = MaterialTheme.typography.labelMedium.copy(fontSize = 12.sp),
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
