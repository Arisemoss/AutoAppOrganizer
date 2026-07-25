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
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

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
        // Default split: hold the origin for half the duration, then move for the other half.
        // Callers that need explicit control (e.g. mimicking a 600ms long-press followed by
        // a 500ms drag, as required by most launchers' edit-mode threshold) should use the
        // [performDrag] overload below that takes separate holdMs/dragMs.
        val half = (durationMs / 2).coerceAtLeast(1L)
        return performDrag(fromX, fromY, toX, toY, holdMs = half, dragMs = half)
    }

    /**
     * Drag with an explicit long-press phase ([holdMs]) followed by a move phase ([dragMs]).
     *
     * This overload exists because most home screen launchers require a long-press (>~400ms)
     * to enter edit mode before they will accept a drag. On API 26+ the two phases are
     * implemented as a continued stroke; on API 24-25 (no continueStroke) we fall back to a
     * single stroke that traces tiny circles at the origin (well below touch slop) to simulate
     * the hold, then moves to the destination — see [buildDragGesture] for details.
     */
    suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        holdMs: Long,
        dragMs: Long
    ): Boolean {
        if (!isValidCoordinate(fromX, fromY) || !isValidCoordinate(toX, toY)) {
            DiagnosticLogger.warn(
                TAG,
                "Drag rejected: invalid coords ($fromX,$fromY)->($toX,$toY)"
            )
            return false
        }
        val gesture = buildDragGesture(fromX, fromY, toX, toY, holdMs, dragMs)
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
     * Builds a drag gesture composed of a long-press phase ([holdMs]) followed by a move
     * phase ([dragMs]).
     *
     * - API 26+: two continued strokes (hold at origin → move to target). This is the only
     *   way to express a true press-and-hold followed by a drag with a single pointer.
     * - API 24-25: `continueStroke` / `willContinue` don't exist, so we cannot split the
     *   gesture into two strokes. Naively drawing a single line from origin to target fails
     *   because the pointer starts moving immediately and never triggers the launcher's
     *   long-press threshold (typically ~400ms) — the drag is silently ignored. Instead we
     *   trace tiny circles (radius `slop`, well below the ~8dp touch slop) at the origin so
     *   the system sees the pointer as effectively stationary during the [holdMs] portion,
     *   then we draw a line to the target. The system allocates stroke time proportionally
     *   to arc length, so we size the circle portion so that its arc length matches the
     *   hold:drag time ratio. This was previously implemented inline in
     *   `AutoAppOrganizerService.dragAndDrop` and has been consolidated here so both the
     *   legacy organize path and the Agent framework share a single, tested implementation.
     */
    private fun buildDragGesture(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        holdMs: Long,
        dragMs: Long
    ): GestureDescription {
        val totalMs = (holdMs + dragMs).coerceAtLeast(1L)
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // ① Hold phase: pointer stays at the origin. willContinue=true keeps it pressed.
            val downPath = Path().apply { moveTo(fromX, fromY) }
            val downStroke = GestureDescription.StrokeDescription(
                downPath, 0, holdMs.coerceAtLeast(1L), /* willContinue = */ true
            )

            // ② Move phase: continues from downStroke, moves to target, then releases.
            val movePath = Path().apply {
                moveTo(fromX, fromY)
                lineTo(toX, toY)
            }
            val moveStroke = downStroke.continueStroke(
                movePath, 0, dragMs.coerceAtLeast(1L), /* willContinue = */ false
            )

            GestureDescription.Builder()
                .addStroke(downStroke)
                .addStroke(moveStroke)
                .build()
        } else {
            // API 24-25: simulated long-press via tiny circles at the origin.
            // See method kdoc for why this is necessary and how the geometry is derived.
            val slop = 4f
            val dragDist = hypot(toX - fromX, toY - fromY).coerceAtLeast(1f)
            val holdRatio = holdMs.toFloat() / totalMs.toFloat()
            // Arc length needed for the hold phase so that time-proportional allocation
            // gives us ~holdMs of stationary time before the lineTo(toX, toY) segment.
            val holdLength = if (holdRatio < 1f) {
                (holdRatio / (1f - holdRatio)) * dragDist
            } else {
                // Edge case: dragMs == 0 → all time is hold; cap the loops to avoid huge paths.
                dragDist * 40f
            }
            val circumference = 2f * Math.PI.toFloat() * slop
            val loops = (holdLength / circumference).toInt().coerceIn(2, 40)
            val ptsPerLoop = 10
            val path = Path().apply {
                moveTo(fromX, fromY)
                val total = loops * ptsPerLoop
                var i = 0
                while (i < total) {
                    val angle = 2f * Math.PI.toFloat() * i / ptsPerLoop
                    lineTo(fromX + slop * cos(angle), fromY + slop * sin(angle))
                    i++
                }
                lineTo(fromX, fromY) // return to origin before moving to target
                lineTo(toX, toY)
            }
            val stroke = GestureDescription.StrokeDescription(path, 0, totalMs)
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
