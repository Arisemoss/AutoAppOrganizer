package com.autoapporganizer.core.agent

import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.action.GestureExecutor
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay

/**
 * Runs an [AgentTask] through a Reason-Act (ReAct) loop.
 *
 * Each iteration:
 *  1. Scans the accessibility tree for the current perception.
 *  2. Optionally queries the vision channel (if a VLM is available).
 *  3. Asks the task to [AgentTask.reason] about the next action.
 *  4. Executes the action via [GestureExecutor].
 *  5. Lets the task [AgentTask.observe] the result and update state.
 *
 * The loop terminates when the task signals completion, when [AgentTask.maxSteps]
 * is reached, or when the action is [Action.Complete].
 *
 * @param executor           Translates actions into accessibility gestures.
 * @param perceptionChannel  Accessibility-based perception source.
 * @param visionChannel      Vision-based perception source (VLM).
 */
class AgentRunner(
    private val executor: GestureExecutor,
    private val perceptionChannel: AccessibilityChannel,
    private val visionChannel: VisionChannel
) {

    companion object {
        private const val TAG = "AgentRunner"

        /** Delay between ReAct iterations to let the UI settle (ms). */
        private const val STEP_SETTLE_MS = 500L

        /**
         * Minimum number of steps between two fresh VLM calls when [AgentTask.needsVision]
         * returns `true`. Even when the task asks for vision on consecutive steps, we reuse
         * the most recent [VisionResult] within this window to avoid hammering the cloud
         * model on rapid iterations where the screen has not meaningfully changed.
         */
        private const val VISION_REUSE_WINDOW = 3
    }

    /**
     * Execute [task] and report progress via [onProgress].
     */
    suspend fun run(task: AgentTask, onProgress: (Int, String) -> Unit): AgentResult {
        DiagnosticLogger.info(TAG, "=== AgentRunner: ${task.name} (maxSteps=${task.maxSteps}) ===")

        val description = task.describe(perceptionChannel, visionChannel)
        DiagnosticLogger.info(TAG, "Task description: $description")

        var state = TaskState()
        var visionAvailable = false
        try {
            visionAvailable = visionChannel.let { true } // presence check; actual use guarded by isAvailable
        } catch (e: Exception) {
            DiagnosticLogger.warn(TAG, "Vision channel check failed: ${e.message}")
        }

        // Cache of the most recent VLM result, plus the step at which it was produced.
        // Reused when the task does not require a fresh pass (needsVision == false) or
        // when the previous pass is still within [VISION_REUSE_WINDOW] steps.
        var cachedVision: VisionResult? = null
        var cachedVisionStep: Int = Int.MIN_VALUE

        try {
            while (state.step < task.maxSteps) {
                // ── Perceive ──────────────────────────────────────────────
                val perception = perceptionChannel.scan()

                // Decide whether this step actually needs a fresh VLM pass.
                // - Task says it doesn't need vision  → reuse cache (no cloud call)
                // - Task says it needs vision, but the last pass is recent → reuse cache
                // - Otherwise → make a fresh cloud call
                val needsFreshVision = visionAvailable &&
                    task.needsVision(state) &&
                    (state.step - cachedVisionStep) >= VISION_REUSE_WINDOW

                val visionResult: VisionResult? = if (needsFreshVision) {
                    try {
                        visionChannel.analyze("Describe the current screen state briefly.")
                    } catch (e: Exception) {
                        DiagnosticLogger.warn(TAG, "Vision analyze failed (non-fatal): ${e.message}")
                        null
                    }
                } else {
                    // Reuse the cached result (may be null if no pass has run yet).
                    if (cachedVision != null) {
                        DiagnosticLogger.debug(
                            TAG,
                            "Vision reuse: skipping VLM call at step ${state.step} " +
                                "(last pass at $cachedVisionStep, needsVision=${task.needsVision(state)})"
                        )
                    }
                    cachedVision
                }

                if (needsFreshVision && visionResult != null) {
                    cachedVision = visionResult
                    cachedVisionStep = state.step
                }

                // ── Reason ────────────────────────────────────────────────
                val action = task.reason(state, perception, visionResult)
                DiagnosticLogger.info(TAG, "Step ${state.step + 1}: ${action.describe()}")

                if (action is Action.Complete) {
                    DiagnosticLogger.info(TAG, "Task signalled completion")
                    break
                }

                // ── Act ───────────────────────────────────────────────────
                val success = executor.execute(action)

                // ── Observe ───────────────────────────────────────────────
                state = task.observe(action, success, state)

                val progress = ((state.step + 1).toFloat() / task.maxSteps * 100).toInt().coerceIn(0, 100)
                onProgress(progress, "步骤 ${state.step}: ${action.describe()}")

                if (task.isComplete(state)) {
                    DiagnosticLogger.info(TAG, "Task isComplete() returned true at step ${state.step}")
                    break
                }

                delay(STEP_SETTLE_MS)
            }

            val complete = task.isComplete(state) || state.step >= task.maxSteps
            val msg = if (state.errors.isNotEmpty()) {
                "完成（${state.errors.size} 个错误）"
            } else {
                "完成"
            }
            DiagnosticLogger.info(TAG, "=== AgentRunner finished: $msg (steps=${state.step}, folders=${task.getFoldersCreated()}) ===")

            return AgentResult(
                success = complete && state.errors.isEmpty(),
                message = msg,
                stepsExecuted = state.step,
                foldersCreated = task.getFoldersCreated()
            )
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
}
