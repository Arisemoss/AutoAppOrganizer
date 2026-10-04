package com.autoapporganizer.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.autoapporganizer.model.OrganizeSession
import com.autoapporganizer.ui.components.AdaptiveContainer
import com.autoapporganizer.ui.components.AuroraButton
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.SectionTitle
import com.autoapporganizer.ui.components.StatusBadge
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.theme.Spacing
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.HistoryManager
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 备份与历史页 —— 诚实语义版。
 *
 * 上半区：备份状态卡（有备份→大小+一键还原；无备份→说明整理时自动创建）。
 * 下半区：整理历史列表（每条可单独删除）—— 列表是"整理历史"，不是备份，
 * 删除只移除历史记录，不影响桌面与备份文件。
 */
@Composable
fun BackupScreen(
    windowSize: WindowSize,
    onUndoOrganize: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val backup = remember { BackupManager(context) }
    val history = remember { HistoryManager(context) }
    val sessions = remember { mutableStateListOf<OrganizeSession>() }
    var hasBackup = remember { backup.hasBackup() }
    val backupSize = remember { backup.getBackupSize() }

    LaunchedEffect(Unit) {
        // 文件 IO 放到默认 Dispatcher（LaunchedEffect 默认主线程）
        val loaded = kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            history.loadAll()
        }
        sessions.clear()
        sessions.addAll(loaded)
    }

    AdaptiveContainer(windowSize = windowSize, modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            // ── 备份状态卡 ──────────────────────────────────────────
            GlassCard {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(Spacing.xl),
                    verticalArrangement = Arrangement.spacedBy(Spacing.m)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "桌面备份",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Spacer(Modifier.height(Spacing.xs))
                            Text(
                                text = if (hasBackup) {
                                    "最近一次备份 · ${formatSize(backupSize)}"
                                } else {
                                    "暂无备份 —— 整理前会自动创建"
                                },
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                            )
                        }
                        StatusBadge(
                            success = if (hasBackup) true else null,
                            text = if (hasBackup) "已备份" else "无备份"
                        )
                    }
                    if (hasBackup) {
                        AuroraButton(
                            text = "还原桌面布局",
                            onClick = onUndoOrganize,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // ── 整理历史 ────────────────────────────────────────────
            SectionTitle("整理历史（${sessions.size}）")
            if (sessions.isEmpty()) {
                GlassCard {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Spacing.xxl),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "还没有整理记录",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.m),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(sessions, key = { it.timestamp }) { session ->
                        HistoryRow(
                            session = session,
                            onDelete = {
                                history.delete(session.timestamp)
                                sessions.removeAll { it.timestamp == session.timestamp }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryRow(session: OrganizeSession, onDelete: () -> Unit) {
    GlassCard {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Spacing.l, vertical = Spacing.m),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = formatTime(session.timestamp),
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Spacing.xs))
                Text(
                    text = "${session.folderCount} 个文件夹 · ${session.appCount} 个应用" +
                        (session.launcher?.let { " · $it" } ?: ""),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = Spacing.m)
                    .clickable(onClick = onDelete)
                    .padding(Spacing.s)
            ) {
                Text(
                    text = "删除",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

private val timeFormat = SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())

private fun formatTime(timestamp: Long): String = timeFormat.format(Date(timestamp))

private fun formatSize(bytes: Long): String = when {
    bytes >= 1024 * 1024 -> String.format(Locale.US, "%.1f MB", bytes / 1024f / 1024f)
    bytes > 0 -> String.format(Locale.US, "%.0f KB", bytes / 1024f)
    else -> "0 KB"
}
