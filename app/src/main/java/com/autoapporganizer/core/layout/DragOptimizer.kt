package com.autoapporganizer.core.layout

import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.util.DiagnosticLogger

/**
 * 拖拽优化器 —— 参考 Operit GraphVisualizer 的力导向布局优化思路。
 *
 * 在 DesktopOrganizeTask 的整理流程中集成空间优化：
 * 1. 同一分类的图标按空间临近度排序，减少拖拽距离
 * 2. 文件夹创建在空间中心位置
 * 3. 跨分类排序：优先处理图标数量多、分布集中的分类
 *
 * 使用方式：
 * ```
 * val optimized = DragOptimizer.optimizeCategory(categoryElements)
 * // optimized.anchor → 最近的图标，optimized.ordered → 排序后的图标列表
 * ```
 */
object DragOptimizer {

    private const val TAG = "DragOptimizer"

    /**
     * 对多个分类进行优先级排序。
     *
     * 排序策略（参考 Operit 的知识图谱批量处理）：
     * 1. 图标数量多的优先（减少碎片化）
     * 2. 空间分布集中的优先（密集分类先处理，减少后续图标移位影响）
     * 3. 包含新创建分类的优先（AI 发现的新分类需要优先创建文件夹）
     *
     * @param categories 分类名 → 图标列表的映射
     * @return 排序后的分类名列表
     */
    fun prioritizeCategories(
        categories: Map<String, List<ScreenElement>>
    ): List<String> {
        return categories.entries
            .sortedWith(
                compareByDescending<Map.Entry<String, List<ScreenElement>>> { entry ->
                    // 1. 图标数量多的优先
                    entry.value.size
                }.thenBy { entry ->
                    // 2. 空间分布分散度（方差）小的优先，即密集的优先
                    spatialDispersion(entry.value)
                }
            )
            .map { it.key }
    }

    /**
     * 优化单个分类的拖拽序列。
     *
     * 返回优化后的动作序列和锚点信息。
     *
     * @param elements 同一分类的图标列表
     * @return 优化结果
     */
    fun optimizeCategory(elements: List<ScreenElement>): CategoryDragPlan {
        if (elements.size < 2) {
            return CategoryDragPlan(
                anchor = elements.firstOrNull(),
                ordered = elements,
                dragSteps = emptyList()
            )
        }

        val steps = SpatialClusterer.optimizeDragSequence(elements)
        val (anchorIdx, _) = SpatialClusterer.findAnchorPair(elements)

        // 按拖拽步骤排序图标
        val orderedIndices = steps.map { it.fromIndex }.distinct()
        val ordered = orderedIndices.map { elements[it] }

        DiagnosticLogger.debug(
            TAG,
            "Optimized category: ${elements.size} icons, ${steps.size} steps, " +
                "anchor=${elements[anchorIdx].label}, " +
                "dispersion=${spatialDispersion(elements)}"
        )

        return CategoryDragPlan(
            anchor = elements[anchorIdx],
            ordered = ordered,
            dragSteps = steps
        )
    }

    /**
     * 计算一组图标的空间分散度（方差）。
     *
     * 值越小表示图标越集中，整理时拖拽距离越短。
     */
    private fun spatialDispersion(elements: List<ScreenElement>): Float {
        if (elements.size <= 1) return 0f
        val centroid = SpatialClusterer.computeCentroid(elements)
        var sumSqDist = 0f
        for (el in elements) {
            val dx = el.centerX - centroid.x
            val dy = el.centerY - centroid.y
            sumSqDist += dx * dx + dy * dy
        }
        return sumSqDist / elements.size
    }
}

/**
 * 单个分类的拖拽计划。
 */
data class CategoryDragPlan(
    /** 锚点图标（文件夹创建位置） */
    val anchor: ScreenElement?,
    /** 优化排序后的图标列表 */
    val ordered: List<ScreenElement>,
    /** 拖拽步骤序列 */
    val dragSteps: List<DragStep>
)