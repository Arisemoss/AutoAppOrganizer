package com.autoapporganizer.task.organize

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.agent.AgentTask
import com.autoapporganizer.core.agent.TaskState
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager

/**
 * Vision-driven desktop organization task.
 *
 * Uses the hybrid perception stack (accessibility + optional VLM) to locate app icons,
 * categorize them via [CategoryMatcher], and issue long-press + drag gestures to group
 * same-category icons into folders on the home screen.
 *
 * State machine phases (tracked in [TaskState.context]["phase"]):
 *  - "scan"     — initial desktop scan + categorization
 *  - "press"    — long-press the anchor icon to trigger folder creation
 *  - "drag"     — drag remaining category members into the new folder
 *  - "next"     — move to the next category
 *  - "done"     — all categories processed
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
        private const val ANCHOR_X = "anchorX"
        private const val ANCHOR_Y = "anchorY"
        private const val FOLDER_X = "folderX"
        private const val FOLDER_Y = "folderY"
    }

    override val name: String = "桌面图标视觉整理"
    override val maxSteps: Int = 30

    private var foldersCreated = 0
    private val categoryMatcher = CategoryMatcher(service)

    /** Categorized icons: category name → list of elements belonging to it. */
    private var categorized: Map<String, List<ScreenElement>> = emptyMap()

    /** Ordered list of categories that have enough members to form a folder. */
    private var categoryQueue: MutableList<String> = mutableListOf()

    /** Bounds of the folder currently being filled (updated after each long-press). */
    private var currentFolderBounds: Rect? = null

    // ──────────────────────────────────────────────
    // AgentTask implementation
    // ──────────────────────────────────────────────

    override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String {
        val perception = accessibility.scan()
        val visionItems = vision.scan()
        DiagnosticLogger.info(TAG, "describe: a11y=${perception.size} vision=${visionItems.size}")

        // Merge: prefer accessibility elements, supplement with vision-only ones.
        val merged = mergePerception(perception, visionItems)
        categorized = categorize(merged)

        // Only keep categories that meet the minimum folder size.
        val minSize = prefs.minFolderSize.coerceAtLeast(2)
        categoryQueue = categorized
            .filter { it.value.size >= minSize }
            .keys
            .toMutableList()

        DiagnosticLogger.info(TAG, "describe: ${merged.size} icons, ${categorized.size} categories, ${categoryQueue.size} worth organizing (minFolderSize=$minSize)")

        return "发现 ${merged.size} 个图标，分为 ${categorized.size} 类，其中 ${categoryQueue.size} 类可整理"
    }

    override suspend fun reason(
        state: TaskState,
        perception: List<ScreenElement>,
        visionResult: VisionResult?
    ): Action {
        val phase = state.context[PHASE] as? String ?: "scan"

        return when (phase) {
            "scan" -> {
                // Move to the first category and start pressing.
                if (categoryQueue.isEmpty()) {
                    DiagnosticLogger.info(TAG, "reason: no categories to organize")
                    return Action.Complete
                }
                val cat = categoryQueue.first()
                val elements = categorized[cat].orEmpty()
                if (elements.isEmpty()) {
                    DiagnosticLogger.warn(TAG, "reason: category '$cat' has no elements, skipping")
                    categoryQueue.removeAt(0)
                    return Action.Wait(100)
                }
                val anchor = elements.first()
                DiagnosticLogger.info(TAG, "reason: pressing anchor for '$cat' at (${anchor.centerX}, ${anchor.centerY})")
                Action.LongPress(anchor.centerX, anchor.centerY, 600L)
            }

            "press" -> {
                // Folder should have been created; now we need to locate it.
                // Use the second icon's position as a hint, then scan for nearby folder.
                val cat = state.context[CATEGORY] as? String ?: categoryQueue.firstOrNull() ?: return Action.Complete
                val elements = categorized[cat].orEmpty()
                if (elements.size < 2) {
                    // Only one icon — nothing to drag in; move to next category.
                    DiagnosticLogger.info(TAG, "reason: category '$cat' has only 1 element, skipping")
                    categoryQueue.remove(cat)
                    foldersCreated++
                    return Action.Wait(300)
                }
                val second = elements[1]
                // Try to find a folder node near the second icon.
                val folderBounds = findFolderNear(perception, second.bounds)
                currentFolderBounds = folderBounds ?: second.bounds
                DiagnosticLogger.info(TAG, "reason: folder bounds for '$cat' = $currentFolderBounds, starting drag")
                Action.Drag(second.centerX, second.centerY, currentFolderBounds!!.exactCenterX(), currentFolderBounds!!.exactCenterY(), 800L)
            }

            "drag" -> {
                val cat = state.context[CATEGORY] as? String ?: return Action.Complete
                val elements = categorized[cat].orEmpty()
                val dragIndex = (state.context[DRAG_INDEX] as? Int) ?: 2 // start from 3rd element (0=anchor, 1=already dragged)

                if (dragIndex >= elements.size) {
                    // All elements in this category have been dragged.
                    DiagnosticLogger.info(TAG, "reason: category '$cat' complete ($foldersCreated folders)")
                    foldersCreated++
                    categoryQueue.remove(cat)
                    return Action.Wait(500)
                }

                val target = elements[dragIndex]
                val folder = currentFolderBounds ?: return Action.Complete
                DiagnosticLogger.info(TAG, "reason: dragging element[$dragIndex] of '$cat' to folder")
                Action.Drag(target.centerX, target.centerY, folder.exactCenterX(), folder.exactCenterY(), 800L)
            }

            "next" -> {
                if (categoryQueue.isEmpty()) {
                    DiagnosticLogger.info(TAG, "reason: all categories done")
                    return Action.Complete
                }
                DiagnosticLogger.info(TAG, "reason: moving to next category '${categoryQueue.first()}'")
                Action.Wait(500)
            }

            else -> Action.Complete
        }
    }

    override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
        val phase = state.context[PHASE] as? String ?: "scan"
        val errors = if (!result) state.errors + "Action ${action.describe()} failed at step ${state.step}" else state.errors

        val newContext = state.context.toMutableMap()
        var newFolders = state.foldersCreated
        var newItems = state.itemsOrganized

        when (phase) {
            "scan" -> {
                if (result) {
                    newContext[PHASE] = "press"
                    val cat = categoryQueue.firstOrNull()
                    if (cat != null) {
                        newContext[CATEGORY] = cat
                        val anchor = categorized[cat]?.firstOrNull()
                        if (anchor != null) {
                            newContext[ANCHOR_X] = anchor.centerX
                            newContext[ANCHOR_Y] = anchor.centerY
                        }
                    }
                }
            }

            "press" -> {
                if (result) {
                    newContext[PHASE] = "drag"
                    newContext[DRAG_INDEX] = 2 // next element to drag (0=anchor, 1=just dragged)
                    newItems++
                } else {
                    DiagnosticLogger.warn(TAG, "observe: press failed, retrying")
                }
            }

            "drag" -> {
                if (result) {
                    val cat = state.context[CATEGORY] as? String
                    val elements = categorized[cat].orEmpty()
                    val currentDragIndex = (state.context[DRAG_INDEX] as? Int) ?: 2
                    // IMPORTANT: use dragIndex-2 to account for anchor (index 0) and
                    // the first dragged item (index 1) in 0-based counting.
                    val adjustedIndex = (currentDragIndex - 2).coerceAtLeast(0)
                    newContext[DRAG_INDEX] = currentDragIndex + 1
                    newItems++

                    // Check if we've dragged all elements for this category.
                    if (currentDragIndex + 1 >= elements.size) {
                        newContext[PHASE] = "next"
                    }
                } else {
                    DiagnosticLogger.warn(TAG, "observe: drag failed, will retry")
                }
            }

            "next" -> {
                if (categoryQueue.isNotEmpty()) {
                    newContext[PHASE] = "scan"
                    newContext.remove(CATEGORY)
                    newContext.remove(DRAG_INDEX)
                    currentFolderBounds = null
                } else {
                    newContext[PHASE] = "done"
                }
            }
        }

        return state.copy(
            step = state.step + 1,
            foldersCreated = newFolders,
            itemsOrganized = newItems,
            errors = errors,
            context = newContext
        )
    }

    override fun isComplete(state: TaskState): Boolean {
        val phase = state.context[PHASE] as? String
        return phase == "done" || categoryQueue.isEmpty() || state.errors.size >= 5
    }

    override fun getFoldersCreated(): Int = foldersCreated

    // ──────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────

    /** Merge accessibility and vision perception, preferring accessibility elements. */
    private fun mergePerception(
        a11y: List<ScreenElement>,
        vision: List<ScreenElement>
    ): List<ScreenElement> {
        if (vision.isEmpty()) return a11y
        val result = a11y.toMutableList()
        for (v in vision) {
            val overlap = a11y.any { a ->
                android.graphics.Rect.intersects(a.bounds, v.bounds) &&
                    a.label.equals(v.label, ignoreCase = true)
            }
            if (!overlap) result.add(v)
        }
        return result
    }

    /** Categorize screen elements using [CategoryMatcher]. */
    private fun categorize(elements: List<ScreenElement>): Map<String, List<ScreenElement>> {
        return elements.groupBy { categoryMatcher.matchCategory(it.label) }
    }

    /**
     * Find a folder node in the perception whose bounds are near [hint].
     * Falls back to null if no folder-like node is found.
     */
    private fun findFolderNear(perception: List<ScreenElement>, hint: Rect): Rect? {
        // A folder node is typically larger than an icon and may have no label
        // or a label like "文件夹". Look for the nearest large element.
        return perception
            .filter { it.bounds.width() in 80..400 && it.bounds.height() in 80..400 }
            .minByOrNull { elem ->
                val cx = elem.bounds.exactCenterX()
                val cy = elem.bounds.exactCenterY()
                val dx = cx - hint.exactCenterX()
                val dy = cy - hint.exactCenterY()
                (dx * dx + dy * dy).toInt()
            }
            ?.bounds
    }
}
