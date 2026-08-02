package com.autoapporganizer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.autoapporganizer.ui.theme.AppCategory
import com.autoapporganizer.ui.theme.ShapeCategoryTile

/**
 * 分类文件夹图标 —— 圆角矩形（20dp）+ 分类渐变背景 + 白色线性图标。
 * 默认在图标下方 8dp 处显示分类名（12sp 白色 80%）。
 */
@Composable
fun CategoryFolderIcon(
    category: AppCategory,
    modifier: Modifier = Modifier,
    tileSize: Int = 56,
    showName: Boolean = true
) {
    Column(
        modifier = modifier.width(tileSize.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(tileSize.dp)
                .clip(ShapeCategoryTile)
                .background(category.gradient),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = category.icon,
                contentDescription = category.label,
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
        }
        if (showName) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.label,
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
        }
    }
}
