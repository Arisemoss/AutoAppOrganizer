package com.autoapporganizer.core.model

/**
 * Represents the outcome of a vision (VLM) analysis pass.
 *
 * To avoid a circular dependency on the perception layer, the success payload holds a
 * list of [VisionDetectedItem] (a plain value type declared in this file) rather than a
 * [com.autoapporganizer.core.perception.ScreenElement]. Callers in the perception layer
 * are responsible for converting items into screen elements when needed.
 */
sealed class VisionResult {

    /**
     * Vision analysis succeeded. [elements] contains the items detected by the model and
     * [rawResponse] preserves the original textual response for diagnostics/debugging.
     */
    data class Success(
        val elements: List<VisionDetectedItem>,
        val rawResponse: String
    ) : VisionResult()

    /**
     * Vision analysis failed. [message] is a human-readable description of the failure
     * and [exception] carries the underlying cause when available.
     */
    data class Error(
        val message: String,
        val exception: Throwable? = null
    ) : VisionResult()
}

/**
 * A single item detected by the vision model.
 *
 * Coordinates ([x], [y], [width], [height]) are assumed to be absolute pixel positions
 * as returned by the VLM (the caller is free to normalize/transform them afterwards).
 *
 * @param label      Human-readable name/label of the detected element.
 * @param x          Absolute X coordinate of the element's top-left corner.
 * @param y          Absolute Y coordinate of the element's top-left corner.
 * @param width      Width of the element in pixels.
 * @param height     Height of the element in pixels.
 * @param confidence Model confidence in the range 0..1 (defaults to 1f when unknown).
 */
data class VisionDetectedItem(
    val label: String,
    val x: Float,
    val y: Float,
    val width: Float,
    val height: Float,
    val confidence: Float = 1f
)
