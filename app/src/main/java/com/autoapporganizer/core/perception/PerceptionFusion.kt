package com.autoapporganizer.core.perception

import android.graphics.Rect
import kotlin.math.max
import kotlin.math.min

/**
 * Merges accessibility-node evidence with vision-model detections.
 *
 * Vision models can "see" icons even when the launcher hides them from the
 * accessibility tree; the accessibility tree can supply stable labels and bounds.
 * This class combines both channels into a single list of [ScreenElement]s with
 * a combined confidence score.
 */
object PerceptionFusion {

    /**
     * Two detections are considered the same icon if their bounding boxes
     * overlap by at least this ratio (intersection / smaller box).
     */
    private const val MIN_OVERLAP_RATIO = 0.5f

    /**
     * Two detections are considered the same icon if their centers are within
     * this distance in pixels.
     */
    private const val CENTER_DISTANCE_PX = 40

    /**
     * Merge [vision] and [accessibility] lists.
     *
     * The algorithm:
     * 1. For every vision detection, try to find a matching accessibility node.
     * 2. If a match is found, keep the accessibility bounds (usually more stable)
     *    and combine the labels; the confidence is boosted.
     * 3. Unmatched vision detections are kept as-is (e.g. icons hidden from a11y).
     * 4. Unmatched accessibility nodes are kept as-is (e.g. text-only items).
     */
    fun merge(
        vision: List<ScreenElement>,
        accessibility: List<ScreenElement>
    ): List<ScreenElement> {
        val matchedA11y = BooleanArray(accessibility.size)
        val merged = mutableListOf<ScreenElement>()

        for (v in vision) {
            var bestIdx = -1
            var bestScore = 0f

            for ((idx, a) in accessibility.withIndex()) {
                if (matchedA11y[idx]) continue
                val score = matchScore(v.bounds, a.bounds, v.label, a.label)
                if (score > bestScore) {
                    bestScore = score
                    bestIdx = idx
                }
            }

            if (bestIdx >= 0 && bestScore >= MIN_OVERLAP_RATIO) {
                matchedA11y[bestIdx] = true
                val a = accessibility[bestIdx]
                merged.add(
                    ScreenElement(
                        id = a.id.takeIf { it.isNotBlank() } ?: v.id,
                        label = mergeLabel(a.label, v.label),
                        bounds = a.bounds,
                        confidence = combineConfidence(v.confidence, a.confidence),
                        source = ScreenElement.Source.FUSED
                    )
                )
            } else {
                merged.add(
                    v.copy(source = ScreenElement.Source.VISION)
                )
            }
        }

        // Append unmatched accessibility nodes.
        for ((idx, a) in accessibility.withIndex()) {
            if (!matchedA11y[idx]) {
                merged.add(a.copy(source = ScreenElement.Source.ACCESSIBILITY))
            }
        }

        return merged
    }

    private fun matchScore(
        a: Rect,
        b: Rect,
        labelA: String,
        labelB: String
    ): Float {
        val overlap = intersectionArea(a, b)
        val smaller = min(a.width() * a.height(), b.width() * b.height())
        val overlapRatio = if (smaller > 0) overlap.toFloat() / smaller else 0f

        val centerA = android.graphics.Point(a.centerX(), a.centerY())
        val centerB = android.graphics.Point(b.centerX(), b.centerY())
        val distance = Math.hypot(
            (centerA.x - centerB.x).toDouble(),
            (centerA.y - centerB.y).toDouble()
        )
        val centerScore = 1f - (distance / CENTER_DISTANCE_PX).coerceIn(0.0, 1.0).toFloat()

        val labelScore = if (labelA.isNotBlank() && labelB.isNotBlank() &&
            (labelA.contains(labelB, ignoreCase = true) || labelB.contains(labelA, ignoreCase = true))
        ) 1f else 0f

        return (overlapRatio * 0.6f) + (centerScore * 0.25f) + (labelScore * 0.15f)
    }

    private fun intersectionArea(a: Rect, b: Rect): Int {
        val left = max(a.left, b.left)
        val top = max(a.top, b.top)
        val right = min(a.right, b.right)
        val bottom = min(a.bottom, b.bottom)
        return if (right > left && bottom > top) (right - left) * (bottom - top) else 0
    }

    private fun mergeLabel(a11yLabel: String, visionLabel: String): String {
        val a = a11yLabel.trim()
        val v = visionLabel.trim()
        return when {
            a.isBlank() -> v
            v.isBlank() -> a
            a.equals(v, ignoreCase = true) -> a
            a.contains(v, ignoreCase = true) -> a
            v.contains(a, ignoreCase = true) -> v
            else -> "$a / $v"
        }
    }

    private fun combineConfidence(c1: Float, c2: Float): Float {
        // Independent sources: P(A ∪ B) = 1 - (1-c1)(1-c2)
        return 1f - (1f - c1.coerceIn(0f, 1f)) * (1f - c2.coerceIn(0f, 1f))
    }
}
