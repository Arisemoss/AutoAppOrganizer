package com.autoapporganizer.core.agent

import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.action.GestureEngine
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.VisionChannel
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay

/**
 * Runs an [AgentTask] through a Reason-Act (ReAct) loop with retry and recovery.
 *
 * Each iteration:
 *  1. Scans the accessibility tree for the current perception.
 *  2. Optionally queries the vision channel (if a VLM is available).
 *  3. Asks the task to [AgentTask.reason] about the next action.
 *  4. Executes the action via [GestureEngine].
 *  5. Lets the task [AgentTask.observe] the result and update state.
 *
 * The loop terminates when the task signals completion, when [AgentTask.maxSteps]
 * is reached, or when the action is [Action.Complete].
 *
 * @param engine             Translates actions into accessibility gestures.
 * @param perceptionChannel  Accessibility-based perception source.
 * @param visionChannel      Vision-based perception source (VLM).
 */
class AgentRunner(
    private val engine: GestureEngine,
    private val perceptionChannel: AccessibilityChannel,
    private val visionChannel: VisionChannel
) {

    companion object {
        private const val TAG = "AgentRunner"

        /** Delay between ReAct iterations to let the UI settle (ms). */
        private const val STEP_SETTLE_MS = 500L

        /** Delay after a failed action before retrying (ms). */
        private const val FAILURE_BACKOFF_MS = 800L

        /**
         * Maximum number of consecutive step-level retries before aborting.
         * A "retry" means the action failed but the task wants to try again
         * (i.e. [TaskState.retryHint] is true).
         */
        private const val MAX_CONSECUTIVE_RETRIES = 3

        /**
         * Minimum number of steps between two fresh VLM calls when [AgentTask.needsVision]
         * returns `true`. Even when the task asks for vision on consecutive steps, we reuse
         * the most recent [VisionResult] within this window to avoid hammering the cloud
         * model on rapid iterations where the screen has not meaningfully changed.
         */
        private const val VISION_REUSE_WINDOW = 3

        /** Maximum total errors before the runner gives up. */
        private const val MAX_TOTAL_ERRORS = 15

        /** Maximum total task duration in milliseconds (5 minutes). */
        private const val MAX_TASK_DURATION_MS = 300_000L

        /** Minimum valid screen dimension for coordinate validation. */
        private const val MIN_SCREEN_DIMENSION = 100
    }

    /**
     * Execute [task] and report progress via [onProgress].
     *
     * @param task The task to execute.
     * @param onProgress Callback: (percent 0-100, description).
     * @return [AgentResult] summarizing the outcome.
     */
    suspend fun run(task: AgentTask, onProgress: (Int, String) -> Unit): AgentResult {
        DiagnosticLogger.info(TAG, "=== AgentRunner: ${task.name} (maxSteps=${task.maxSteps}) ===")

        val description = task.describe(perceptionChannel, visionChannel)
        DiagnosticLogger.info(TAG, "Task description: $description")

        var state = TaskState()
        val visionAvailable = runCatching { visionChannel.isAvailable() }.getOrElse { false }
        if (!visionAvailable) {
            DiagnosticLogger.warn(TAG, "Vision channel not available; running in accessibility-only mode")
        }

        // Cache of the most recent VLM result, plus the step at which it was produced.
        var cachedVision: VisionResult? = null
        var cachedVisionStep: Int = Int.MIN_VALUE

        // Track consecutive retries and failures at the step level.
        var consecutiveRetries = 0
        var consecutiveFailures = 0
        var lastAction: Action? = null

        // Task duration guard
        val startTime = System.currentTimeMillis()

        // Screen dimensions for coordinate validation
        val screenW = engine.screenWidth
        val screenH = engine.screenHeight
        val hasValidScreen = screenW > MIN_SCREEN_DIMENSION && screenH > MIN_SCREEN_DIMENSION

        try {
            while (state.step < task.maxSteps) {
                // ── Guard: task duration exceeded ──────────────────────────
                val elapsed = System.currentTimeMillis() - startTime
                if (elapsed > MAX_TASK_DURATION_MS) {
                    DiagnosticLogger.warn(TAG, "Task duration exceeded ${MAX_TASK_DURATION_MS}ms, aborting")
                    break
                }

                // ── Guard: too many total errors ─────────────────────────
                if (state.errors.size >= MAX_TOTAL_ERRORS) {
                    DiagnosticLogger.warn(TAG, "Too many errors (${state.errors.size}), aborting")
                    break
                }

                // ── Perceive ──────────────────────────────────────────────
                val perception = perceptionChannel.scanElements()

                // Decide whether this step needs a fresh VLM pass.
                val needsFreshVision = visionAvailable &&
                    task.needsVision(state) &&
                    (state.step - cachedVisionStep) >= VISION_REUSE_WINDOW

                val visionResult: VisionResult? = if (needsFreshVision) {
                    val result = try {
                        val prompt = buildVisionPrompt(state, perception, task)
                        visionChannel.analyze(prompt)
                    } catch (e: Exception) {
                        DiagnosticLogger.warn(TAG, "Vision analyze failed (non-fatal): ${e.message}")
                        null
                    }
                    if (result != null) {
                        cachedVision = result
                        cachedVisionStep = state.step
                    }
                    result
                } else {
                    if (cachedVision != null && task.needsVision(state)) {
                        DiagnosticLogger.debug(
                            TAG,
                            "Vision reuse: skipping VLM call at step ${state.step + 1} " +
                                "(last pass at ${cachedVisionStep + 1})"
                        )
                    }
                    cachedVision
                }

                // ── Reason ────────────────────────────────────────────────
                val action = task.reason(state, perception, visionResult)
                DiagnosticLogger.info(TAG, "Step ${state.step + 1}/${task.maxSteps}: ${action.describe()}")

                if (action is Action.Complete) {
                    DiagnosticLogger.info(TAG, "Task signalled completion")
                    break
                }

                // ── Validate action coordinates ───────────────────────────
                if (hasValidScreen && !isActionValid(action, screenW, screenH)) {
                    DiagnosticLogger.warn(
                        TAG,
                        "Action coordinates out of bounds ($screenW x $screenH): ${action.describe()}, skipping"
                    )
                    state = state.copy(
                        step = state.step + 1,
                        errors = state.errors + "Invalid action coordinates: ${action.describe()}"
                    )
                    continue
                }

                // ── Act ───────────────────────────────────────────────────
                val success = engine.execute(action)

                // ── Observe ───────────────────────────────────────────────
                state = task.observe(action, success, state)

                // ── Retry / failure handling ──────────────────────────────
                if (!success) {
                    consecutiveFailures++
                    consecutiveRetries++

                    if (state.retryHint && consecutiveRetries < MAX_CONSECUTIVE_RETRIES) {
                        DiagnosticLogger.warn(
                            TAG,
                            "Action failed, retrying (${consecutiveRetries}/$MAX_CONSECUTIVE_RETRIES): ${action.describe()}"
                        )
                        delay(FAILURE_BACKOFF_MS * consecutiveRetries)
                        // Don't advance step on retry; the task's observe() already
                        // updated context with retry state.
                        continue
                    }

                    if (consecutiveFailures >= 5) {
                        DiagnosticLogger.error(
                            TAG,
                            "Too many consecutive failures ($consecutiveFailures), aborting"
                        )
                        break
                    }
                } else {
                    consecutiveRetries = 0
                    consecutiveFailures = 0
                }

                lastAction = action

                val progress = calculateProgress(state, task)
                onProgress(progress, "步骤 ${state.step}: ${action.describe()}")

                if (task.isComplete(state)) {
                    DiagnosticLogger.info(TAG, "Task isComplete() returned true at step ${state.step}")
                    break
                }

                delay(STEP_SETTLE_MS)
            }

            val complete = task.isComplete(state) || state.step >= task.maxSteps
            val elapsed = System.currentTimeMillis() - startTime
            val msg = when {
                elapsed > MAX_TASK_DURATION_MS -> "终止（超时：${elapsed / 1000}秒）"
                state.errors.size >= MAX_TOTAL_ERRORS -> "终止（错误过多：${state.errors.size}）"
                state.errors.isNotEmpty() -> "完成（${state.errors.size} 个错误）"
                else -> "完成"
            }
            DiagnosticLogger.info(
                TAG,
                "=== AgentRunner finished: $msg (steps=${state.step}, errors=${state.errors.size}, folders=${task.getFoldersCreated()}) ==="
            )

            return AgentResult(
                success = complete && state.errors.isEmpty(),
                message = msg,
                stepsExecuted = state.step,
                foldersCreated = task.getFoldersCreated()
            )
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "AgentRunner crashed: ${e.message} (${e.javaClass.simpleName})")
            return AgentResult(
                success = false,
                message = "Agent 异常: ${e.message}",
                stepsExecuted = state.step,
                foldersCreated = task.getFoldersCreated()
            )
        }
    }

    private fun calculateProgress(state: TaskState, task: AgentTask): Int {
        val raw = ((state.step.toFloat() / task.maxSteps) * 95f).toInt()
        return raw.coerceIn(0, 95)
    }

    /**
     * Validate that action coordinates are within screen bounds.
     * Drag actions are the most critical — out-of-bounds drags silently fail.
     */
    private fun isActionValid(action: Action, screenW: Int, screenH: Int): Boolean {
        return when (action) {
            is Action.Click -> action.x in 0f..screenW.toFloat() && action.y in 0f..screenH.toFloat()
            is Action.LongPress -> action.x in 0f..screenW.toFloat() && action.y in 0f..screenH.toFloat()
            is Action.Drag -> {
                action.fromX in 0f..screenW.toFloat() && action.fromY in 0f..screenH.toFloat() &&
                action.toX in 0f..screenW.toFloat() && action.toY in 0f..screenH.toFloat()
            }
            is Action.Swipe -> {
                action.fromX in 0f..screenW.toFloat() && action.fromY in 0f..screenH.toFloat() &&
                action.toX in 0f..screenW.toFloat() && action.toY in 0f..screenH.toFloat()
            }
            // Non-coordinate-based actions are always valid
            is Action.Type, is Action.Wait, Action.Home, Action.Back, Action.Complete -> true
        }
    }

    /**
     * Build a context-aware prompt for the VLM, incorporating the current task state,
     * detected elements, and phase information.
     */
    private fun buildVisionPrompt(
        state: TaskState,
        perception: List<com.autoapporganizer.core.perception.ScreenElement>,
        task: AgentTask
    ): String {
        val phase = state.context["phase"] as? String ?: "scan"
        val category = state.context["category"] as? String
        val dragIndex = state.context["dragIndex"] as? Int

        val elementsDesc = perception.take(30).joinToString("\n") { el ->
            "  - ${el.label} @ (${el.centerX.toInt()}, ${el.centerY.toInt()}) " +
                "[${el.bounds.width()}x${el.bounds.height()}]"
        }

        return buildString {
            appendLine("You are an Android UI automation assistant. Your task is to organize home screen app icons into folders by category.")
            appendLine()
            appendLine("## Current State")
            appendLine("- Phase: $phase")
            appendLine("- Step: ${state.step + 1}/${task.maxSteps}")
            appendLine("- Items organized so far: ${state.itemsOrganized}")
            if (category != null) {
                appendLine("- Active category: $category")
                appendLine("- Drag progress: ${dragIndex ?: "N/A"}")
            }
            appendLine()

            appendLine("## Detected Screen Elements (label @ center_coords [size])")
            appendLine(if (elementsDesc.isBlank()) "(No elements detected)" else elementsDesc)
            appendLine()

            appendLine("## Available Actions")
            appendLine("- drag: Drag one icon onto another to create a folder. Drag an icon to a folder to add it.")
            appendLine("  Format: {\"type\": \"drag\", \"fromX\": N, \"fromY\": N, \"toX\": N, \"toY\": N, \"durationMs\": 800}")
            appendLine("- wait: Wait for UI to settle. Format: {\"type\": \"wait\", \"ms\": N}")
            appendLine("- complete: Signal that the task is finished.")
            appendLine()

            appendLine("## Instructions")
            appendLine("Look at the screenshot carefully. Decide the SINGLE most appropriate next action.")
            appendLine("Use absolute pixel coordinates for drag gestures.")
            appendLine("If icons of the same category are already grouped together, drag one onto the other to create a folder.")
            appendLine("If a folder already exists, drag remaining icons into it.")
            appendLine()
            appendLine("Return STRICT JSON only (no markdown, no explanation):")
            appendLine("{\"thought\": \"brief reasoning for this action\", \"actions\": [{\"type\": \"drag\", \"fromX\": N, \"fromY\": N, \"toX\": N, \"toY\": N, \"durationMs\": 800}]}")
            appendLine()
            appendLine("If the task is finished, return: {\"thought\": \"All icons organized\", \"actions\": [{\"type\": \"complete\"}]}")
        }
    }
}