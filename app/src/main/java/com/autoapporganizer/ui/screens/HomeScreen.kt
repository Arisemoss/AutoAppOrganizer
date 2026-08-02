package com.autoapporganizer.ui.screens

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.OrbitVisualizer
import com.autoapporganizer.ui.components.PrimaryGradientButton
import com.autoapporganizer.ui.components.SecondaryOutlineButton
import com.autoapporganizer.ui.components.StatusChip
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.SkyCyan
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.primaryLinearGradient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 页面 1：主控制台 / Home
 *
 * 顶部日期 + 标题 + 副标题 + 渐变分割线；中部圆形可视化；底部悬浮毛玻璃操作卡片。
 */
@Suppress("UNUSED_PARAMETER")
@Composable
fun HomeScreen(
    pendingAppCount: Int,
    lastOrganizeLabel: String,
    backupLabel: String,
    ready: Boolean,
    onOrganize: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenSettings: () -> Unit,
    onVisionOrganize: () -> Unit,
    onDiagnose: () -> Unit
) {
    val isDark = LocalIsDark.current
    val dateText = remember {
        SimpleDateFormat("M月d日 EEE", Locale.CHINA).format(Date())
    }
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight
    val titleColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)

    Box(modifier = Modifier.fillMaxSize()) {
        AppBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // ── 顶部标题区 ──
            Text(text = dateText, style = MaterialTheme.typography.bodyMedium, color = secondaryText)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "桌面整理", style = MaterialTheme.typography.headlineLarge, color = titleColor)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "智能分类 · 一键归位", style = MaterialTheme.typography.bodyMedium, color = secondaryText)
            Spacer(modifier = Modifier.height(8.dp))
            // 渐变分割线：40dp 宽，2dp 高，圆角 1dp
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 2.dp)
                    .clip(RoundedCornerShape(1.dp))
                    .background(primaryLinearGradient(isDark))
            )

            Spacer(modifier = Modifier.weight(1f))

            // ── 中部圆形可视化 ──
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                OrbitVisualizer(
                    count = pendingAppCount,
                    countLabel = "个应用待整理",
                    progress = 0f
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            // ── 底部悬浮操作卡片 ──
            GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 24) {
                Column(modifier = Modifier.padding(24.dp)) {
                    // 快捷状态标签 —— 两个 chip 都带左侧色点
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusChip(
                            text = "上次整理：$lastOrganizeLabel",
                            leadingColor = if (isDark) AuroraCyan else SkyCyan
                        )
                        StatusChip(
                            text = "备份：$backupLabel",
                            leadingColor = if (isDark) Color(0xFF22C55E) else Color(0xFF16A34A)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // 主按钮
                    PrimaryGradientButton(
                        text = if (ready) "开始整理" else "开启权限后整理",
                        modifier = Modifier.fillMaxWidth(),
                        onClick = onOrganize
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // 次级按钮行（规格：查看备份 + 分类设置 等宽并排）
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        SecondaryOutlineButton(
                            text = "查看备份",
                            modifier = Modifier.weight(1f),
                            onClick = onOpenBackup
                        )
                        SecondaryOutlineButton(
                            text = "分类设置",
                            modifier = Modifier.weight(1f),
                            onClick = onOpenSettings
                        )
                    }
                }
            }

            // L6: 底部边距 24dp（规格要求卡片 margin 24dp）
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
