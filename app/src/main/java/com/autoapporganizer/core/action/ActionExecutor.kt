package com.autoapporganizer.core.action

import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay

/**
 * High-level action executor with retry, settle delay and error recovery.
 *
 * Callers describe *what* they want to do via [Action]s; this class decides
 * *how* to execute them reliably on the given [GestureEngine].
 */
class ActionExecutor(
    private val engine: GestureEngine,
    private val maxRetries: Int = 2,
    private val settleDelayMs: Long = 150L
) {

    companion object {
        private const val TAG = "ActionExecutor"
    }

    /**
     * Execute a single [action].
     *
     * @param action the action to perform
     * @param retryable if `true`, transient failures are retried up to [maxRetries] times
     * @return `true` if the action succeeded (or was [Action.Complete])
     */
    suspend fun execute(action: Action, retryable: Boolean = true): Boolean {
        val attempts = if (retryable) maxRetries + 1 else 1
        var lastError: Throwable? = null

        repeat(attempts) { attempt ->
            if (attempt > 0) {
                val backoff = settleDelayMs * attempt
                DiagnosticLogger.debug(TAG, "Retry ${attempt}/$maxRetries for ${action.describe()} after ${backoff}ms")
                delay(backoff)
            }

            val ok = try {
                engine.execute(action)
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "Action failed: ${action.describe()}, ${e.message}")
                lastError = e
                false
            }

            if (ok) {
                delay(settleDelayMs)
                return true
            }

            DiagnosticLogger.warn(TAG, "Action returned false: ${action.describe()}")
            lastError = lastError ?: IllegalStateException("Gesture engine returned false")
        }

        DiagnosticLogger.error(TAG, "Action exhausted retries: ${action.describe()}, lastError=${lastError?.message}")
        return false
    }

    /**
     * Execute a sequence of actions atomically (early abort on failure).
     *
     * @param actions actions to execute in order
     * @param abortOnFailure if `true`, stop on the first failed action
     * @return number of successfully executed actions
     */
    suspend fun executeSequence(
        actions: List<Action>,
        abortOnFailure: Boolean = true
    ): Int {
        var completed = 0
        for (action in actions) {
            val ok = execute(action)
            if (ok) {
                completed++
            } else if (abortOnFailure) {
                DiagnosticLogger.warn(TAG, "Sequence aborted at ${action.describe()}")
                break
            }
        }
        return completed
    }

    /**
     * Convenience: HOME → wait → screenshot.
     */
    suspend fun goHomeAndScreenshot(): ActionExecutorResult {
        val homeOk = execute(Action.Home)
        delay(settleDelayMs)
        val bitmap = engine.takeScreenshot()
        return ActionExecutorResult(homeOk, bitmap)
    }

    data class ActionExecutorResult(
        val success: Boolean,
        val screenshot: android.graphics.Bitmap? = null
    )
}
