package com.autoapporganizer.core.action

import android.graphics.Bitmap

/**
 * Platform-independent gesture engine abstraction.
 *
 * Implementations translate high-level [Action]s into platform gestures
 * (accessibility gestures, ADB, etc.) and provide screen capture.
 * This interface exists so that tests can inject a fake engine and so the
 * application can swap the underlying input method without changing callers.
 */
interface GestureEngine {

    /** Execute [action] and return `true` on success. */
    suspend fun execute(action: Action): Boolean

    /** Tap at screen coordinates ([x], [y]). */
    suspend fun performClick(x: Float, y: Float): Boolean

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    suspend fun performLongPress(x: Float, y: Float, durationMs: Long): Boolean

    /**
     * Drag from ([fromX], [fromY]) to ([toX], [toY]).
     *
     * @param durationMs Total duration; the engine may split it into a
     *                   long-press phase and a move phase as appropriate.
     */
    suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        durationMs: Long
    ): Boolean

    /**
     * Drag with explicit long-press ([holdMs]) and move ([dragMs]) phases.
     */
    suspend fun performDrag(
        fromX: Float, fromY: Float,
        toX: Float, toY: Float,
        holdMs: Long, dragMs: Long
    ): Boolean

    /** Capture a screenshot, or return `null` if unavailable. */
    suspend fun takeScreenshot(): Bitmap?

    /** Current screen width in pixels, or -1 if unknown. */
    val screenWidth: Int

    /** Current screen height in pixels, or -1 if unknown. */
    val screenHeight: Int
}
