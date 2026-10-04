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
        private const val DRAG_QUEUE = "dragQueue"
        private const val FOLDER_BOUNDS = "folderBounds"

        /** Folder candidates must fall inside this size window (px). */
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

    /** Icons discovered during [describe] (for history records). */
    private var iconCount = 0

    /** Category → member count snapshot from [describe] (for history records). */
    private var categorySummary: Map<String, Int> = emptyMap()

    // Anchor pair + remaining drag order for the category currently being organized.
    // reason() computes them, observe() consumes them (reason cannot write state).
    private var currentAnchorIdx = -1
    private var currentSecondIdx = -1
    private var currentDragQueue: List<Int> = emptyList()

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
        iconCount = merged.size
        categorySummary = categorized.mapValues { it.value.size }

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
        perception: List<ScreenElement>
    ): Action {
        val phase = state.context[PHASE] as? String ?: "scan"

        return when (phase) {
            "scan" -> reasonScan(state, perception)
            "drag" -> reasonDrag(state, perception)
            "next" -> reasonNext()
            "done" -> Action.Complete
            else -> Action.Complete
        }
    }

    private fun reasonScan(state: TaskState, perception: List<ScreenElement>): Action {
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
        currentAnchorIdx = anchorIdx
        currentSecondIdx = secondIdx
        // The anchors become the folder; everything else must be dragged into it in
        // this order. Never assume the pair is (0, 1) — findAnchorPair is centroid-based.
        currentDragQueue = elements.indices.filter { it != anchorIdx && it != secondIdx }

        // Launchers re-grid icons after every drop, so coordinates captured during
        // describe() are stale for every category after the first. Re-locate both
        // endpoints in the fresh perception; fall back to cached bounds on no match.
        val anchor = relocate(elements[anchorIdx], perception)
        val second = relocate(elements[secondIdx], perception)

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
        val queue = state.context[DRAG_QUEUE] as? List<Int> ?: emptyList()

        val nextIdx = queue.firstOrNull()
        if (nextIdx == null) {
            DiagnosticLogger.info(TAG, "reason: category '$cat' drag complete")
            return Action.Wait(300)
        }

        val elements = categorized[cat].orEmpty()
        if (nextIdx >= elements.size) {
            // Defensive: the category shrank between describe() and now.
            DiagnosticLogger.warn(TAG, "reason: drag index $nextIdx out of bounds for '$cat'")
            return Action.Wait(300)
        }

        // Re-locate the folder on every drag: launchers often re-grid icons after each drop,
        // so the folder coordinate cached at creation time may be stale.
        val folderBounds = locateFolder(perception, state)
            ?: return Action.Complete

        val target = relocate(elements[nextIdx], perception)
        DiagnosticLogger.info(
            TAG,
            "reason: dragging ${target.label}[$nextIdx] into '$cat' folder at $folderBounds"
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

                    // The folder should now exist near the second anchor icon. Use the
                    // second icon's original bounds as the initial folder location;
                    // reasonDrag will re-locate it before each subsequent drop.
                    val secondIdx = currentSecondIdx.coerceIn(0, elements.size - 1)
                    val secondElement = elements[secondIdx]

                    newContext[PHASE] = "drag"
                    newContext[CATEGORY] = cat
                    newContext[DRAG_QUEUE] = currentDragQueue
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
                    val queue = (state.context[DRAG_QUEUE] as? List<Int>).orEmpty()
                    val remaining = queue.drop(1)
                    newContext[DRAG_QUEUE] = remaining
                    newItems++

                    if (remaining.isEmpty()) {
                        // All icons for this category have been moved into the folder.
                        categoryQueue.remove(cat)
                        newContext[PHASE] = "next"
                        newContext.remove(CATEGORY)
                        newContext.remove(DRAG_QUEUE)
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
     * Vision is consumed in [describe] (icon detection + AI classification). No VLM
     * calls happen per ReAct step: the per-step result previously had no consumer.
     */
    override fun getFoldersCreated(): Int = foldersCreated

    /** Icons discovered during [describe] (for history records). */
    fun getIconsFound(): Int = iconCount

    /** Category → member count snapshot from [describe] (for history records). */
    fun getCategorySummary(): Map<String, Int> = categorySummary

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
     * 1. Prefer a size-window element whose label names the category (launchers
     *    usually label the folder with its name — app icons share the size window,
     *    so size alone is not a reliable folder signal).
     * 2. Fall back to the size-window element nearest the original folder hint.
     * 3. Fall back to the cached hint from "scan"; abort the category if null.
     */
    private fun locateFolder(perception: List<ScreenElement>, state: TaskState): Rect? {
        val hint = state.context[FOLDER_BOUNDS] as? Rect
        val cat = state.context[CATEGORY] as? String

        val candidates = perception.filter {
            it.bounds.width() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX &&
                it.bounds.height() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX
        }

        val folder = candidates
            .firstOrNull { cat != null && it.label.contains(cat, ignoreCase = true)  }?.bounds
            ?: candidates.minByOrNull { elem ->
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

    /**
     * Re-locate [element] in the fresh perception scan.
     *
     * Launchers re-grid icons after every drop, so bounds captured during describe()
     * go stale. The per-step scan is the source of truth: match by label (exact >
     * containment) with bounds IoU as a tiebreaker, and fall back to the cached
     * element when nothing matches (icon already moved, label merged by fusion).
     */
    private fun relocate(element: ScreenElement, perception: List<ScreenElement>): ScreenElement {
        var best = element
        var bestScore = 0f
        for (p in perception) {
            val labelScore = when {
                p.label.equals(element.label, ignoreCase = true) -> 2f
                element.label.contains(p.label, ignoreCase = true) ||
                    p.label.contains(element.label, ignoreCase = true) -> 1f
                else -> 0f
            }
            if (labelScore == 0f) continue
            val score = labelScore + boundsIoU(element.bounds, p.bounds)
            if (score > bestScore) {
                bestScore = score
                best = p
            }
        }
        return best
    }

    /** Intersection-over-union of two rects (0..1). */
    private fun boundsIoU(a: Rect, b: Rect): Float {
        val left = maxOf(a.left, b.left)
        val top = maxOf(a.top, b.top)
        val right = minOf(a.right, b.right)
        val bottom = minOf(a.bottom, b.bottom)
        if (right <= left || bottom <= top) return 0f
        val inter = (right - left) * (bottom - top).toFloat()
        val union = a.width() * a.height() + b.width() * b.height() - inter
        return if (union > 0f) inter / union else 0f
    }

    private fun markDone(state: TaskState, errors: List<String>): TaskState {
        return state.copy(
            step = state.step + 1,
            errors = errors,
            context = state.context.toMutableMap().apply { put(PHASE, "done") }
        )
    }
}
