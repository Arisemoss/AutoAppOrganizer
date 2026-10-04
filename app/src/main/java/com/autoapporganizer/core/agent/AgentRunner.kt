package com.autoapporganizer.core.agent

import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.action.GestureEngine
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay

/**
 * Runs an [AgentTask] through a Reason-Act loop.
 *
 * Each iteration:
 *  1. Scans the accessibility tree for a fresh perception (launchers re-grid icons
 *     after every drop, so only the latest scan reflects the true layout).
 *  2. Asks the task to [AgentTask.reason] about the next action.
 *  3. Executes the action via [GestureEngine].
 *  4. Lets the task [AgentTask.observe] the result and update state.
 *
 * The loop terminates when the task signals [Action.Complete], when
 * [AgentTask.isComplete] returns true, or when [AgentTask.maxSteps] is reached.
 *
 * Note: no VLM calls happen inside the loop. The single meaningful vision pass
 * (icon detection + classification) runs in [AgentTask.describe]; a per-step
 * "state description" call used to exist here but nothing consumed its result,
 * so it only burned API quota. Reintroduce loop-level vision only together with
 * a consumer that actually acts on the coordinates.
 *
 * @param engine             Translates actions into accessibility gestures.
 * @param perceptionChannel  Accessibility-based perception source.
 * @param visionChannel      Vision-based perception source (used by describe only).
 */
class AgentRunner(
    private val engine: GestureEngine,
    private val perceptionChannel: AccessibilityChannel,
    private val visionChannel: VisionChannel
) {

    companion object {
        private const val TAG = "AgentRunner"

        /** Delay between iterations to let the UI settle (ms). */
        private const val STEP_SETTLE_MS = 500L
    }

    /**
     * Execute [task] and report progress via [onProgress].
     */
    suspend fun run(task: AgentTask, onProgress: (Int, String) -> Unit): AgentResult {
        DiagnosticLogger.info(TAG, "=== AgentRunner: ${task.name} (maxSteps=${task.maxSteps}) ===")

        val description = task.describe(perceptionChannel, visionChannel)
        DiagnosticLogger.info(TAG, "Task description: $description")

        var state = TaskState()
        var completed = false

        try {
            while (state.step < task.maxSteps) {
                // ── Perceive ──────────────────────────────────────────────
                val perception = perceptionChannel.scanElements()

                // ── Reason ────────────────────────────────────────────────
                val action = task.reason(state, perception)
                DiagnosticLogger.info(TAG, "Step ${state.step + 1}: ${action.describe()}")

                if (action is Action.Complete) {
                    DiagnosticLogger.info(TAG, "Task signalled completion")
                    completed = true
                    break
                }

                // ── Act ───────────────────────────────────────────────────
                val success = engine.execute(action)

                // ── Observe ───────────────────────────────────────────────
                state = task.observe(action, success, state)

                onProgress(calculateProgress(state, task), "步骤 ${state.step}: ${action.describe()}")

                if (task.isComplete(state)) {
                    DiagnosticLogger.info(TAG, "Task isComplete() returned true at step ${state.step}")
                    completed = true
                    break
                }

                delay(STEP_SETTLE_MS)
            }

            // Reaching maxSteps without the task completing is a failure, not a
            // success: previously `step >= maxSteps` was OR-ed into the completed
            // check and half-finished runs were reported as done.
            val exhausted = !completed && state.step >= task.maxSteps
            val msg = when {
                exhausted -> "已达最大步数（${task.maxSteps}），整理未全部完成"
                state.errors.isNotEmpty() -> "完成（${state.errors.size} 个错误）"
                else -> "完成"
            }
            DiagnosticLogger.info(
                TAG,
                "=== AgentRunner finished: $msg (steps=${state.step}, folders=${task.getFoldersCreated()}) ==="
            )

            return AgentResult(
                success = completed && state.errors.isEmpty(),
                message = msg,
                stepsExecuted = state.step,
                foldersCreated = task.getFoldersCreated()
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "AgentRunner crashed: ${e.message}")
            return AgentResult(
                success = false,
                message = "Agent 异常: ${e.message}",
                stepsExecuted = state.step,
                foldersCreated = task.getFoldersCreated()
            )
        }
    }

    private fun calculateProgress(state: TaskState, task: AgentTask): Int {
        // Cap progress at 95% until the task explicitly completes; reserve 100% for done.
        val raw = ((state.step.toFloat() / task.maxSteps) * 95f).toInt()
        return raw.coerceIn(0, 95)
    }
}
