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
     * Renamed from `scan()` to make the cost obvious at the call site: this triggers a
     * cloud VLM call (1-3s + API fee), unlike [AccessibilityChannel.scanElements] which
     * only walks the local accessibility tree.
     */
    suspend fun detectIcons(): List<ScreenElement>
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

    override fun isAvailable(): Boolean = vlm.isAvailable

    companion object {
        private const val TAG = "VisionChannel"

        /**
         * Builds the icon-scan prompt for a screen of the given size.
         *
         * The screen dimensions are injected so the model reports coordinates in the
         * correct pixel space (a model that does not know the image resolution tends to
         * hallucinate or mis-scale boxes). The prompt also demands strict JSON, unique
         * detections, and explicitly excludes folders/widgets/chrome so the downstream
         * [DetectionFilter] has fewer false positives to remove.
         */
        private fun buildIconScanPrompt(screenWidth: Int, screenHeight: Int): String =
            "Identify all app icons visible on this Android home screen. " +
                "The screenshot is ${screenWidth}x${screenHeight} pixels. " +
                "Return ONLY a JSON array - no markdown, no code fence, no prose, nothing else. " +
                "Each array element must be an object with exactly these keys: " +
                "\"label\" (string: the app name shown under the icon), " +
                "\"x\" (number), \"y\" (number), \"width\" (number), \"height\" (number), " +
                "\"confidence\" (number 0..1). " +
                "x and y are the ABSOLUTE pixel coordinates of the icon's top-left corner " +
                "in the ${screenWidth}x${screenHeight} screenshot; width and height are the " +
                "icon's size in pixels. " +
                "Detect each icon exactly once - do not report any icon twice. " +
                "Only report app icons: ignore folders, widgets, the dock, the status bar " +
                "and the wallpaper. " +
                "If this is not a home screen or no app icons are visible, return []."
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

    override suspend fun detectIcons(): List<ScreenElement> {
        if (!vlm.isAvailable) {
            DiagnosticLogger.debug(TAG, "scan: VLM not available, returning empty list")
            return emptyList()
        }

        // Capture once: the bitmap provides both the image and the screen dimensions
        // needed to build a resolution-aware prompt.
        val bitmap = accessibilityChannel.screenshot()
        if (bitmap == null) {
            DiagnosticLogger.warn(TAG, "scan: screenshot capture failed")
            return emptyList()
        }

        val prompt = buildIconScanPrompt(bitmap.width, bitmap.height)
        return when (val result = vlm.analyze(bitmap, prompt)) {
            is VisionResult.Success -> {
                DiagnosticLogger.debug(
                    TAG,
                    "scan: VLM detected ${result.elements.size} item(s) on ${bitmap.width}x${bitmap.height}"
                )
                val raw = result.elements.mapIndexed { index, item ->
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
                // De-duplicate, reject hallucinations and clamp coordinates.
                val filtered = DetectionFilter.apply(raw, bitmap.width, bitmap.height)
                DiagnosticLogger.debug(
                    TAG,
                    "scan: ${filtered.size} item(s) after filter (${raw.size - filtered.size} dropped)"
                )
                filtered
            }
            is VisionResult.Error -> {
                DiagnosticLogger.warn(TAG, "scan: VLM error: ${result.message}")
                emptyList()
            }
        }
    }
}
