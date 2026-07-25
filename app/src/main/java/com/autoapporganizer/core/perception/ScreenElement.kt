package com.autoapporganizer.core.perception

import android.graphics.Rect

/**
 * A unified description of a single interactable element on the screen, regardless of
 * whether it was discovered through the AccessibilityService tree or through a vision
 * (VLM) pass.
 *
 * @param id           Stable identifier for the element within a scan (not globally unique).
 * @param label        Human-readable label (content description, text, or VLM-detected name).
 * @param bounds       The element's bounds in screen coordinates.
 * @param confidence   Confidence in the detection (1f for accessibility, 0..1 for vision).
 * @param source       Where this element originated from.
 * @param packageName  Optional package name of the app owning the element.
 */
data class ScreenElement(
    val id: String,
    val label: String,
    val bounds: Rect,
    val confidence: Float = 1f,
    val source: Source = Source.ACCESSIBILITY,
    val packageName: String? = null
) {
    /** Origin of a [ScreenElement]. */
    enum class Source { ACCESSIBILITY, VISION }

    /** Horizontal center of the bounds. */
    val centerX: Float
        get() = bounds.exactCenterX()

    /** Vertical center of the bounds. */
    val centerY: Float
        get() = bounds.exactCenterY()

    /** Width of the bounds in pixels. */
    val width: Int
        get() = bounds.width()

    /** Height of the bounds in pixels. */
    val height: Int
        get() = bounds.height()
}
