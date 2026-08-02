package com.autoapporganizer.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CloudUpload
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Restore
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.autoapporganizer.ui.BackupEntry
import com.autoapporganizer.ui.components.AppBackground
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.GradientToggle
import com.autoapporganizer.ui.components.bounceClick
import com.autoapporganizer.ui.theme.AuroraCyan
import com.autoapporganizer.ui.theme.ErrorRed
import com.autoapporganizer.ui.theme.LocalIsDark
import com.autoapporganizer.ui.theme.MonoLabel
import com.autoapporganizer.ui.theme.SuccessGreen
import com.autoapporganizer.ui.theme.TextSecondaryDark
import com.autoapporganizer.ui.theme.TextSecondaryLight
import com.autoapporganizer.ui.theme.WarningAmber
import com.autoapporganizer.ui.theme.flowingPrimaryGradient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 页面 4：备份管理 / Backup
 *
 * 顶部标题 + 流光搜索框 + 自动备份开关；列表展示备份条目（左侧状态渐变竖条）。
 */
@Composable
fun BackupScreen(
    backups: List<BackupEntry>,
    autoBackup: Boolean,
    onAutoBackupChange: (Boolean) -> Unit,
    onRestore: (BackupEntry) -> Unit,
    onDelete: (BackupEntry) -> Unit,
    onBack: () -> Unit
) {
    val isDark = LocalIsDark.current
    val secondaryText = if (isDark) TextSecondaryDark else TextSecondaryLight
    val titleColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    var query by remember { mutableStateOf("") }

    val timeFmt = remember { SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA) }
    val filtered = remember(backups, query) {
        if (query.isBlank()) backups
        else backups.filter { timeFmt.format(Date(it.timestamp)).contains(query.trim(), ignoreCase = true) }
    }

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
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 12.dp),
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
                Text(text = "备份管理", style = MaterialTheme.typography.headlineMedium, color = titleColor)
            }

            // 流光搜索框
            FlowingSearchField(
                query = query,
                onQueryChange = { query = it },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 自动备份开关
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "自动备份",
                    style = MaterialTheme.typography.titleMedium,
                    color = titleColor,
                    modifier = Modifier.weight(1f)
                )
                GradientToggle(checked = autoBackup, onCheckedChange = onAutoBackupChange)
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filtered.isEmpty()) {
                EmptyBackupState(modifier = Modifier.weight(1f))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtered, key = { it.timestamp }) { entry ->
                        BackupCard(
                            entry = entry,
                            timeLabel = timeFmt.format(Date(entry.timestamp)),
                            onRestore = { onRestore(entry) },
                            onDelete = { onDelete(entry) }
                        )
                    }
                }
            }
        }
    }
}

/** 流光边框搜索框 —— 聚焦时主渐变沿边框流动。 */
@Composable
private fun FlowingSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isDark = LocalIsDark.current
    val transition = rememberInfiniteTransition(label = "searchFlow")
    val shift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(3000, easing = LinearEasing)),
        label = "shift"
    )
    val shape = RoundedCornerShape(16.dp)
    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .border(width = 1.dp, brush = flowingPrimaryGradient(isDark, shift), shape = shape)
            .background(if (isDark) Color(0x14FFFFFF) else Color(0x0A000000))
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.fillMaxSize(),
            placeholder = {
                Text("搜索备份时间…", style = MaterialTheme.typography.bodyMedium, color = if (isDark) TextSecondaryDark else TextSecondaryLight)
            },
            leadingIcon = {
                Icon(Icons.Outlined.Search, contentDescription = null, tint = if (isDark) TextSecondaryDark else TextSecondaryLight)
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Ascii),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                cursorColor = AuroraCyan
            )
        )
    }
}

/** 备份条目卡片 —— 左侧 4dp 状态渐变竖条 + 时间/体积 + 还原/删除。 */
@Composable
private fun BackupCard(
    entry: BackupEntry,
    timeLabel: String,
    onRestore: () -> Unit,
    onDelete: () -> Unit
) {
    val isDark = LocalIsDark.current
    val titleColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    val barBrush = if (entry.fresh) {
        androidx.compose.ui.graphics.Brush.linearGradient(listOf(SuccessGreen, Color(0xFF4ADE80)))
    } else {
        androidx.compose.ui.graphics.Brush.linearGradient(listOf(WarningAmber, Color(0xFFFB923C)))
    }
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20, blur = false) {
        Row(modifier = Modifier.fillMaxWidth().height(80.dp)) {
            // 状态竖条
            Box(modifier = Modifier.width(4.dp).fillMaxSize().background(barBrush))
            Row(
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = timeLabel,
                        style = MonoLabel.copy(fontSize = 14.sp),
                        color = titleColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${entry.folderCount} 文件夹 · ${entry.appCount} 应用 · ${entry.sizeLabel}",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isDark) TextSecondaryDark else TextSecondaryLight
                    )
                }
                ActionIcon(Icons.Outlined.Restore, "还原", AuroraCyan, onRestore)
                Spacer(modifier = Modifier.width(8.dp))
                ActionIcon(Icons.Outlined.DeleteOutline, "删除", ErrorRed, onDelete)
            }
        }
    }
}

@Composable
private fun ActionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    desc: String,
    tint: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(if (LocalIsDark.current) Color(0x14FFFFFF) else Color(0x0A000000))
            .bounceClick(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector = icon, contentDescription = desc, tint = tint, modifier = Modifier.size(20.dp))
    }
}

/** 空状态：渐变云备份图标 + 文案。 */
@Composable
private fun EmptyBackupState(modifier: Modifier = Modifier) {
    val isDark = LocalIsDark.current
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        androidx.compose.ui.graphics.Brush.radialGradient(
                            listOf(AuroraCyan.copy(alpha = 0.25f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.CloudUpload,
                    contentDescription = null,
                    tint = AuroraCyan,
                    modifier = Modifier.size(48.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "暂无备份",
                style = MaterialTheme.typography.titleMedium,
                color = if (isDark) TextSecondaryDark else TextSecondaryLight
            )
        }
    }
}
