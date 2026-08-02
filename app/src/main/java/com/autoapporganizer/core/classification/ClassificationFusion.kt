package com.autoapporganizer.core.classification

import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger

/**
 * 分类融合层 —— 参考 Operit 的多路信号融合思路。
 *
 * 融合策略（AI 优先 + 关键词兜底）：
 * 1. AI 语义分类结果优先采用
 * 2. 低置信度（< CONFIDENCE_THRESHOLD）的项回退到 CategoryMatcher 关键词匹配
 * 3. AI 未覆盖的图标（如 VLM 漏检的）由 CategoryMatcher 补充分类
 * 4. 最终返回统一的分组结果
 *
 * 与 [PerceptionFusion] 的分工：
 * - PerceptionFusion：合并空间检测结果（VLM 坐标 + 无障碍节点树坐标）
 * - ClassificationFusion：合并分类结果（AI 语义分类 + 关键词分类）
 */
object ClassificationFusion {

    private const val TAG = "ClassificationFusion"

    /**
     * 融合 AI 分类、缓存分类和关键词分类，生成最终分组。
     *
     * @param aiResponse AI 分类响应（可为 null，表示 AI 不可用）
     * @param elements 桌面元素列表
     * @param categoryMatcher 关键词分类器（兜底）
     * @param cachedCategories 缓存中的分类结果 (label → category)，来自持久化缓存
     * @return 分类名 → 元素列表的映射
     */
    fun fuse(
        aiResponse: ClassificationResponse?,
        elements: List<ScreenElement>,
        categoryMatcher: CategoryMatcher,
        cachedCategories: Map<String, String> = emptyMap()
    ): Map<String, List<ScreenElement>> {
        // 构建 label → ScreenElement 的查找表
        val elementMap = buildElementMap(elements)

        if (aiResponse == null) {
            DiagnosticLogger.debug(TAG, "AI classification unavailable, falling back to keyword matching")
            return fallbackToKeyword(elements, categoryMatcher)
        }

        DiagnosticLogger.info(
            TAG,
            "Fusing: AI ${aiResponse.categories.size} categories + ${aiResponse.uncertain.size} uncertain " +
                "with ${elements.size} screen elements"
        )

        val result = mutableMapOf<String, MutableList<ScreenElement>>()
        val classifiedLabels = mutableSetOf<String>()

        // 1. 接受 AI 高置信度分类
        for (catResult in aiResponse.categories) {
            for (app in catResult.apps) {
                // 跳过低置信度的项，交给关键词处理
                if (app.confidence < CLASSIFICATION_CONFIDENCE_THRESHOLD) {
                    DiagnosticLogger.debug(
                        TAG,
                        "Low confidence: '${app.label}' → ${app.category} (${app.confidence})"
                    )
                    continue
                }

                val matched = findElement(app.label, elementMap)
                if (matched != null) {
                    result.getOrPut(catResult.category) { mutableListOf() }.add(matched)
                    classifiedLabels.add(app.label)
                }
            }
        }

        // 2. 处理 AI 标记为 uncertain 的项 → 缓存优先 > 关键词兜底
        for (app in aiResponse.uncertain) {
            if (classifiedLabels.contains(app.label)) continue
            val matched = findElement(app.label, elementMap)
            if (matched != null) {
                val fallbackCategory = resolveFallbackCategory(matched.label, cachedCategories, categoryMatcher)
                result.getOrPut(fallbackCategory) { mutableListOf() }.add(matched)
                classifiedLabels.add(app.label)
                DiagnosticLogger.debug(
                    TAG,
                    "Uncertain '${app.label}' → fallback: $fallbackCategory"
                )
            }
        }

        // 3. AI 未覆盖的图标 → 缓存优先 > 关键词兜底
        val unclassified = elements.filter { el ->
            !classifiedLabels.contains(el.label)
        }
        if (unclassified.isNotEmpty()) {
            DiagnosticLogger.debug(TAG, "${unclassified.size} unclassified elements, using cache/keyword fallback")
            for (el in unclassified) {
                val category = resolveFallbackCategory(el.label, cachedCategories, categoryMatcher)
                result.getOrPut(category) { mutableListOf() }.add(el)
            }
        }

        // 4. 合并过小的分类到"其他"
        val merged = mergeSmallCategories(result)

        DiagnosticLogger.info(
            TAG,
            "Fusion complete: ${merged.size} categories, ${merged.values.sumOf { it.size }} elements"
        )
        return merged
    }

    /**
     * 纯关键词兜底分类（AI 不可用时）。
     */
    private fun fallbackToKeyword(
        elements: List<ScreenElement>,
        categoryMatcher: CategoryMatcher
    ): Map<String, List<ScreenElement>> {
        val result = mutableMapOf<String, MutableList<ScreenElement>>()
        for (el in elements) {
            val category = categoryMatcher.matchCategory(el.label)
            result.getOrPut(category) { mutableListOf() }.add(el)
        }
        return mergeSmallCategories(result)
    }

    /**
     * 兜底分类决策：缓存优先，关键词匹配兜底。
     */
    private fun resolveFallbackCategory(
        label: String,
        cachedCategories: Map<String, String>,
        categoryMatcher: CategoryMatcher
    ): String {
        return cachedCategories[label] ?: categoryMatcher.matchCategory(label)
    }

    /**
     * 将只有 1 个元素的分类合并到"其他"。
     */
    private fun mergeSmallCategories(
        categorized: Map<String, MutableList<ScreenElement>>
    ): Map<String, List<ScreenElement>> {
        val result = mutableMapOf<String, MutableList<ScreenElement>>()
        val others = mutableListOf<ScreenElement>()

        for ((category, items) in categorized) {
            if (category == "其他") {
                others.addAll(items)
            } else if (items.size < 2) {
                others.addAll(items)
            } else {
                result[category] = items
            }
        }

        if (others.isNotEmpty()) {
            result["其他"] = others
        }

        return result
    }

    /**
     * 构建 label → ScreenElement 的查找表。
     * 使用模糊匹配（子串包含），因为 VLM 返回的标签可能与无障碍节点树的标签不完全一致。
     * 注意：同标签的多个元素会保留最后一个，这是预期行为（VLM 无法区分同标签元素）。
     */
    private fun buildElementMap(elements: List<ScreenElement>): Map<String, ScreenElement> {
        val byLabel = elements.groupBy { it.label }
        val duplicates = byLabel.filter { it.value.size > 1 }
        if (duplicates.isNotEmpty()) {
            DiagnosticLogger.warn(
                TAG,
                "Duplicate labels detected: ${duplicates.keys.joinToString()}, keeping last of each"
            )
        }
        return elements.associateBy { it.label }
    }

    /**
     * 根据标签查找对应的 ScreenElement。
     * 首先尝试精确匹配，失败则尝试模糊匹配（要求子串长度 >= 较短标签的 50% 以避免误匹配）。
     */
    private fun findElement(
        label: String,
        elementMap: Map<String, ScreenElement>
    ): ScreenElement? {
        // 精确匹配
        elementMap[label]?.let { return it }

        // 模糊匹配：VLM 返回的标签可能只是无障碍标签的一部分
        val lowerLabel = label.lowercase()
        for ((key, element) in elementMap) {
            val lowerKey = key.lowercase()
            val containsKey = lowerKey.contains(lowerLabel)
            val containsLabel = lowerLabel.contains(lowerKey)
            if (containsKey || containsLabel) {
                // 子串长度至少为较短标签的 50%，避免"计算器"与"计算机"误匹配
                val minLen = minOf(lowerLabel.length, lowerKey.length)
                val matchLen = if (containsKey) lowerLabel.length else lowerKey.length
                if (matchLen.toFloat() / minLen >= 0.5f) {
                    return element
                }
            }
        }

        return null
    }
}