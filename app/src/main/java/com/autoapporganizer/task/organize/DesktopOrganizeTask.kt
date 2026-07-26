package com.autoapporganizer.task.organize

import android.content.Context
import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.agent.AgentTask
import com.autoapporganizer.core.agent.TaskState
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.PerceptionFusion
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.core.plan.VisionPlanner
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
 * When a VLM vision result is available, [VisionPlanner] is used to parse the model's
 * action plan. When not available, the task falls back to accessibility-based heuristic
 * logic.
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
    private val context: Context,
    private val prefs: PrefsManager
) : AgentTask {

    companion object {
        private const val TAG = "DesktopOrganizeTask"

        // Context keys
        private const val PHASE = "phase"
        private const val CATEGORY = "category"
        private const val DRAG_INDEX = "dragIndex"
        private const val FOLDER_BOUNDS = "folderBounds"
        private const val RETRY_COUNT = "retryCount"

        /** Minimum and maximum size for folder candidate detection. */
        private const val FOLDER_MIN_SIZE_PX = 80
        private const val FOLDER_MAX_SIZE_PX = 400

        /** Maximum consecutive failures before aborting a category. */
        private const val MAX_CONSECUTIVE_FAILURES = 3

        /** Maximum total categories to skip before giving up entirely. */
        private const val MAX_SKIPPED_CATEGORIES = 5
    }

    override val name: String = "桌面图标视觉整理"
    override val maxSteps: Int = 80

    private var foldersCreated = 0
    private val categoryMatcher = CategoryMatcher(context)

    /** Categorized icons: category name → list of elements belonging to it. */
    private var categorized: Map<String, List<ScreenElement>> = emptyMap()

    /** Ordered list of categories that have enough members to form a folder. */
    private var categoryQueue: MutableList<String> = mutableListOf()

    /** Count of categories that were skipped due to failures. */
    private var skippedCategories = 0

    /** Screen bounds, cached for VisionPlanner coordinate normalization. */
    private val screenBounds: Rect by lazy {
        val dm = context.resources.displayMetrics
        Rect(0, 0, dm.widthPixels, dm.heightPixels)
    }

    // ──────────────────────────────────────────────
    // AgentTask implementation
    // ──────────────────────────────────────────────

    override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String {
        val perception = accessibility.scanElements()
        val visionItems = vision.detectIcons()
        DiagnosticLogger.info(TAG, "describe: a11y=${perception.size} vision=${visionItems.size}")

        // Merge accessibility and vision evidence using the dedicated fusion layer.
        val merged = PerceptionFusion.merge(visionItems, perception)
        categorized = categorize(merged)

        // Only keep categories that meet the minimum folder size.
        val minSize = prefs.minFolderSize.coerceAtLeast(2)
        categoryQueue = categorized
            .filter { it.value.size >= minSize }
            .keys
            .toMutableList()

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
        val consecutiveFailures = (state.context[RETRY_COUNT] as? Int) ?: 0

        // If we've had too many consecutive failures, abort the current category.
        if (consecutiveFailures >= MAX_CONSECUTIVE_FAILURES) {
            skippedCategories++
            DiagnosticLogger.warn(
                TAG,
                "Too many consecutive failures ($consecutiveFailures), " +
                    "skipping category (total skipped: $skippedCategories/$MAX_SKIPPED_CATEGORIES)"
            )
            if (skippedCategories >= MAX_SKIPPED_CATEGORIES) {
                DiagnosticLogger.error(TAG, "Too many skipped categories, giving up")
                return Action.Complete
            }
            return reasonNext()
        }

        // 1) Try to use VLM vision plan if available and valid
        if (visionResult is VisionResult.Success && visionResult.rawResponse.isNotBlank()) {
            val plan = VisionPlanner.parse(visionResult.rawResponse, screenBounds)
            if (plan.actions.isNotEmpty()) {
                val action = plan.actions.first()
                DiagnosticLogger.info(
                    TAG,
                    "reason(vision): phase=$phase thought='${plan.thought.take(100)}' action=${action.describe()}"
                )
                // Validate: if VLM says "complete" but we still have categories, ignore it
                if (action is Action.Complete && categoryQueue.isNotEmpty()) {
                    DiagnosticLogger.warn(
                        TAG,
                        "VLM says complete but queue not empty (${categoryQueue.size} categories left), overriding"
                    )
                    // fall through to heuristic logic
                } else {
                    return action
                }
            }
        }

        // 2) Fallback: heuristic accessibility-based logic
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
        val anchor = elements[0]
        val second = elements[1]

        DiagnosticLogger.info(
            TAG,
            "reason: creating folder for '$cat' by dragging ${anchor.label} onto ${second.label}"
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
            DiagnosticLogger.info(TAG, "reason: category '$cat' drag complete (${elements.size} icons)")
            return Action.Wait(300)
        }

        // Re-locate the folder on every drag: launchers often re-grid icons after each drop,
        // so the folder coordinate cached at creation time may be stale.
        // Try both perception-based and hint-based folder location.
        val folderBounds = locateFolder(perception, state)
        if (folderBounds == null) {
            DiagnosticLogger.warn(TAG, "reason: cannot locate folder for '$cat', aborting category")
            return reasonNext()
        }

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
        val consecutiveFailures = (state.context[RETRY_COUNT] as? Int) ?: 0
        val newErrors = if (!result) {
            state.errors + "Action ${action.describe()} failed at step ${state.step}"
        } else {
            state.errors
        }

        val newContext = state.context.toMutableMap()
        var newItems = state.itemsOrganized
        var retryHint = false

        when (phase) {
            "scan" -> {
                if (result) {
                    val cat = categoryQueue.firstOrNull() ?: return markDone(state, newErrors)
                    val elements = categorized[cat].orEmpty()
                    if (elements.size < 2) {
                        categoryQueue.removeAt(0)
                        return state.copy(step = state.step + 1, errors = newErrors)
                    }

                    newContext[PHASE] = "drag"
                    newContext[CATEGORY] = cat
                    newContext[DRAG_INDEX] = 2
                    newContext[FOLDER_BOUNDS] = elements[1].bounds
                    newContext[RETRY_COUNT] = 0
                    foldersCreated++
                    newItems += 2 // anchor + second are now inside the folder
                    DiagnosticLogger.info(TAG, "observe: folder created for '$cat' (total: $foldersCreated)")
                } else {
                    val newRetry = consecutiveFailures + 1
                    newContext[RETRY_COUNT] = newRetry
                    // Folder creation failures are often transient (gesture cancelled by launcher
                    // animation, etc.), so hint the runner to retry.
                    retryHint = newRetry < MAX_CONSECUTIVE_FAILURES
                    DiagnosticLogger.warn(
                        TAG,
                        "observe: folder creation failed (retry $newRetry/$MAX_CONSECUTIVE_FAILURES, retryHint=$retryHint)"
                    )
                }
            }

            "drag" -> {
                if (result) {
                    val cat = state.context[CATEGORY] as? String ?: return markDone(state, newErrors)
                    val elements = categorized[cat].orEmpty()
                    val currentDragIndex = (state.context[DRAG_INDEX] as? Int) ?: 2
                    val nextDragIndex = currentDragIndex + 1
                    newContext[DRAG_INDEX] = nextDragIndex
                    newContext[RETRY_COUNT] = 0 // reset failure count on success
                    newItems++

                    if (nextDragIndex >= elements.size) {
                        // All icons for this category have been moved into the folder.
                        categoryQueue.remove(cat)
                        newContext[PHASE] = "next"
                        newContext.remove(CATEGORY)
                        newContext.remove(DRAG_INDEX)
                        newContext.remove(FOLDER_BOUNDS)
                        newContext.remove(RETRY_COUNT)
                        DiagnosticLogger.info(
                            TAG,
                            "observe: category '$cat' complete (${elements.size} icons, $foldersCreated folders total)"
                        )
                    }
                } else {
                    val newRetry = consecutiveFailures + 1
                    newContext[RETRY_COUNT] = newRetry
                    // Drag failures are also often transient (launcher re-grid, gesture conflict).
                    retryHint = newRetry < MAX_CONSECUTIVE_FAILURES
                    DiagnosticLogger.warn(
                        TAG,
                        "observe: drag failed (retry $newRetry/$MAX_CONSECUTIVE_FAILURES, retryHint=$retryHint)"
                    )
                }
            }

            "next" -> {
                if (categoryQueue.isNotEmpty()) {
                    newContext[PHASE] = "scan"
                    newContext[RETRY_COUNT] = 0
                } else {
                    newContext[PHASE] = "done"
                }
            }
        }

        return state.copy(
            step = state.step + 1,
            itemsOrganized = newItems,
            errors = newErrors,
            context = newContext,
            retryHint = retryHint
        )
    }

    override fun isComplete(state: TaskState): Boolean {
        val phase = state.context[PHASE] as? String
        return phase == "done" || categoryQueue.isEmpty() || state.errors.size >= 10
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

    /** Categorize screen elements using [CategoryMatcher]. */
    private fun categorize(elements: List<ScreenElement>): Map<String, List<ScreenElement>> {
        return elements.groupBy { categoryMatcher.matchCategory(it.label) }
    }

    /**
     * Locate the folder created in the current category.
     *
     * Strategy (in order of priority):
     * 1. Search the latest perception for a folder node near the original hint.
     * 2. Search for any element whose label contains "folder" or "文件夹".
     * 3. Fall back to the cached hint from "scan".
     * 4. If none exists, return null to abort the category.
     */
    private fun locateFolder(perception: List<ScreenElement>, state: TaskState): Rect? {
        val hint = state.context[FOLDER_BOUNDS] as? Rect

        // Strategy 1: Find a folder-sized element near the hint.
        val folderCandidates = perception.filter {
            it.bounds.width() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX &&
            it.bounds.height() in FOLDER_MIN_SIZE_PX..FOLDER_MAX_SIZE_PX
        }

        val nearestFolder = folderCandidates.minByOrNull { elem ->
            val hintRect = hint ?: return@minByOrNull Int.MAX_VALUE
            val dx = elem.bounds.exactCenterX() - hintRect.exactCenterX()
            val dy = elem.bounds.exactCenterY() - hintRect.exactCenterY()
            (dx * dx + dy * dy).toInt()
        }?.bounds

        if (nearestFolder != null) {
            if (hint != null && nearestFolder != hint) {
                DiagnosticLogger.debug(
                    TAG,
                    "locateFolder: folder moved from $hint to $nearestFolder"
                )
            }
            return nearestFolder
        }

        // Strategy 2: Search for folder-labeled elements.
        val labeledFolder = perception.firstOrNull {
            it.label.contains("folder", ignoreCase = true) ||
            it.label.contains("文件夹")
        }?.bounds

        if (labeledFolder != null) {
            DiagnosticLogger.debug(TAG, "locateFolder: found by label at $labeledFolder")
            return labeledFolder
        }

        // Strategy 3: Fall back to hint.
        if (hint != null) {
            DiagnosticLogger.debug(TAG, "locateFolder: using cached hint $hint")
            return hint
        }

        DiagnosticLogger.warn(TAG, "locateFolder: no folder found and no hint available")
        return null
    }

    private fun markDone(state: TaskState, errors: List<String>): TaskState {
        return state.copy(
            step = state.step + 1,
            errors = errors,
            context = state.context.toMutableMap().apply { put(PHASE, "done") }
        )
    }
}