package com.autoapporganizer.core.classification

/** 分类置信度阈值：低于此值的分类结果回退到关键词匹配或缓存 */
const val CLASSIFICATION_CONFIDENCE_THRESHOLD = 0.5f

/**
 * AI 驱动的分类结果，包含置信度评分和推理过程。
 *
 * 与 [CategoryMatcher] 不同，这里每个分类决策都带有置信度，
 * 低置信度的项可以通过 [ClassificationFusion] 回退到关键词匹配。
 */
data class ClassificationResult(
    /** 分类名称（如 "社交"、"视频"） */
    val category: String,
    /** 属于该分类的应用列表 */
    val apps: List<ClassifiedApp>,
    /** 是否需要创建新分类（AI 判断现有分类不够用） */
    val isNewCategory: Boolean = false
)

/**
 * 单个应用的分类信息。
 */
data class ClassifiedApp(
    /** 应用标签（来自 VLM 或无障碍节点树） */
    val label: String,
    /** 分配的类别 */
    val category: String,
    /** 分类置信度 0..1，AI 分类通常 > 0.7，低置信度触发关键词兜底 */
    val confidence: Float,
    /** AI 给出的分类理由（可空，用于日志和调试） */
    val reasoning: String? = null
)

/**
 * 批量分类的顶层响应模型。
 */
data class ClassificationResponse(
    /** 所有分类结果 */
    val categories: List<ClassificationResult>,
    /** AI 认为不确定的项（低置信度），需要回退到关键词匹配 */
    val uncertain: List<ClassifiedApp> = emptyList(),
    /** AI 给出的整体思考（可选，用于日志） */
    val thought: String? = null
)