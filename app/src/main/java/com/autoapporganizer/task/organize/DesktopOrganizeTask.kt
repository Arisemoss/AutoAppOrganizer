package com.autoapporganizer.task.organize

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.agent.AgentTask
import com.autoapporganizer.core.agent.TaskState
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
    private val prefs: PrefsManager
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

                    // The folder should now exist near the second icon. Use the second icon's
                    // original bounds as the initial folder location; reasonDrag will re-locate
                    // it before each subsequent drop.
                    newContext[PHASE] = "drag"
                    newContext[CATEGORY] = cat
                    newContext[DRAG_INDEX] = 2
                    newContext[FOLDER_BOUNDS] = elements[1].bounds
                    foldersCreated++
                    newItems += 2 // anchor + second are now inside the folder
                    DiagnosticLogger.info(TAG, "observe: folder created for '$cat'")
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

    /**
     * Categorize screen elements using [CategoryMatcher].
     *
     * Both the visible label and the system-provided package name are used: the package
     * name is the strongest evidence (e.g. `com.tencent.mm` unambiguously identifies
     * WeChat even when the label is missing, renamed or OCR-garbled).
     */
    private fun categorize(elements: List<ScreenElement>): Map<String, List<ScreenElement>> {
        return elements.groupBy { categoryMatcher.matchCategory(it.label, it.packageName) }
    }

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
