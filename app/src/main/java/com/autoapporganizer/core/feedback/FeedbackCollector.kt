package com.autoapporganizer.core.feedback

import com.autoapporganizer.core.classification.ClassificationResponse
import com.autoapporganizer.util.DiagnosticLogger

/**
 * 分类反馈收集器 —— 参考 Operit 的知识图谱去重与合并机制。
 *
 * 收集和管理分类过程中的反馈信息：
 * 1. 低置信度分类项（供 UI 展示和用户审阅）
 * 2. 分类统计（成功率、AI 覆盖率等）
 * 3. 用户纠正记录
 */
class FeedbackCollector {

    companion object {
        private const val TAG = "FeedbackCollector"
    }

    /** 低置信度分类项列表 */
    private val lowConfidenceItems = mutableListOf<FeedbackItem>()

    /** AI 分类统计 */
    private var aiClassifiedCount = 0
    private var keywordFallbackCount = 0
    private var totalIcons = 0

    /**
     * 记录分类结果，提取低置信度项。
     */
    fun collect(response: ClassificationResponse, totalElements: Int) {
        totalIcons = totalElements

        // 收集低置信度项
        for (cat in response.categories) {
            for (app in cat.apps) {
                if (app.confidence < 0.5f) {
                    lowConfidenceItems.add(
                        FeedbackItem(
                            label = app.label,
                            aiCategory = app.category,
                            aiConfidence = app.confidence,
                            reasoning = app.reasoning
                        )
                    )
                } else {
                    aiClassifiedCount++
                }
            }
        }

        // 不确定的项
        for (app in response.uncertain) {
            lowConfidenceItems.add(
                FeedbackItem(
                    label = app.label,
                    aiCategory = "不确定",
                    aiConfidence = app.confidence,
                    reasoning = app.reasoning
                )
            )
        }

        // lowConfidenceItems 已包含 categories 中低置信度项 + uncertain 全部项，无需重复加
        keywordFallbackCount = lowConfidenceItems.size

        DiagnosticLogger.info(
            TAG,
            "Feedback: AI classified=$aiClassifiedCount, fallback=$keywordFallbackCount, " +
                "low confidence=${lowConfidenceItems.size}, total=$totalIcons"
        )
    }

    /**
     * 记录用户纠正。
     */
    fun recordCorrection(label: String, fromCategory: String, toCategory: String) {
        DiagnosticLogger.info(TAG, "User correction: '$label' $fromCategory → $toCategory")
        // 从低置信度列表中移除已纠正的项
        lowConfidenceItems.removeAll { it.label == label }
    }

    /**
     * 获取低置信度项列表（供 UI 展示）。
     */
    fun getLowConfidenceItems(): List<FeedbackItem> = lowConfidenceItems.toList()

    /**
     * 获取分类统计摘要。
     */
    fun getSummary(): FeedbackSummary {
        return FeedbackSummary(
            totalIcons = totalIcons,
            aiClassifiedCount = aiClassifiedCount,
            keywordFallbackCount = keywordFallbackCount,
            lowConfidenceCount = lowConfidenceItems.size,
            aiCoverageRate = if (totalIcons > 0) aiClassifiedCount.toFloat() / totalIcons else 0f
        )
    }

    /**
     * 重置收集器。
     */
    fun reset() {
        lowConfidenceItems.clear()
        aiClassifiedCount = 0
        keywordFallbackCount = 0
        totalIcons = 0
    }
}

/**
 * 单条反馈项。
 */
data class FeedbackItem(
    /** 应用标签 */
    val label: String,
    /** AI 给出的分类 */
    val aiCategory: String,
    /** AI 分类置信度 */
    val aiConfidence: Float,
    /** AI 给出的理由 */
    val reasoning: String? = null
)

/**
 * 分类统计摘要。
 */
data class FeedbackSummary(
    /** 总图标数 */
    val totalIcons: Int,
    /** AI 成功分类数 */
    val aiClassifiedCount: Int,
    /** 关键词兜底数 */
    val keywordFallbackCount: Int,
    /** 低置信度项数 */
    val lowConfidenceCount: Int,
    /** AI 覆盖率 */
    val aiCoverageRate: Float
)