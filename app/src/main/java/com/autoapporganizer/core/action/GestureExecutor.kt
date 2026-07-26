package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ColorSpace
import android.graphics.Path
import android.graphics.Rect
import android.hardware.HardwareBuffer
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.WindowManager
import android.view.accessibility.AccessibilityNodeInfo
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.math.max
import kotlin.math.min

/**
 * Accessibility-backed [GestureEngine].
 *
 * This implementation relies on the accessibility API to dispatch gestures,
 * simulate system buttons, and capture the screen. It is used by the production app;
 * tests can inject a fake [GestureEngine] instead.
 */
class GestureExecutor(private val service: AccessibilityService) : GestureEngine {

    companion object {
        private const val TAG = "GestureExecutor"
        private const val DEFAULT_CLICK_MS = 120L
        private const val DEFAULT_HOLD_MS = 600L
        private const val DEFAULT_DRAG_MS = 800L
        private const val DEFAULT_SWIPE_MS = 300L
        private const val SETTLE_MS = 120L
        private const val INVALID = -1

        /** Timeout for gesture dispatch (ms) — prevents indefinite hangs. */
        const val GESTURE_TIMEOUT_MS = 5_000L
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    override val screenWidth: Int
        get() = resolveScreenBounds().width()

    override val screenHeight: Int
        get() = resolveScreenBounds().height()

    /**
     * Resolve screen bounds from the active window root, falling back to
     * the display metrics when the accessibility tree is not yet populated.
     */
    private fun resolveScreenBounds(): Rect {
        val root = service.rootInActiveWindow
        if (root != null) {
            val bounds = Rect()
            root.getBoundsInScreen(bounds)
            if (bounds.width() > 0 && bounds.height() > 0) {
                root.recycle()
                return bounds
            }
            root.recycle()
        }

        val metrics = DisplayMetrics()
        @Suppress("DEPRECATION")
        (service.getSystemService(Context.WINDOW_SERVICE) as WindowManager)
            .defaultDisplay.getMetrics(metrics)
        return Rect(0, 0, metrics.widthPixels, metrics.heightPixels)
    }

    /**
     * Clamp coordinates to the screen bounds and ensure they are not negative.
     * Out-of-bounds gestures would silently fail or hit the wrong screen edge.
     */
    private fun clampToScreen(x: Float, y: Float): Pair<Float, Float> {
        val bounds = resolveScreenBounds()
        val maxX = max(bounds.left, bounds.right - 1).toFloat()
        val maxY = max(bounds.top, bounds.bottom - 1).toFloat()
        val minX = bounds.left.toFloat()
        val minY = bounds.top.toFloat()
        return Pair(
            min(maxX, max(minX, x)),
            min(maxY, max(minY, y))
        )
    }

    private fun isWithinScreen(x: Float, y: Float): Boolean {
        val bounds = resolveScreenBounds()
        return x >= bounds.left && x < bounds.right && y >= bounds.top && y < bounds.bottom
    }

    override suspend fun execute(action: Action): Boolean = when (action) {
        is Action.Click -> performClick(action.x, action.y)
        is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
        is Action.Drag -> performDrag(
            action.fromX, action.fromY,
            action.toX, action.toY,
            action.durationMs
        )
        is Action.Swipe -> performSwipe(
            action.fromX, action.fromY,
            action.toX, action.toY,
            action.durationMs
        )
        is Action.Type -> performType(action.text)
        is Action.Wait -> {
            kotlinx.coroutines.delay(action.ms)
            true
        }
        Action.Home -> performGlobal(AccessibilityService.GLOBAL_ACTION_HOME)
        Action.Back -> performGlobal(AccessibilityService.GLOBAL_ACTION_BACK)
        Action.Complete -> true
    }

    override suspend fun performClick(x: Float, y: Float): Boolean {
        if (!isWithinScreen(x, y)) {
            DiagnosticLogger.warn(TAG, "Click out of bounds: ($x,$y), screen=${screenWidth}x$screenHeight")
        }
        val (cx, cy) = clampToScreen(x, y)
        val path = Path().apply { moveTo(cx, cy) }
        val stroke = GestureDescription.StrokeDescription(path, 0, DEFAULT_CLICK_MS)
        return dispatchGesture(stroke, "Click($cx,$cy)")
    }

    override suspend fun performLongPress(x: Float, y: Float, durationMs: Long): Boolean {
        if (!isWithinScreen(x, y)) {
            DiagnosticLogger.warn(TAG, "LongPress out of bounds: ($x,$y)")
        }
        val (cx, cy) = clampToScreen(x, y)
        val path = Path().apply { moveTo(cx, cy) }
        val stroke = GestureDescription.StrokeDescription(path, 0, max(durationMs, DEFAULT_CLICK_MS))
        return dispatchGesture(stroke, "LongPress($cx,$cy,$durationMs)")
    }

    override suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        durationMs: Long
    ): Boolean = performDrag(fromX, fromY, toX, toY, DEFAULT_HOLD_MS, durationMs)

    override suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        holdMs: Long, dragMs: Long
    ): Boolean {
        if (!isWithinScreen(fromX, fromY) || !isWithinScreen(toX, toY)) {
            DiagnosticLogger.warn(TAG, "Drag out of bounds: ($fromX,$fromY)->($toX,$toY)")
        }
        val (fx, fy) = clampToScreen(fromX, fromY)
        val (tx, ty) = clampToScreen(toX, toY)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val path = Path().apply {
                moveTo(fx, fy)
                lineTo(tx, ty)
            }
            val pressMs = max(holdMs, 0)
            val moveMs = max(dragMs, 100L)
            val stroke = GestureDescription.StrokeDescription(path, pressMs, moveMs, true)
            val result = dispatchGesture(stroke, "Drag($fx,$fy->$tx,$ty h=${pressMs}ms d=${moveMs}ms)")
            kotlinx.coroutines.delay(SETTLE_MS)
            return result
        }

        // Fallback for pre-O: dispatch two separate strokes.
        val pressPath = Path().apply { moveTo(fx, fy) }
        val press = GestureDescription.StrokeDescription(pressPath, 0, holdMs)
        val movePath = Path().apply {
            moveTo(fx, fy)
            lineTo(tx, ty)
        }
        val move = GestureDescription.StrokeDescription(movePath, 0, max(dragMs, 100L))
        val pressOk = dispatchGesture(press, "Drag-press($fx,$fy)")
        kotlinx.coroutines.delay(SETTLE_MS)
        val moveOk = dispatchGesture(move, "Drag-move($fx,$fy->$tx,$ty)")
        kotlinx.coroutines.delay(SETTLE_MS)
        return pressOk && moveOk
    }

    private suspend fun performSwipe(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        durationMs: Long
    ): Boolean {
        if (!isWithinScreen(fromX, fromY) || !isWithinScreen(toX, toY)) {
            DiagnosticLogger.warn(TAG, "Swipe out of bounds: ($fromX,$fromY)->($toX,$toY)")
        }
        val (fx, fy) = clampToScreen(fromX, fromY)
        val (tx, ty) = clampToScreen(toX, toY)
        val path = Path().apply {
            moveTo(fx, fy)
            lineTo(tx, ty)
        }
        val stroke = GestureDescription.StrokeDescription(
            path,
            0,
            max(durationMs, DEFAULT_SWIPE_MS)
        )
        return dispatchGesture(stroke, "Swipe($fx,$fy->$tx,$ty)")
    }

    private suspend fun performType(text: String): Boolean {
        DiagnosticLogger.debug(TAG, "Type action requested for ${text.length} chars")
        return try {
            val root = service.rootInActiveWindow
            val focused = root?.findFocus(AccessibilityNodeInfo.FOCUS_INPUT)
            if (focused != null && focused.isEditable) {
                val bundle = android.os.Bundle().apply {
                    putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text)
                }
                val ok = focused.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, bundle)
                focused.recycle()
                root.recycle()
                ok
            } else {
                // Fallback: try shell input (requires ADB/root; gracefully degrades).
                Runtime.getRuntime().exec(arrayOf("input", "text", text.replace(" ", "%s"))).waitFor()
                true
            }
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Type failed: ${e.message}")
            false
        }
    }

    private suspend fun performGlobal(globalAction: Int): Boolean {
        return try {
            service.performGlobalAction(globalAction)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Global action $globalAction failed: ${e.message}")
            false
        }
    }

    private suspend fun dispatchGesture(
        stroke: GestureDescription.StrokeDescription,
        label: String
    ): Boolean = kotlinx.coroutines.withTimeoutOrNull(GESTURE_TIMEOUT_MS) {
        suspendCancellableCoroutine { cont ->
            val builder = GestureDescription.Builder().addStroke(stroke)
            val dispatched = service.dispatchGesture(builder.build(), object : AccessibilityService.GestureResultCallback() {
                override fun onCompleted(gestureDescription: GestureDescription?) {
                    DiagnosticLogger.debug(TAG, "Gesture completed: $label")
                    if (cont.isActive) cont.resume(true)
                }

                override fun onCancelled(gestureDescription: GestureDescription?) {
                    DiagnosticLogger.warn(TAG, "Gesture cancelled: $label")
                    if (cont.isActive) cont.resume(false)
                }
            }, mainHandler)

            if (!dispatched) {
                DiagnosticLogger.error(TAG, "dispatchGesture rejected immediately: $label")
                cont.resume(false)
            }
        }
    } ?: run {
        DiagnosticLogger.error(TAG, "Gesture timed out after ${GESTURE_TIMEOUT_MS}ms: $label")
        false
    }

    /**
     * Capture a screenshot via the accessibility API.
     *
     * NOTE: Prefer taking the screenshot through the perception layer
     * ([AccessibilityChannel]) in production to keep screenshot logic in one place.
     * This method is kept to satisfy [GestureEngine.takeScreenshot] for tests and
     * simple callers; it delegates to the same accessibility API.
     */
    override suspend fun takeScreenshot(): Bitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            DiagnosticLogger.warn(TAG, "Screenshot requires API 30+")
            return null
        }
        val displayId = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            service.display?.displayId ?: 0
        } else 0
        return suspendCancellableCoroutine { cont ->
            val callback = object : AccessibilityService.TakeScreenshotCallback {
                override fun onSuccess(result: AccessibilityService.ScreenshotResult) {
                    val bitmap = try {
                        val hwBuffer = result.hardwareBuffer
                        val colorSpace = result.colorSpace
                        Bitmap.wrapHardwareBuffer(hwBuffer, colorSpace)
                    } catch (e: Exception) {
                        DiagnosticLogger.warn(TAG, "Failed to wrap hardware buffer: ${e.message}")
                        null
                    }
                    if (cont.isActive) cont.resume(bitmap)
                }

                override fun onFailure(errorCode: Int) {
                    DiagnosticLogger.warn(TAG, "Screenshot failed with errorCode=$errorCode")
                    if (cont.isActive) cont.resume(null)
                }
            }
            service.takeScreenshot(displayId, service.mainExecutor, callback)
        }
    }
}
