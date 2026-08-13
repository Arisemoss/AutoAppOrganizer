package com.autoapporganizer.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.GradientCircularProgress
import com.autoapporganizer.ui.components.TypewriterText
import com.autoapporganizer.ui.theme.AppCategory
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.MonoDisplayLarge
import com.autoapporganizer.ui.theme.MonoLabel
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight

/**
 * 页面 2：整理进度 / Organizing
 *
 * 全屏沉浸式：漂移渐变光斑 + 中央发光圆环 + 打字机状态 + 底部横向分类预览。
 */
@Composable
fun OrganizingScreen(
    progress: Float,
    statusMessage: String,
    categories: List<Pair<AppCategory, Int>>
) {
    val isDark = LocalIsDark.current
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight
    val pctColor = lerp(Color(0xFF64748B), Color.White, progress.coerceIn(0f, 1f))

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground(blobs = listOf { ImmersiveBlobs() })

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(modifier = Modifier.weight(1f))

            // 中央圆环 + 百分比
            Box(modifier = Modifier.size(220.dp), contentAlignment = Alignment.Center) {
                GradientCircularProgress(
                    modifier = Modifier.fillMaxSize(),
                    progress = progress,
                    strokeWidth = 8.dp,
                    glow = true
                )
                Text(
                    text = "${(progress * 100).toInt()}%",
                    style = MonoDisplayLarge.copy(fontSize = 48.sp),
                    color = pctColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            TypewriterText(
                text = statusMessage,
                style = MaterialTheme.typography.bodyMedium,
                color = secondaryText
            )

            Spacer(modifier = Modifier.weight(1f))

            // 底部横向分类预览
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(categories, key = { _, pair -> pair.first.name }) { index, (category, count) ->
                    CategoryPreviewCard(category = category, count = count, index = index)
                }
            }
        }
    }
}

/** 单个分类预览卡片：80×96，毛玻璃，从右侧滑入（stagger 100ms）。 */
@Composable
private fun CategoryPreviewCard(
    category: AppCategory,
    count: Int,
    index: Int
) {
    val visibleState = remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { visibleState.value = true }
    AnimatedVisibility(
        visible = visibleState.value,
        enter = slideInHorizontally(
            animationSpec = tween(300, delayMillis = index * 100),
            initialOffsetX = { it / 2 }
        ) + fadeIn(animationSpec = tween(300, delayMillis = index * 100))
    ) {
        GlassCard(modifier = Modifier.size(width = 80.dp, height = 96.dp), cornerRadius = 16) {
            Column(
                modifier = Modifier.fillMaxSize().padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(category.gradient),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = category.icon,
                        contentDescription = category.label,
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = count.toString(), style = MonoLabel, color = Color.White)
                Text(
                    text = category.label,
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.7f),
                    maxLines = 1
                )
            }
        }
    }
}

/** 三色漂移渐变光斑 —— 主渐变三色大半径模糊，缓慢漂移。 */
@Composable
private fun ImmersiveBlobs() {
    val isDark = LocalIsDark.current
    val colors = if (isDark) {
        listOf(Color(0xFF06B6D4), Color(0xFF8B5CF6), Color(0xFFEC4899))
    } else {
        listOf(Color(0xFF0EA5E9), Color(0xFFA78BFA), Color(0xFFF472B6))
    }
    val t = rememberInfiniteTransition(label = "blobs")
    val x1 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(9000, easing = LinearEasing), RepeatMode.Reverse), label = "x1")
    val y1 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(11000, easing = LinearEasing), RepeatMode.Reverse), label = "y1")
    val x2 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(12000, easing = LinearEasing), RepeatMode.Reverse), label = "x2")
    val y2 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(8000, easing = LinearEasing), RepeatMode.Reverse), label = "y2")
    val x3 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(10000, easing = LinearEasing), RepeatMode.Reverse), label = "x3")
    val y3 by t.animateFloat(0f, 1f, infiniteRepeatable(tween(13000, easing = LinearEasing), RepeatMode.Reverse), label = "y3")

    Box(Modifier.fillMaxSize()) {
        Blob(colors[0], x1, y1, Modifier.align(Alignment.TopStart))
        Blob(colors[1], x2, y2, Modifier.align(Alignment.TopEnd))
        Blob(colors[2], x3, y3, Modifier.align(Alignment.BottomCenter))
    }
}

@Composable
private fun Blob(color: Color, fx: Float, fy: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(320.dp)
            .graphicsLayer {
                translationX = fx * 200f - 100f
                translationY = fy * 200f - 100f
            }
            .background(Brush.radialGradient(listOf(color.copy(alpha = 0.45f), Color.Transparent)))
    )
}
