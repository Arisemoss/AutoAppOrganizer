package com.autoapporganizer.ui.screens

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.autoapporganizer.ui.theme.flowingGradientColors
import com.autoapporganizer.ui.theme.primaryLinearGradient
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 页面 4：备份管理 / Backup
 *
 * 顶部标题 + 毛玻璃流光搜索框（聚焦时流光）+ 自动备份开关；
 * 列表展示备份条目（毛玻璃卡片 + 左侧状态渐变竖条）。
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
    val titleColor = if (isDark) Color(0xFFF1F5F9) else Color(0xFF0F172A)
    var query by remember { mutableStateOf("") }
    var searchFocused by remember { mutableStateOf(false) }

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

            // 毛玻璃流光搜索框（聚焦时流光）
            FlowingSearchField(
                query = query,
                onQueryChange = { query = it },
                focused = searchFocused,
                onFocusChange = { searchFocused = it },
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
                    // M4: 卡片间距 16dp（规格要求 margin 16dp）
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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

/**
 * 毛玻璃搜索框 —— 聚焦时边框流光渐变，非聚焦时静态边框。
 */
@Composable
private fun FlowingSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    focused: Boolean,
    onFocusChange: (Boolean) -> Unit,
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
    val colors = flowingGradientColors(isDark)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            // 毛玻璃背景
            .background(if (isDark) Color(0xB812121A) else Color(0xD9FFFFFF))
            .then(
                if (focused) {
                    // 聚焦时流光边框：用 drawBehind + size 构造跨全宽位移渐变
                    Modifier.drawBehind {
                        val span = size.width * 1.5f
                        val offset = shift * span - span * 0.25f
                        drawRoundRect(
                            brush = Brush.linearGradient(
                                colors = colors,
                                start = Offset(offset, 0f),
                                end = Offset(offset + span, 0f)
                            ),
                            style = Stroke(width = 2f),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
                        )
                    }
                } else {
                    // 非聚焦：静态 1px 边框
                    Modifier.border(
                        width = 1.dp,
                        color = if (isDark) Color(0x0FFFFFFF) else Color(0x0A000000),
                        shape = shape
                    )
                }
            )
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier
                .fillMaxSize()
                .onFocusChanged { onFocusChange(it.isFocused) },
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

/** 备份条目卡片 —— 毛玻璃 + 左侧 4dp 状态渐变竖条 + 时间/体积 + 还原/删除。 */
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
        Brush.linearGradient(listOf(SuccessGreen, Color(0xFF4ADE80)))
    } else {
        Brush.linearGradient(listOf(WarningAmber, Color(0xFFFB923C)))
    }
    // H6: 改回毛玻璃 blur=true
    GlassCard(modifier = Modifier.fillMaxWidth(), cornerRadius = 20, blur = true) {
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
    icon: ImageVector,
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

/**
 * 空状态：渐变线条绘制的云备份图标（Canvas 手绘渐变描边）+ 文案。
 */
@Composable
private fun EmptyBackupState(modifier: Modifier = Modifier) {
    val isDark = LocalIsDark.current
    val gradientBrush = primaryLinearGradient(isDark)
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // 渐变辉光底
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        Brush.radialGradient(
                            listOf(AuroraCyan.copy(alpha = 0.20f), Color.Transparent)
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                // 渐变线条云图标 —— Canvas 手绘
                Canvas(modifier = Modifier.size(56.dp)) {
                    val stroke = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    val w = size.width
                    val h = size.height
                    // 云朵下沿弧线
                    drawArc(
                        brush = gradientBrush,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(w * 0.15f, h * 0.35f),
                        size = androidx.compose.ui.geometry.Size(w * 0.7f, h * 0.5f),
                        style = stroke
                    )
                    // 左侧凸起弧
                    drawArc(
                        brush = gradientBrush,
                        startAngle = 160f,
                        sweepAngle = 140f,
                        useCenter = false,
                        topLeft = Offset(w * 0.18f, h * 0.15f),
                        size = androidx.compose.ui.geometry.Size(w * 0.35f, h * 0.45f),
                        style = stroke
                    )
                    // 右侧凸起弧
                    drawArc(
                        brush = gradientBrush,
                        startAngle = 200f,
                        sweepAngle = 140f,
                        useCenter = false,
                        topLeft = Offset(w * 0.47f, h * 0.12f),
                        size = androidx.compose.ui.geometry.Size(w * 0.38f, h * 0.5f),
                        style = stroke
                    )
                    // 上传箭头
                    drawLine(
                        brush = gradientBrush,
                        start = Offset(w * 0.5f, h * 0.75f),
                        end = Offset(w * 0.5f, h * 0.45f),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        brush = gradientBrush,
                        start = Offset(w * 0.4f, h * 0.55f),
                        end = Offset(w * 0.5f, h * 0.45f),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round
                    )
                    drawLine(
                        brush = gradientBrush,
                        start = Offset(w * 0.6f, h * 0.55f),
                        end = Offset(w * 0.5f, h * 0.45f),
                        strokeWidth = 2.5f,
                        cap = StrokeCap.Round
                    )
                }
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
