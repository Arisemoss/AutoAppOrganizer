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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.ui.components.AdaptiveContainer
import com.autoapporganizer.ui.components.AuroraButton
import com.autoapporganizer.ui.components.GhostButton
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.SectionTitle
import com.autoapporganizer.ui.components.StatChip
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ElectricPurple
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.Spacing
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.HistoryManager
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * 首页 —— 极光渐变 Hero 卡 + 分段策略展示 + 统计行 + 快捷入口。
 *
 * 层次：Hero（唯一主 CTA）→ 策略（当前模式说明）→ 统计（数据概览）→ 入口（次级行为）。
 * Compact 单列；Expanded 双栏（左 Hero+策略，右统计+入口）。
 */
@Composable
fun HomeScreen(
    windowSize: WindowSize,
    onStartOrganize: () -> Unit,
    onStartVisionOrganize: () -> Unit,
    onOpenBackup: () -> Unit,
    onOpenGuide: () -> Unit,
    onOpenSettings: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val history = remember { HistoryManager(context) }
    val backup = remember { BackupManager(context) }
    val totalSessions = remember { history.totalSessions() }
    val latest = remember { history.latest() }
    val hasBackup = remember { backup.hasBackup() }
    val launcher = AutoAppOrganizerService.detectedLauncherPkg

    AdaptiveContainer(windowSize = windowSize, modifier = modifier.fillMaxSize()) {
        if (windowSize == WindowSize.Expanded) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = Spacing.xxl, vertical = Spacing.xxl),
                horizontalArrangement = Arrangement.spacedBy(Spacing.xxl)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1.15f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xl)
                ) {
                    HeroCard(onStartOrganize, onStartVisionOrganize)
                    StrategyCard()
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(Spacing.xl)
                ) {
                    StatsRow(totalSessions, latest, launcher)
                    QuickActions(hasBackup, onOpenBackup, onOpenGuide, onOpenSettings)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
                verticalArrangement = Arrangement.spacedBy(Spacing.xl)
            ) {
                HeroCard(onStartOrganize, onStartVisionOrganize)
                StatsRow(totalSessions, latest, launcher)
                StrategyCard()
                QuickActions(hasBackup, onOpenBackup, onOpenGuide, onOpenSettings)
            }
        }
    }
}

/** Hero 卡 —— 极光渐变底 + 主标题 + 唯一主 CTA。 */
@Composable
private fun HeroCard(
    onStartOrganize: () -> Unit,
    onStartVisionOrganize: () -> Unit
) {
    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        listOf(
                            ElectricPurple.copy(alpha = 0.16f),
                            AuroraCyan.copy(alpha = 0.10f),
                            androidx.compose.ui.graphics.Color.Transparent
                        )
                    )
                )
                .padding(Spacing.xxl)
        ) {
            Text(
                text = "桌面整理",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(Spacing.s))
            Text(
                text = "自动分类桌面图标到文件夹，支持传统无障碍与视觉 AI 双模式，整理前自动备份。",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.72f),
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight * 1.2
            )
            Spacer(Modifier.height(Spacing.xxl))
            AuroraButton(
                text = "开始整理",
                onClick = onStartOrganize,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(Spacing.m))
            GhostButton(
                text = "视觉 AI 整理",
                onClick = onStartVisionOrganize,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

/** 统计行 —— 三枚等宽数字胶囊：整理次数 / 最近整理 / 当前桌面。 */
@Composable
private fun StatsRow(
    totalSessions: Int,
    latest: com.autoapporganizer.model.OrganizeSession?,
    launcher: String?
) {
    SectionTitle("概览")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        StatChip(
            value = totalSessions.toString(),
            label = "整理次数",
            modifier = Modifier.weight(1f)
        )
        StatChip(
            value = latest?.let { formatRelative(it.timestamp) } ?: "—",
            label = "最近整理",
            modifier = Modifier.weight(1f)
        )
        StatChip(
            value = launcher?.substringBeforeLast('.') ?: "未检测",
            label = "当前桌面",
            modifier = Modifier.weight(1f)
        )
    }
}

/** 策略说明卡 —— 当前整理模式一览（详细切换在设置页）。 */
@Composable
private fun StrategyCard() {
    GlassCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.m)
        ) {
            SectionTitle("整理模式")
            StrategyRow("传统无障碍", "节点树识别 + 200+ 词库关键词分类", false)
            StrategyRow("视觉 AI", "截图交给 VLM 语义分类，支持 OpenAI / Gemini / GLM", true)
            StrategyRow("混合增强", "视觉优先，失败自动回退传统模式（推荐）", false)
        }
    }
}

@Composable
private fun StrategyRow(title: String, description: String, recommended: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.m)
    ) {
        Box(
            modifier = Modifier
                .width(4.dp)
                .height(36.dp)
                .clip(CircleShape)
                .background(
                    if (recommended) {
                        Brush.verticalGradient(listOf(ElectricPurple, AuroraCyan))
                    } else {
                        Brush.verticalGradient(
                            listOf(
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f),
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.15f)
                            )
                        )
                    }
                )
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }
    }
}

/** 快捷入口 —— 备份 / 无障碍指南 / 设置 / AI 配置。 */
@Composable
private fun QuickActions(
    hasBackup: Boolean,
    onOpenBackup: () -> Unit,
    onOpenGuide: () -> Unit,
    onOpenSettings: () -> Unit
) {
    SectionTitle("快捷入口")
    Column(verticalArrangement = Arrangement.spacedBy(Spacing.m)) {
        QuickActionRow("备份与历史", if (hasBackup) "已有备份，可一键还原" else "暂无备份，整理时自动创建", onOpenBackup)
        QuickActionRow("无障碍指南", "开启无障碍服务与悬浮窗权限", onOpenGuide)
        QuickActionRow("整理设置", "整理模式、文件夹数量、AI 视觉配置", onOpenSettings)
    }
}

@Composable
private fun QuickActionRow(title: String, subtitle: String, onClick: () -> Unit) {
    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(Spacing.l),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = "›",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
            )
        }
    }
}

/** 相对时间：分钟 / 小时 / 天。 */
private fun formatRelative(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
    return when {
        minutes < 1 -> "刚刚"
        minutes < 60 -> "${minutes} 分钟前"
        minutes < 60 * 24 -> "${TimeUnit.MILLISECONDS.toHours(diff)} 小时前"
        else -> String.format(Locale.US, "%d 天前", TimeUnit.MILLISECONDS.toDays(diff))
    }
}
