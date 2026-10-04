package com.autoapporganizer.core.layout

import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.util.DiagnosticLogger

/**
 * 拖拽优化器 —— 负责两件事：
 * 1. [prioritizeCategories]：决定先整理哪个分类（数量多、空间集中的优先）
 * 2. [optimizeCategory]：为单个分类生成拖拽计划（锚点对建夹 + 剩余图标按距离拖入）
 *
 * 生产链路只接线了 [prioritizeCategories]；[optimizeCategory] 供测试与后续
 * 计划型执行器使用，契约与 DragOptimizerTest 保持一致。
 */
object DragOptimizer {

    private const val TAG = "DragOptimizer"

    /**
     * 对分类进行优先级排序。
     *
     * 排序策略：
     * 1. 图标数量多的分类优先（一次整理收益最大）
     * 2. 数量相同时，空间分散度小的优先（图标聚集的分类拖拽路径更短，
     *    先做低风险高收益的分类）
     *
     * @param categories 分类名 → 该分类的图标列表
     * @return 按优先级排序的分类名列表
     */
    fun prioritizeCategories(categories: Map<String, List<ScreenElement>>): List<String> {
        return categories.entries
            .sortedWith(
                compareByDescending<Map.Entry<String, List<ScreenElement>>> { it.value.size }
                    .thenBy { spatialDispersion(it.value) }
            )
            .map { it.key }
    }

    /**
     * 为单个分类生成拖拽计划。
     *
     * 契约：
     * - 少于 2 个元素：无法建夹，anchor=null、ordered=原列表、无拖拽步骤
     * - ≥2 个元素：锚点对（[SpatialClusterer.findAnchorPair]）拖拽建夹，
     *   文件夹落在 second 图标位置；ordered 恰好包含全部元素（锚点对在前，
     *   其余按到 second 的距离升序）；dragSteps = 1 步建夹 + (n-2) 步拖入
     */
    fun optimizeCategory(elements: List<ScreenElement>): CategoryDragPlan {
        if (elements.size < 2) {
            return CategoryDragPlan(
                anchor = null,
                ordered = elements,
                dragSteps = emptyList()
            )
        }

        val (anchorIdx, secondIdx) = SpatialClusterer.findAnchorPair(elements)
        val anchor = elements[anchorIdx]
        val second = elements[secondIdx]

        // 剩余图标按到 second（即文件夹落点）的距离升序，拖拽路径最短。
        val rest = elements.indices
            .filter { it != anchorIdx && it != secondIdx }
            .sortedBy { idx ->
                val dx = elements[idx].centerX - second.centerX
                val dy = elements[idx].centerY - second.centerY
                dx * dx + dy * dy
            }

        val steps = buildList {
            add(
                DragStep(
                    fromIndex = anchorIdx,
                    fromLabel = anchor.label,
                    toIndex = secondIdx,
                    toLabel = second.label,
                    isFolderCreation = true
                )
            )
            rest.forEach { idx ->
                add(
                    DragStep(
                        fromIndex = idx,
                        fromLabel = elements[idx].label,
                        toIndex = secondIdx,
                        toLabel = second.label,
                        isFolderCreation = false
                    )
                )
            }
        }

        DiagnosticLogger.debug(
            TAG,
            "Optimized category: ${elements.size} icons, ${steps.size} steps, " +
                "anchor=${anchor.label}, dispersion=${spatialDispersion(elements)}"
        )

        return CategoryDragPlan(
            anchor = anchor,
            ordered = listOf(anchor, second) + rest.map { elements[it] },
            dragSteps = steps
        )
    }

    /**
     * 计算一组图标的空间分散度（到质心距离平方的均值）。
     *
     * 值越小表示图标越聚集，整理时的拖拽总距离越短。
     * 除以 n 而非 n-1：仅用于排序，量纲差一个常数因子不影响次序。
     */
    private fun spatialDispersion(elements: List<ScreenElement>): Float {
        if (elements.size <= 1) return 0f
        val centroid = SpatialClusterer.computeCentroid(elements)
        var sumSq = 0f
        for (el in elements) {
            val dx = el.centerX - centroid.x
            val dy = el.centerY - centroid.y
            sumSq += dx * dx + dy * dy
        }
        return sumSq / elements.size
    }
}

/**
 * 单个分类的拖拽计划。
 *
 * @param anchor    建夹锚点（<2 个元素时为 null）
 * @param ordered   处理顺序的全部图标（锚点对在前，其余按到文件夹落点的距离排序）
 * @param dragSteps 拖拽步骤：1 步建夹 + (n-2) 步拖入
 */
data class CategoryDragPlan(
    val anchor: ScreenElement?,
    val ordered: List<ScreenElement>,
    val dragSteps: List<DragStep>
)
