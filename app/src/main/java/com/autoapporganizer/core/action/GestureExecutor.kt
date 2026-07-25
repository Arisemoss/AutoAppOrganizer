package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions,
 * executing them against the device through an [AccessibilityService].
 *
 * All gesture work is suspending and bounded by [GESTURE_TIMEOUT_MS]; a gesture that does
 * not complete in time is reported as a failure (`false`).
 *
 * @param service The hosting accessibility service used to dispatch gestures and global actions.
 */
class GestureExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "GestureExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L

        /** Stroke duration used for simple taps (ms). */
        private const val CLICK_DURATION_MS = 100L

        /** Settling delay after Home/Back global actions (ms). */
        private const val GLOBAL_ACTION_SETTLE_MS = 500L

        /** Screenshot capture timeout (ms). */
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
    }

    /**
     * Execute [action] and return `true` on success.
     *
     * For gesture actions the result reflects whether the gesture completed (vs. being
     * cancelled or timing out). For [Action.Wait] the result is always `true`; for
     * [Action.Complete] this is a no-op that also returns `true`.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.toX, action.toY, action.durationMs
            )
            is Action.Wait -> {
                delay(action.ms)
                true
            }
            Action.Home -> {
                val ok = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME)
                delay(GLOBAL_ACTION_SETTLE_MS)
                DiagnosticLogger.debug(TAG, "Home performed=$ok")
                ok
            }
            Action.Back -> {
                val ok = service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK)
                delay(GLOBAL_ACTION_SETTLE_MS)
                DiagnosticLogger.debug(TAG, "Back performed=$ok")
                ok
            }
            Action.Complete -> true
        }
    }

    /**
     * Capture a screenshot via the accessibility service (API 30+). Returns `null` when the
     * API is unavailable or the capture fails/times out.
     */
    suspend fun takeScreenshot(): Bitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            DiagnosticLogger.warn(TAG, "takeScreenshot requires API 30+ (current=${Build.VERSION.SDK_INT})")
            return null
        }
        return withTimeoutOrNull(SCREENSHOT_TIMEOUT_MS) {
            suspendCancellableCoroutine<Bitmap?> { cont ->
                try {
                    service.takeScreenshot(
                        Display.DEFAULT_DISPLAY,
                        ContextCompat.getMainExecutor(service),
                        object : AccessibilityService.TakeScreenshotCallback {
                            override fun onSuccess(result: AccessibilityService.ScreenshotResult) {
                                val bitmap = result.bitmap
                                DiagnosticLogger.debug(
                                    TAG,
                                    "Screenshot captured: ${bitmap.width}x${bitmap.height}"
                                )
                                if (cont.isActive) cont.resume(bitmap)
                            }

                            override fun onFailure(errorCode: Int) {
                                DiagnosticLogger.error(
                                    TAG,
                                    "takeScreenshot onFailure errorCode=$errorCode",
                                    null
                                )
                                if (cont.isActive) cont.resume(null)
                            }
                        }
                    )
                } catch (e: Exception) {
                    DiagnosticLogger.error(TAG, "takeScreenshot threw: ${e.message}")
                    if (cont.isActive) cont.resume(null)
                }
            }
        }.also {
            if (it == null) DiagnosticLogger.warn(TAG, "Screenshot capture returned null (timeout or failure)")
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Gesture implementations
    // ---------------------------------------------------------------------------------------------

    private suspend fun performClick(x: Float, y: Float): Boolean {
        if (!isValidCoordinate(x, y)) {
            DiagnosticLogger.warn(TAG, "Click rejected: invalid coords ($x,$y)")
            return false
        }
        val path = Path().apply { moveTo(x, y) }
        return dispatchGesture(buildGestureDescription(path, CLICK_DURATION_MS))
    }

    private suspend fun performLongPress(x: Float, y: Float, durationMs: Long): Boolean {
        if (!isValidCoordinate(x, y)) {
            DiagnosticLogger.warn(TAG, "LongPress rejected: invalid coords ($x,$y)")
            return false
        }
        val path = Path().apply { moveTo(x, y) }
        return dispatchGesture(buildGestureDescription(path, durationMs))
    }

    private suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        durationMs: Long
    ): Boolean {
        if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
            DiagnosticLogger.warn(
                TAG,
                "Drag rejected: invalid coords ($fromX,$fromY)->($toX,$toY)"
            )
            return false
        }
        val gesture = buildDragGesture(fromX, fromY, toX, toY, durationMs)
        return dispatchGesture(gesture)
    }

    // ---------------------------------------------------------------------------------------------
    // Gesture description builders
    // ---------------------------------------------------------------------------------------------

    /**
     * Builds a single-stroke [GestureDescription] along [path] lasting [durationMs].
     */
    private fun buildGestureDescription(path: Path, durationMs: Long): GestureDescription {
        val stroke = GestureDescription.StrokeDescription(path, 0, durationMs)
        return GestureDescription.Builder().addStroke(stroke).build()
    }

    /**
     * Builds a drag gesture. On API 26+ a continued stroke is used so the press is held at
     * the origin before moving smoothly to the destination (mimicking a real drag). On older
     * API levels a single slow stroke from origin to destination is used instead.
     */
    private fun buildDragGesture(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        durationMs: Long
    ): GestureDescription {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // API 26+: press-and-hold at the origin, then continue into a move to the target.
            val half = (durationMs / 2).coerceAtLeast(1L)

            val downPath = Path().apply { moveTo(fromX, fromY) }
            val downStroke = GestureDescription.StrokeDescription(downPath, 0, half, /* willContinue = */ true)

            val movePath = Path().apply {
                moveTo(fromX, fromY)
                lineTo(toX, toY)
            }
            val moveStroke = downStroke.continueStroke(movePath, 0, half, /* willContinue = */ false)

            GestureDescription.Builder()
                .addStroke(downStroke)
                .addStroke(moveStroke)
                .build()
        } else {
            // API 24-25: a single slow stroke from origin to destination.
            val path = Path().apply {
                moveTo(fromX, fromY)
                lineTo(toX, toY)
            }
            val stroke = GestureDescription.StrokeDescription(path, 0, durationMs)
            GestureDescription.Builder().addStroke(stroke).build()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Gesture dispatch
    // ---------------------------------------------------------------------------------------------

    /**
     * Dispatches [gesture] and suspends until it completes or is cancelled, bounded by
     * [GESTURE_TIMEOUT_MS]. Returns `false` on cancellation or timeout.
     */
    private suspend fun dispatchGesture(gesture: GestureDescription): Boolean {
        return withTimeoutOrNull(GESTURE_TIMEOUT_MS) {
            suspendCancellableCoroutine { cont ->
                try {
                    val dispatched = service.dispatchGesture(
                        gesture,
                        object : AccessibilityService.GestureResultCallback() {
                            override fun onCompleted(
                                gestureDescription: GestureDescription?,
                                motionEvent: MotionEvent?
                            ) {
                                if (cont.isActive) cont.resume(true)
                            }

                            override fun onCancelled(
                                gestureDescription: GestureDescription?,
                                motionEvent: MotionEvent?
                            ) {
                                DiagnosticLogger.warn(TAG, "Gesture cancelled by system")
                                if (cont.isActive) cont.resume(false)
                            }
                        },
                        /* handler = */ null
                    )
                    if (!dispatched) {
                        DiagnosticLogger.warn(TAG, "dispatchGesture returned false immediately")
                        if (cont.isActive) cont.resume(false)
                    }
                } catch (e: Exception) {
                    DiagnosticLogger.error(TAG, "dispatchGesture threw: ${e.message}")
                    if (cont.isActive) cont.resume(false)
                }
            }
        } ?: run {
            DiagnosticLogger.warn(TAG, "Gesture timed out after ${GESTURE_TIMEOUT_MS}ms")
            false
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------------------------------

    /**
     * `true` when both coordinates are non-negative (i.e. plausible screen coordinates).
     */
    private fun isValidCoordinate(x: Float, y: Float): Boolean = x >= 0f && y >= 0f
}
