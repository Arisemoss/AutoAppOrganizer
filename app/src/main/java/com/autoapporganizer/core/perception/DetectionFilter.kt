package com.autoapporganizer.core.perception

import android.graphics.Rect
import com.autoapporganizer.util.DiagnosticLogger
import kotlin.math.max
import kotlin.math.min

/**
 * Post-processing for VLM icon detections.
 *
 * Vision models frequently return imperfect boxes: duplicate detections of the same
 * icon, hallucinated icons in empty space, widgets/folders mistaken for apps, or
 * coordinates slightly outside the screen. This filter normalizes those raw detections
 * before they enter the fusion/categorization pipeline:
 *
 *  1. [nms] — non-maximum suppression: boxes overlapping the same icon are merged,
 *     keeping only the highest-confidence detection.
 *  2. Size constraints — icons must be within a plausible fraction of the screen
 *     width (rejects both folder-sized boxes and tiny hallucination boxes).
 *  3. Confidence floor — low-confidence detections are discarded.
 *  4. Out-of-screen handling — boxes fully outside the screen are dropped; partially
 *     visible boxes are clamped back onto the screen.
 */
object DetectionFilter {

    private const val TAG = "DetectionFilter"

    /** Detections below this confidence are treated as hallucinations and dropped. */
    const val DEFAULT_MIN_CONFIDENCE = 0.35f

    /** Two boxes whose IoU is at least this are considered the same icon. */
    const val NMS_IOU_THRESHOLD = 0.5f

    /** An icon's side must be at least this fraction of the screen width (e.g. 4%). */
    private const val MIN_SIDE_RATIO = 0.04f

    /** An icon's side must be at most this fraction of the screen width (e.g. 26%). */
    private const val MAX_SIDE_RATIO = 0.26f

    /** Absolute lower bound for an icon side in pixels (matches a11y channel floor). */
    private const val ABS_MIN_SIDE_PX = 40

    /**
     * Apply the full filter pipeline. Detections are sorted top-to-bottom then
     * left-to-right so the output order is deterministic for logging and testing.
     */
    fun apply(
        elements: List<ScreenElement>,
        screenWidth: Int,
        screenHeight: Int,
        minConfidence: Float = DEFAULT_MIN_CONFIDENCE
    ): List<ScreenElement> {
        if (elements.isEmpty() || screenWidth <= 0 || screenHeight <= 0) return emptyList()

        val minSide = (screenWidth * MIN_SIDE_RATIO).coerceAtLeast(ABS_MIN_SIDE_PX.toFloat())
        val maxSide = (screenWidth * MAX_SIDE_RATIO).coerceAtLeast(minSide)

        var kept = elements.filter { el ->
            val w = el.bounds.width()
            val h = el.bounds.height()
            val sizeOk = w >= minSide && h >= minSide && w <= maxSide && h <= maxSide
            if (!sizeOk) {
                DiagnosticLogger.debug(
                    TAG,
                    "dropping '${el.label}' size ${w}x${h} outside [$minSide..$maxSide]"
                )
            }
            val confOk = el.confidence >= minConfidence
            if (!confOk) {
                DiagnosticLogger.debug(
                    TAG,
                    "dropping '${el.label}' confidence ${el.confidence} < $minConfidence"
                )
            }
            sizeOk && confOk
        }

        kept = kept.mapNotNull { clampToScreen(it, screenWidth, screenHeight) }
        kept = nms(kept, NMS_IOU_THRESHOLD)

        return kept.sortedWith(
            compareBy({ it.bounds.top }, { it.bounds.left })
        )
    }

    /**
     * Greedy non-maximum suppression. Boxes are processed in descending confidence
     * order; any box whose IoU with an already-kept box is at least [iouThreshold]
     * is suppressed. Ties in confidence keep the earlier element (stable sort).
     */
    fun nms(
        elements: List<ScreenElement>,
        iouThreshold: Float = NMS_IOU_THRESHOLD
    ): List<ScreenElement> {
        if (elements.size <= 1) return elements

        val sorted = elements.sortedByDescending { it.confidence }
        val suppressed = BooleanArray(sorted.size)
        val kept = mutableListOf<ScreenElement>()

        for (i in sorted.indices) {
            if (suppressed[i]) continue
            kept.add(sorted[i])
            for (j in i + 1 until sorted.size) {
                if (suppressed[j]) continue
                if (iou(sorted[i].bounds, sorted[j].bounds) >= iouThreshold) {
                    DiagnosticLogger.debug(
                        TAG,
                        "nms: suppressing '${sorted[j].label}' (IoU with '${sorted[i].label}' " +
                            "= ${iou(sorted[i].bounds, sorted[j].bounds)})"
                    )
                    suppressed[j] = true
                }
            }
        }
        return kept
    }

    /** Intersection-over-union of two rectangles, 0f when either is empty. */
    fun iou(a: Rect, b: Rect): Float {
        val left = max(a.left, b.left)
        val top = max(a.top, b.top)
        val right = min(a.right, b.right)
        val bottom = min(a.bottom, b.bottom)
        val intersection = if (right > left && bottom > top) (right - left) * (bottom - top) else 0
        val union = a.width() * a.height() + b.width() * b.height() - intersection
        return if (union <= 0) 0f else intersection.toFloat() / union
    }

    /**
     * Drop boxes fully outside the screen; clamp partially visible boxes back onto it.
     */
    private fun clampToScreen(el: ScreenElement, screenWidth: Int, screenHeight: Int): ScreenElement? {
        val b = el.bounds
        if (b.right <= 0 || b.bottom <= 0 || b.left >= screenWidth || b.top >= screenHeight) {
            DiagnosticLogger.debug(TAG, "dropping '${el.label}' fully outside screen: $b")
            return null
        }
        val clamped = Rect(
            b.left.coerceIn(0, screenWidth - 1),
            b.top.coerceIn(0, screenHeight - 1),
            b.right.coerceIn(1, screenWidth),
            b.bottom.coerceIn(1, screenHeight)
        )
        return if (clamped == b) el else el.copy(bounds = clamped)
    }
}