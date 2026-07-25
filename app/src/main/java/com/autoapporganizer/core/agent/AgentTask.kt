package com.autoapporganizer.core.agent

import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.core.perception.VisionChannel

/**
 * Mutable state carried across ReAct iterations by [AgentRunner].
 *
 * NOTE: folder count is intentionally NOT tracked here. [AgentTask.getFoldersCreated]
 * is the single source of truth (typically backed by a task-local counter), because
 * folder creation is a task-specific event that does not map cleanly onto a per-step
 * state field. Keeping it out of [TaskState] avoids a second, divergent counter.
 *
 * @param step           Current step index (0-based).
 * @param itemsOrganized Number of icons successfully dragged into folders.
 * @param errors         Accumulated error messages.
 * @param context        Free-form bag for task-specific flags (e.g. "phase", "currentCategory").
 */
data class TaskState(
    val step: Int = 0,
    val itemsOrganized: Int = 0,
    val errors: List<String> = emptyList(),
    val context: Map<String, Any> = emptyMap()
)

/**
 * Outcome of running an [AgentTask] through [AgentRunner].
 */
data class AgentResult(
    val success: Boolean,
    val message: String,
    val stepsExecuted: Int,
    val foldersCreated: Int = 0
)

/**
 * A discrete goal the vision agent can pursue via the ReAct loop.
 *
 * The runner calls [describe] once at the start, then repeatedly calls
 * [reason] → execute → [observe] until [isComplete] returns true or [maxSteps]
 * is reached.
 */
interface AgentTask {

    val name: String

    /** Maximum number of ReAct iterations before the runner gives up. */
    val maxSteps: Int

    /** Describe the initial goal for logging. Called once before the loop. */
    suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String

    /** Decide the next [com.autoapporganizer.core.action.Action] based on state and perception. */
    suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): com.autoapporganizer.core.action.Action

    /** Update state after an action was executed. */
    suspend fun observe(action: com.autoapporganizer.core.action.Action, result: Boolean, state: TaskState): TaskState

    /** Check if the task is complete. */
    fun isComplete(state: TaskState): Boolean

    /**
     * Whether the next reason() iteration needs a fresh VLM pass for [state].
     *
     * Default is `false`: most steps only need the accessibility tree, and the
     * caller ([AgentRunner]) may reuse the most recent [VisionResult] when this
     * returns `false`. Tasks that genuinely need a vision pass (e.g. an initial
     * icon scan or locating a newly-created folder) should override and return
     * `true` for the relevant phase.
     */
    fun needsVision(state: TaskState): Boolean = false

    /** Get number of folders created (for reporting). */
    fun getFoldersCreated(): Int
}
