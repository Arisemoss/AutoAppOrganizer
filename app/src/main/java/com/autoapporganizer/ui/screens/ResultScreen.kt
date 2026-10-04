package com.autoapporganizer.ui.screens

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.components.AdaptiveContainer
import com.autoapporganizer.ui.components.AuroraButton
import com.autoapporganizer.ui.components.GhostButton
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.StatChip
import com.autoapporganizer.ui.components.StatusBadge
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.Spacing
import com.autoapporganizer.ui.theme.SuccessGreen

/**
 * 整理结果页 —— 大状态徽标 + 统计 + 撤销/返回。
 *
 * 数据全部来自整理回调参数（不再读旧历史），撤销仅在成功且建出文件夹时可用。
 */
@Composable
fun ResultScreen(
    windowSize: WindowSize,
    success: Boolean,
    folderCount: Int,
    message: String,
    onUndo: () -> Unit,
    onHome: () -> Unit,
    modifier: Modifier = Modifier
) {
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
                    StatusCircle(success)
                    StatusBadge(
                        success = success,
                        text = if (success) "整理完成" else "整理未完成"
                    )
                    Text(
                        text = message.ifBlank { if (success) "桌面已整理" else "整理未完成，请重试" },
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                        textAlign = TextAlign.Center
                    )
                    if (success && folderCount > 0) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(Spacing.m)
                        ) {
                            StatChip(
                                value = folderCount.toString(),
                                label = "创建文件夹",
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(Spacing.m)
                    ) {
                        AuroraButton(
                            text = "返回首页",
                            onClick = onHome,
                            modifier = Modifier.fillMaxWidth()
                        )
                        if (success && folderCount > 0) {
                            GhostButton(
                                text = "撤销整理",
                                onClick = onUndo,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

/** 大状态徽标 —— 成功绿勾 / 失败红叉（纯 Canvas，无图标依赖）。 */
@Composable
private fun StatusCircle(success: Boolean) {
    val color = if (success) SuccessGreen else ErrorRed
    Box(
        modifier = Modifier
            .size(88.dp)
            .clip(CircleShape)
            .background(color.copy(alpha = 0.14f)),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.22f)),
            contentAlignment = Alignment.Center
        ) {
            androidx.compose.foundation.Canvas(modifier = Modifier.size(36.dp)) {
                val stroke = Stroke(width = 5f, cap = StrokeCap.Round)
                if (success) {
                    // 对勾
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.18f, size.height * 0.55f),
                        end = Offset(size.width * 0.42f, size.height * 0.78f),
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.42f, size.height * 0.78f),
                        end = Offset(size.width * 0.82f, size.height * 0.24f),
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )
                } else {
                    // 叉
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.24f, size.height * 0.24f),
                        end = Offset(size.width * 0.76f, size.height * 0.76f),
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        color = color,
                        start = Offset(size.width * 0.76f, size.height * 0.24f),
                        end = Offset(size.width * 0.24f, size.height * 0.76f),
                        strokeWidth = 5f,
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

