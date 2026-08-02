package com.autoapporganizer.core.layout

import android.graphics.PointF
import android.graphics.Rect
import com.autoapporganizer.core.perception.ScreenElement
import kotlin.math.abs
import kotlin.math.sqrt

/**
 * 空间聚类器 —— 参考 Operit GraphVisualizer 的连通分量聚类思想。
 *
 * 将桌面图标按网格位置聚合为空间簇，用于：
 * 1. 识别同一分类图标在桌面上的空间分布
 * 2. 选择最优的文件夹创建位置（空间中心）
 * 3. 按空间临近度排序拖拽顺序，减少拖拽距离
 */
object SpatialClusterer {

    /**
     * 网格容差（像素）：同一行/列的图标中心坐标偏差在此范围内视为对齐。
     * Android 桌面图标网格通常间距为 100-180px，容差设为 60px 足够鲁棒。
     */
    private const val GRID_TOLERANCE_PX = 60

    /**
     * 对一组图标进行空间聚类，返回按空间临近度排序的图标列表。
     *
     * 排序策略：
     * 1. 计算所有图标的几何中心（质心）
     * 2. 按距离质心的远近排序，近的优先
     * 3. 锚点（距离质心最近的图标）排在第一位，作为文件夹创建位置
     *
     * 这样做的效果：同一分类的图标中，最靠近空间中心的先被拖拽，
     * 文件夹创建在空间中心位置，后续图标向中心拖入，拖拽距离最小。
     *
     * @param elements 同一分类的图标列表
     * @return 排序后的图标索引列表（0-based），第一个是锚点
     */
    fun sortByProximity(elements: List<ScreenElement>): List<Int> {
        if (elements.size <= 1) return elements.indices.toList()

        // 计算质心
        val centroid = computeCentroid(elements)
        // 按距离排序
        return elements.indices
            .sortedBy { i -> distance(centroid, elements[i].centerX, elements[i].centerY) }
    }

    /**
     * 计算最优文件夹创建锚点。
     *
     * 返回两个图标的索引对：(anchor, second)，其中：
     * - anchor：距离质心最近的图标
     * - second：距离 anchor 最近的图标
     *
     * 拖拽 anchor 到 second 上创建文件夹，文件夹位置在质心附近。
     *
     * @param elements 同一分类的图标列表
     * @return Pair(anchorIndex, secondIndex)
     */
    fun findAnchorPair(elements: List<ScreenElement>): Pair<Int, Int> {
        require(elements.size >= 2) { "Need at least 2 elements for anchor pair" }

        val sorted = sortByProximity(elements)
        val anchor = sorted[0]

        // 从剩余图标中找距离 anchor 最近的
        val second = sorted.drop(1)
            .minByOrNull { i ->
                val dx = elements[i].centerX - elements[anchor].centerX
                val dy = elements[i].centerY - elements[anchor].centerY
                dx * dx + dy * dy
            } ?: sorted[1]

        return Pair(anchor, second)
    }

    /**
     * 计算图标列表的几何质心。
     */
    fun computeCentroid(elements: List<ScreenElement>): PointF {
        if (elements.isEmpty()) return PointF(0f, 0f)
        var sumX = 0f
        var sumY = 0f
        for (el in elements) {
            sumX += el.centerX
            sumY += el.centerY
        }
        return PointF(sumX / elements.size, sumY / elements.size)
    }

    /**
     * 对同一分类的图标进行拖拽顺序优化。
     *
     * 返回排序后的拖拽序列，
     * 每个元素为 (fromIndex, toIndex) 或 special marker。
     *
     * 优化策略：
     * 1. 第一步：拖拽 anchor → second（创建文件夹）
     * 2. 后续：按距离文件夹位置的远近，依次拖入剩余图标
     *
     * @param elements 同一分类的图标列表
     * @return 拖拽序列表，List<Pair<fromIndex, toIndex>>
     */
    fun optimizeDragSequence(elements: List<ScreenElement>): List<DragStep> {
        if (elements.size < 2) return emptyList()

        val (anchor, second) = findAnchorPair(elements)
        val steps = mutableListOf<DragStep>()

        // Step 1: 创建文件夹（anchor → second）
        steps.add(
            DragStep(
                fromIndex = anchor,
                fromLabel = elements[anchor].label,
                toIndex = second,
                toLabel = elements[second].label,
                isFolderCreation = true
            )
        )

        // 文件夹位置：second 的坐标（创建后 launcher 会自动对齐）
        val folderCenter = PointF(elements[second].centerX, elements[second].centerY)

        // Step 2+: 按距离文件夹位置的远近排序剩余图标
        val remaining = elements.indices
            .filter { it != anchor && it != second }
            .sortedBy { i ->
                distance(folderCenter, elements[i].centerX, elements[i].centerY)
            }

        for (idx in remaining) {
            steps.add(
                DragStep(
                    fromIndex = idx,
                    fromLabel = elements[idx].label,
                    toIndex = second, // 拖入文件夹
                    toLabel = elements[second].label,
                    isFolderCreation = false
                )
            )
        }

        return steps
    }

    /**
     * 判断两个图标是否在同一行（基于网格对齐）。
     */
    fun isSameRow(y1: Float, y2: Float): Boolean {
        return abs(y1 - y2) <= GRID_TOLERANCE_PX
    }

    /**
     * 判断两个图标是否在同一列（基于网格对齐）。
     */
    fun isSameColumn(x1: Float, x2: Float): Boolean {
        return abs(x1 - x2) <= GRID_TOLERANCE_PX
    }

    /**
     * 计算两点之间的欧几里得距离。
     */
    private fun distance(p: PointF, x: Float, y: Float): Float {
        val dx = p.x - x
        val dy = p.y - y
        return sqrt(dx * dx + dy * dy)
    }
}

/**
 * 单步拖拽操作描述。
 */
data class DragStep(
    /** 拖拽源图标索引 */
    val fromIndex: Int,
    /** 拖拽源图标标签 */
    val fromLabel: String,
    /** 拖拽目标索引（创建文件夹时是目标图标，拖入时是文件夹位置） */
    val toIndex: Int,
    /** 拖拽目标标签 */
    val toLabel: String,
    /** 是否为文件夹创建步骤（第一个拖拽） */
    val isFolderCreation: Boolean
)