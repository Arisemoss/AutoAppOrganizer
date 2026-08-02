package com.autoapporganizer.task.organize

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.agent.AgentTask
import com.autoapporganizer.core.agent.TaskState
import com.autoapporganizer.core.classification.ClassificationFusion
import com.autoapporganizer.core.classification.ClassificationResponse
import com.autoapporganizer.core.classification.CLASSIFICATION_CONFIDENCE_THRESHOLD
import com.autoapporganizer.core.classification.SemanticClassifier
import com.autoapporganizer.core.feedback.ClassificationCache
import com.autoapporganizer.core.feedback.FeedbackCollector
import com.autoapporganizer.core.layout.DragOptimizer
import com.autoapporganizer.core.layout.SpatialClusterer
import com.autoapporganizer.core.model.VisionModelService
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.PerceptionFusion
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager

/**
 * Vision-driven desktop organization task.
 *
 * Uses the hybrid perception stack (accessibility + optional VLM) to locate app icons,
 * categorize them via [CategoryMatcher], and issue drag gestures to group same-category
 * icons into folders on the home screen.
 *
 * State machine phases (tracked in [TaskState.context]["phase"]):
 *  - "scan"  — pick the next category and create a folder by dragging the anchor icon
 *              onto the second icon of the same category.
 *  - "drag"  — drag remaining category members into the folder created in "scan".
 *  - "next"  — category finished; advance to the next category or complete.
 *  - "done"  — all categories processed.
 */
class DesktopOrganizeTask(
    private val perceptionChannel: AccessibilityChannel,
    private val visionChannel: VisionChannel,
    private val service: AccessibilityService,
    private val prefs: PrefsManager,
    private val vlmService: VisionModelService? = null
) : AgentTask {

    companion object {
        private const val TAG = "DesktopOrganizeTask"

        private const val PHASE = "phase"
        private const val CATEGORY = "category"
        private const val DRAG_INDEX = "dragIndex"
        private const val FOLDER_BOUNDS = "folderBounds"

        /** Minimum overlap ratio to consider a perception element a folder candidate. */
        private const val FOLDER_MIN_SIZE_PX = 80
        private const val FOLDER_MAX_SIZE_PX = 400
    }

    override val name: String = "桌面图标视觉整理"
    override val maxSteps: Int = 60

    private var foldersCreated = 0
    private val categoryMatcher = CategoryMatcher(service)

    /** AI 语义分类器（VLM 驱动），仅当 VLM 可用时初始化 */
    private val semanticClassifier: SemanticClassifier? =
        if (vlmService != null && vlmService.isAvailable) {
            SemanticClassifier(vlmService, categoryMatcher)
        } else {
            null
        }

    /** 最近一次 AI 分类响应（用于日志和低置信度展示） */
    private var lastClassificationResponse: ClassificationResponse? = null

    /** 反馈收集器（低置信度项追踪） */
    private val feedbackCollector = FeedbackCollector()

    /** 分类缓存（持久化 AI 分类结果，加速后续整理） */
    private val classificationCache = ClassificationCache(service)

    /** Categorized icons: category name → list of elements belonging to it. */
    private var categorized: Map<String, List<ScreenElement>> = emptyMap()

    /** Ordered list of categories that have enough members to form a folder. */
    private var categoryQueue: MutableList<String> = mutableListOf()

    // ──────────────────────────────────────────────
    // AgentTask implementation
    // ──────────────────────────────────────────────

    override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String {
        val perception = accessibility.scanElements()
        val visionItems = vision.detectIcons()
        DiagnosticLogger.info(TAG, "describe: a11y=${perception.size} vision=${visionItems.size}")

        // Merge accessibility and vision evidence using the dedicated fusion layer.
        val merged = PerceptionFusion.merge(visionItems, perception)

        // 使用 AI 语义分类 + 关键词兜底（参考 Operit autoCategorizeMemories）
        categorized = categorizeWithAI(merged)

        // Only keep categories that meet the minimum folder size.
        val minSize = prefs.minFolderSize.coerceAtLeast(2)
        val eligible = categorized.filter { it.value.size >= minSize }

        // 空间优化：按图标数量和空间集中度排序分类优先级（参考 Operit 的知识图谱批量处理）
        categoryQueue = DragOptimizer.prioritizeCategories(eligible).toMutableList()

        DiagnosticLogger.info(
            TAG,
            "describe: ${merged.size} icons, ${categorized.size} categories, " +
                "${categoryQueue.size} worth organizing (minFolderSize=$minSize)"
        )

        return "发现 ${merged.size} 个图标，分为 ${categorized.size} 类，其中 ${categoryQueue.size} 类可整理"
    }

    override suspend fun reason(
        state: TaskState,
        perception: List<ScreenElement>,
        visionResult: VisionResult?
    ): Action {
        val phase = state.context[PHASE] as? String ?: "scan"

        return when (phase) {
            "scan" -> reasonScan(state)
            "drag" -> reasonDrag(state, perception)
            "next" -> reasonNext()
            "done" -> Action.Complete
            else -> Action.Complete
        }
    }

    private fun reasonScan(state: TaskState): Action {
        // Drop empty or too-small categories silently.
        while (categoryQueue.isNotEmpty()) {
            val cat = categoryQueue.first()
            val elements = categorized[cat].orEmpty()
            if (elements.size >= 2) break
            DiagnosticLogger.warn(TAG, "reason: category '$cat' has ${elements.size} elements, skipping")
            categoryQueue.removeAt(0)
        }

        if (categoryQueue.isEmpty()) {
            DiagnosticLogger.info(TAG, "reason: no categories left to organize")
            return Action.Complete
        }

        val cat = categoryQueue.first()
        val elements = categorized[cat].orEmpty()

        // 空间优化：选择距离质心最近的图标对作为锚点，减少拖拽距离
        val (anchorIdx, secondIdx) = SpatialClusterer.findAnchorPair(elements)
        val anchor = elements[anchorIdx]
        val second = elements[secondIdx]

        DiagnosticLogger.info(
            TAG,
            "reason: creating folder for '$cat' by dragging ${anchor.label} onto ${second.label} (spatial optimized)"
        )
        return Action.Drag(
            anchor.centerX, anchor.centerY,
            second.centerX, second.centerY,
            durationMs = 800L
        )
    }

    private fun reasonDrag(state: TaskState, perception: List<ScreenElement>): Action {
        val cat = state.context[CATEGORY] as? String ?: return Action.Complete
        val elements = categorized[cat].orEmpty()
        val dragIndex = (state.context[DRAG_INDEX] as? Int) ?: 2

        if (dragIndex >= elements.size) {
            DiagnosticLogger.info(TAG, "reason: category '$cat' drag complete")
            return Action.Wait(300)
        }

        // Re-locate the folder on every drag: launchers often re-grid icons after each drop,
        // so the folder coordinate cached at creation time may be stale.
        val folderBounds = locateFolder(perception, state)
            ?: return Action.Complete

        val target = elements[dragIndex]
        DiagnosticLogger.info(
            TAG,
            "reason: dragging ${target.label}[$dragIndex] into '$cat' folder at $folderBounds"
        )
        return Action.Drag(
            target.centerX, target.centerY,
            folderBounds.exactCenterX(), folderBounds.exactCenterY(),
            durationMs = 800L
        )
    }

    private fun reasonNext(): Action {
        return if (categoryQueue.isEmpty()) {
            DiagnosticLogger.info(TAG, "reason: all categories done")
            Action.Complete
        } else {
            DiagnosticLogger.info(TAG, "reason: moving to next category '${categoryQueue.first()}'")
            Action.Wait(400)
        }
    }

    override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
        val phase = state.context[PHASE] as? String ?: "scan"
        val errors = if (!result) {
            state.errors + "Action ${action.describe()} failed at step ${state.step}"
        } else {
            state.errors
        }

        val newContext = state.context.toMutableMap()
        var newItems = state.itemsOrganized

        when (phase) {
            "scan" -> {
                if (result) {
                    val cat = categoryQueue.firstOrNull() ?: return markDone(state, errors)
                    val elements = categorized[cat].orEmpty()
                    if (elements.size < 2) {
                        categoryQueue.removeAt(0)
                        return state.copy(step = state.step + 1, errors = errors)
                    }

                    // 使用空间优化后的锚点对：文件夹创建在 second 图标的位置
                    val (_, secondIdx) = SpatialClusterer.findAnchorPair(elements)
                    val secondElement = elements[secondIdx]

                    // The folder should now exist near the second icon. Use the second icon's
                    // original bounds as the initial folder location; reasonDrag will re-locate
                    // it before each subsequent drop.
                    newContext[PHASE] = "drag"
                    newContext[CATEGORY] = cat
                    newContext[DRAG_INDEX] = 2
                    newContext[FOLDER_BOUNDS] = secondElement.bounds
                    foldersCreated++
                    newItems += 2 // anchor + second are now inside the folder
                    DiagnosticLogger.info(TAG, "observe: folder created for '$cat' at ${secondElement.bounds}")
                } else {
                    DiagnosticLogger.warn(TAG, "observe: folder creation failed, will retry")
                }
            }

            "drag" -> {
                if (result) {
                    val cat = state.context[CATEGORY] as? String ?: return markDone(state, errors)
                    val elements = categorized[cat].orEmpty()
                    val currentDragIndex = (state.context[DRAG_INDEX] as? Int) ?: 2
                    val nextDragIndex = currentDragIndex + 1
                    newContext[DRAG_INDEX] = nextDragIndex
                    newItems++

                    if (nextDragIndex >= elements.size) {
                        // All icons for this category have been moved into the folder.
                        categoryQueue.remove(cat)
                        newContext[PHASE] = "next"
                        newContext.remove(CATEGORY)
                        newContext.remove(DRAG_INDEX)
                        newContext.remove(FOLDER_BOUNDS)
                        DiagnosticLogger.info(TAG, "observe: category '$cat' complete ($foldersCreated folders)")
                    }
                } else {
                    DiagnosticLogger.warn(TAG, "observe: drag failed, will retry")
                }
            }

            "next" -> {
                if (categoryQueue.isNotEmpty()) {
                    newContext[PHASE] = "scan"
                } else {
                    newContext[PHASE] = "done"
                }
            }
        }

        return state.copy(
            step = state.step + 1,
            itemsOrganized = newItems,
            errors = errors,
            context = newContext
        )
    }

    override fun isComplete(state: TaskState): Boolean {
        val phase = state.context[PHASE] as? String
        return phase == "done" || categoryQueue.isEmpty() || state.errors.size >= 5
    }

    /**
     * Vision is useful when accessibility alone may miss icons or folders:
     *  - "scan" : initial icon discovery
     *  - "drag" : folder re-location after each drop
     */
    override fun needsVision(state: TaskState): Boolean {
        val phase = state.context[PHASE] as? String ?: "scan"
        return phase == "scan" || phase == "drag"
    }

    override fun getFoldersCreated(): Int = foldersCreated

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    /** 使用 AI 语义分类 + 缓存先验 + 关键词兜底进行图标分类（参考 Operit 的多路信号融合） */
    private suspend fun categorizeWithAI(elements: List<ScreenElement>): Map<String, List<ScreenElement>> {
        // 1. 构建缓存映射 (label → category)，作为兜底分类的先验知识
        val cachedMap = mutableMapOf<String, String>()
        for (el in elements) {
            val cached = classificationCache.lookup(el.label)
            if (cached != null) {
                cachedMap[el.label] = cached
                DiagnosticLogger.debug(TAG, "Cache hit: '${el.label}' → $cached")
            }
        }

        // 2. 尝试 AI 语义分类
        val aiResponse = semanticClassifier?.classify(elements)
        lastClassificationResponse = aiResponse

        if (aiResponse != null) {
            DiagnosticLogger.info(
                TAG,
                "AI classification: ${aiResponse.categories.size} categories, " +
                    "${aiResponse.uncertain.size} uncertain - ${aiResponse.thought ?: ""}"
            )

            // 收集反馈：低置信度项
            feedbackCollector.collect(aiResponse, elements.size)

            // 缓存高置信度结果
            val allApps = aiResponse.categories.flatMap { it.apps }
            classificationCache.cacheBatch(allApps)

            // 记录低置信度项
            val lowConfidence = allApps
                .filter { it.confidence < CLASSIFICATION_CONFIDENCE_THRESHOLD }
            if (lowConfidence.isNotEmpty()) {
                DiagnosticLogger.warn(
                    TAG,
                    "Low confidence classifications: ${lowConfidence.joinToString { "${it.label}→${it.category}(${it.confidence})" }}"
                )
            }
        } else {
            // AI 不可用时，记录为纯关键词兜底
            feedbackCollector.collect(
                ClassificationResponse(emptyList(), emptyList(), "VLM unavailable"),
                elements.size
            )
        }

        // 3. 融合 AI 分类、缓存和关键词分类
        return ClassificationFusion.fuse(aiResponse, elements, categoryMatcher, cachedMap)
    }

    /** 获取最近一次分类响应（用于 UI 展示低置信度项） */
    fun getLastClassificationResponse(): ClassificationResponse? = lastClassificationResponse

    /** 获取反馈收集器（用于 UI 展示统计信息） */
    fun getFeedbackCollector(): FeedbackCollector = feedbackCollector

    /** 获取分类缓存（用于查看缓存状态） */
    fun getClassificationCache(): ClassificationCache = classificationCache

    /**
     * Locate the folder created in the current category.
     *
     * 1. Search the latest perception for a large element near the original folder hint.
     * 2. Fall back to the cached hint from "scan".
     * 3. If neither exists, abort the category.
     */
    private fun locateFolder(perception: List<ScreenElement>, state: TaskState): Rect? {
        val hint = state.context[FOLDER_BOUNDS] as? Rect
        val cat = state.context[CATEGORY] as? String

        // Try finding a folder node in the current perception.
        val folder = perception
            .filter { it.bounds.width() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX && it.bounds.height() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX }
            .minByOrNull { elem ->
                val hintRect = hint ?: return@minByOrNull Int.MAX_VALUE
                val dx = elem.bounds.exactCenterX() - hintRect.exactCenterX()
                val dy = elem.bounds.exactCenterY() - hintRect.exactCenterY()
                (dx * dx + dy * dy).toInt()
            }
            ?.bounds

        if (folder != null && hint != null && folder != hint) {
            DiagnosticLogger.debug(TAG, "locateFolder: folder moved from $hint to $folder")
        }

        return folder ?: hint
    }

    private fun markDone(state: TaskState, errors: List<String>): TaskState {
        return state.copy(
            step = state.step + 1,
            errors = errors,
            context = state.context.toMutableMap().apply { put(PHASE, "done") }
        )
    }
}
