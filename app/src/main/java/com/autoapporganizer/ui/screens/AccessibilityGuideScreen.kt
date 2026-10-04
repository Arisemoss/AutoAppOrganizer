package com.autoapporganizer.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.ui.components.AdaptiveContainer
import com.autoapporganizer.ui.components.GhostButton
import com.autoapporganizer.ui.components.GlassCard
import com.autoapporganizer.ui.components.SectionTitle
import com.autoapporganizer.ui.components.StatusBadge
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.theme.Spacing

/**
 * 无障碍指南页 —— 三项权限的状态与一键跳转。
 *
 * 必需：无障碍服务（识别图标/派发手势）、悬浮窗（Android 15 与小米设备上
 * 保证手势派发）。可选：使用统计（「不常用」分类依赖它）。
 */
@Composable
fun AccessibilityGuideScreen(
    windowSize: WindowSize,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    // remember { } 非状态快照：进入本屏时检查一次即可，跳设置后返回会重建。
    val accessibilityOn = remember { AutoAppOrganizerService.instance != null }
    val overlayOn = remember { Settings.canDrawOverlays(context) }

    AdaptiveContainer(windowSize = windowSize, modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = Spacing.xl, vertical = Spacing.xl),
            verticalArrangement = Arrangement.spacedBy(Spacing.xl)
        ) {
            SectionTitle("开启以下权限后即可整理桌面")

            PermissionCard(
                title = "① 无障碍服务（必需）",
                description = "用于读取桌面图标并模拟拖拽手势，整理能力的核心依赖。",
                granted = accessibilityOn,
                grantedLabel = "已开启",
                deniedLabel = "未开启",
                buttonText = "前往开启无障碍",
                onButtonClick = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }
            )

            PermissionCard(
                title = "② 悬浮窗权限（Android 15 / 小米必需）",
                description = "部分系统要求悬浮窗权限才允许后台派发触摸手势。",
                granted = overlayOn,
                grantedLabel = "已授权",
                deniedLabel = "未授权",
                buttonText = "前往授权悬浮窗",
                onButtonClick = {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:${context.packageName}")
                    )
                    context.startActivity(intent)
                }
            )

            PermissionCard(
                title = "③ 使用统计权限（可选）",
                description = "开启后可把 7 天内几乎未使用的应用归入「不常用」分类。",
                granted = null,
                grantedLabel = "可选",
                deniedLabel = "可选",
                buttonText = "前往使用统计设置",
                onButtonClick = {
                    context.startActivity(
                        Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    )
                }
            )

            Spacer(Modifier.height(Spacing.s))
            Text(
                text = "提示：整理前会自动备份桌面布局，可在「备份与历史」中一键还原。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
        }
    }
}

@Composable
private fun PermissionCard(
    title: String,
    description: String,
    granted: Boolean?,
    grantedLabel: String,
    deniedLabel: String,
    buttonText: String,
    onButtonClick: () -> Unit
) {
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
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                StatusBadge(
                    success = granted,
                    text = if (granted == true) grantedLabel else deniedLabel
                )
            }
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
            )
            GhostButton(
                text = buttonText,
                onClick = onButtonClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
