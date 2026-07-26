package com.autoapporganizer.core.perception

import com.autoapporganizer.core.model.VisionResult

/**
 * Source of screen information derived from a vision-language model: it captures a
 * screenshot via the [AccessibilityChannel] and asks a vision model to analyse it.
 */
interface VisionChannel {

    /** Whether the underlying vision model is configured and reachable. */
    fun isAvailable(): Boolean

    /**
     * Capture the current screen and run the VLM with [prompt], returning the raw
     * [VisionResult].
     */
    suspend fun analyze(prompt: String): VisionResult

    /**
     * Run a vision pass specialized for finding app icons on the home screen, returning
     * the detections as [ScreenElement]s with [ScreenElement.Source.VISION].
     * Returns an empty list when the VLM is unavailable or the analysis fails.
     *
     * This triggers a cloud VLM call (1-3s + API fee), unlike [AccessibilityChannel.scanElements]
     * which only walks the local accessibility tree.
     */
    suspend fun detectIcons(): List<ScreenElement>
}
