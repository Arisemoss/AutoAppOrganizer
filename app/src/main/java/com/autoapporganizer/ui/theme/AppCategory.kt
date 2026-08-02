package com.autoapporganizer.ui.theme

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Forum
import androidx.compose.material.icons.outlined.LocalShipping
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.MoreHoriz
import androidx.compose.material.icons.outlined.MusicNote
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.PlayCircleOutline
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.SportsEsports
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 桌面应用分类调色板 —— 每个分类对应一组渐变色与线性图标。
 *
 * 分类名与 assets/categories.json 对齐：社交 / 购物 / 视频 / 音乐 / 工具 /
 * 阅读 / 出行 / 系统 / 游戏 / 学习 / 金融 / 健康 / 摄影，外加兜底的「其他」「不常用」。
 */
enum class AppCategory(
    val label: String,
    val start: Color,
    val end: Color,
    val icon: ImageVector
) {
    SOCIAL("社交", Color(0xFF3B82F6), Color(0xFF60A5FA), Icons.Outlined.Forum),
    SHOPPING("购物", Color(0xFFF59E0B), Color(0xFFFBBF24), Icons.Outlined.LocalShipping),
    VIDEO("视频", Color(0xFFEF4444), Color(0xFFF87171), Icons.Outlined.PlayCircleOutline),
    MUSIC("音乐", Color(0xFF8B5CF6), Color(0xFFA78BFA), Icons.Outlined.MusicNote),
    TOOLS("工具", Color(0xFF64748B), Color(0xFF94A3B8), Icons.Outlined.Calculate),
    READING("阅读", Color(0xFF10B981), Color(0xFF34D399), Icons.Outlined.MenuBook),
    TRAVEL("出行", Color(0xFF0EA5E9), Color(0xFF38BDF8), Icons.Outlined.DirectionsCar),
    SYSTEM("系统", Color(0xFF6B7280), Color(0xFF9CA3AF), Icons.Outlined.Settings),
    GAMES("游戏", Color(0xFFEC4899), Color(0xFFF472B6), Icons.Outlined.SportsEsports),
    STUDY("学习", Color(0xFFF97316), Color(0xFFFB923C), Icons.Outlined.School),
    FINANCE("金融", Color(0xFF14B8A6), Color(0xFF2DD4BF), Icons.Outlined.AccountBalance),
    HEALTH("健康", Color(0xFF22C55E), Color(0xFF4ADE80), Icons.Outlined.FitnessCenter),
    PHOTO("摄影", Color(0xFFD946EF), Color(0xFFE879F9), Icons.Outlined.PhotoCamera),
    OTHER("其他", Color(0xFF64748B), Color(0xFF94A3B8), Icons.Outlined.MoreHoriz),
    RARELY("不常用", Color(0xFF475569), Color(0xFF64748B), Icons.Outlined.MoreHoriz);

    /** 该分类的线性渐变（用于文件夹图标背景）。 */
    val gradient: Brush.LinearGradient
        get() = Brush.linearGradient(colors = listOf(start, end))

    companion object {
        /** 将中文分类名映射为 [AppCategory]，未知分类回退到 [OTHER]。 */
        fun fromLabel(label: String?): AppCategory {
            if (label == null) return OTHER
            return entries.firstOrNull { it.label == label } ?: OTHER
        }
    }
}
