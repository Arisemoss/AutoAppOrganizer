package com.autoapporganizer.core.perception

import android.graphics.Rect
import com.autoapporganizer.core.model.VisionModelService
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.util.DiagnosticLogger

/**
 * Source of screen information derived from a vision-language model: it captures a
 * screenshot via the [AccessibilityChannel] and asks a [VisionModelService] to analyse it.
 */
interface VisionChannel {

    /**
     * Capture the current screen and run the VLM with [prompt], returning the raw
     * [VisionResult].
     */
    suspend fun analyze(prompt: String): VisionResult

    /**
     * Run a vision pass specialized for finding app icons on the home screen, returning
     * the detections as [ScreenElement]s with [ScreenElement.Source.VISION].
     * Returns an empty list when the VLM is unavailable or the analysis fails.
     */
    suspend fun scan(): List<ScreenElement>
}

/**
 * [VisionChannel] that combines an [AccessibilityChannel] (for screenshots) with a
 * [VisionModelService] (for analysis) and normalizes VLM detections into [ScreenElement]s.
 *
 * @param accessibilityChannel Used to capture screenshots.
 * @param vlm                  The vision-language model used to analyse the screenshots.
 */
class VisionChannelImpl(
    private val accessibilityChannel: AccessibilityChannel,
    private val vlm: VisionModelService
) : VisionChannel {

    companion object {
        private const val TAG = "VisionChannel"

        private const val ICON_SCAN_PROMPT =
            "Identify all app icons visible on the home screen / desktop. " +
                "Return ONLY a JSON array (no markdown, no prose) where each object has " +
                "the keys: label (string), x (number), y (number), width (number), " +
                "height (number), confidence (number 0..1). " +
                "x and y are the top-left absolute pixel coordinates of the icon; " +
                "width and height are the icon size in pixels."
    }

    override suspend fun analyze(prompt: String): VisionResult {
        val bitmap = accessibilityChannel.screenshot()
        if (bitmap == null) {
            DiagnosticLogger.warn(TAG, "analyze: screenshot capture failed")
            return VisionResult.Error("Failed to capture screenshot")
        }
        DiagnosticLogger.debug(
            TAG,
            "analyze: captured ${bitmap.width}x${bitmap.height}, invoking VLM (prompt len=${prompt.length})"
        )
        return vlm.analyze(bitmap, prompt)
    }

    override suspend fun scan(): List<ScreenElement> {
        if (!vlm.isAvailable) {
            DiagnosticLogger.debug(TAG, "scan: VLM not available, returning empty list")
            return emptyList()
        }

        return when (val result = analyze(ICON_SCAN_PROMPT)) {
            is VisionResult.Success -> {
                DiagnosticLogger.debug(TAG, "scan: VLM detected ${result.elements.size} item(s)")
                result.elements.mapIndexed { index, item ->
                    val left = item.x.toInt()
                    val top = item.y.toInt()
                    val right = (item.x + item.width).toInt()
                    val bottom = (item.y + item.height).toInt()
                    ScreenElement(
                        id = "vision_$index",
                        label = item.label,
                        bounds = Rect(left, top, right, bottom),
                        confidence = item.confidence,
                        source = ScreenElement.Source.VISION,
                        packageName = null
                    )
                }
            }
            is VisionResult.Error -> {
                DiagnosticLogger.warn(TAG, "scan: VLM error: ${result.message}")
                emptyList()
            }
        }
    }
}
