package com.autoapporganizer.core.perception

import android.graphics.Rect
import com.autoapporganizer.testutil.TestHelpers.mockRect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [PerceptionFusion].
 */
class PerceptionFusionTest {

    private fun element(
        label: String,
        bounds: Rect,
        source: ScreenElement.Source = ScreenElement.Source.ACCESSIBILITY,
        confidence: Float = 1f,
        id: String = label
    ): ScreenElement = ScreenElement(
        id = id,
        label = label,
        bounds = bounds,
        confidence = confidence,
        source = source
    )

    @Test
    fun `merge empty lists returns empty list`() {
        val result = PerceptionFusion.merge(emptyList(), emptyList())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `unmatched accessibility nodes are kept as accessibility`() {
        val a11y = listOf(element("设置", mockRect(0, 0, 100, 100)))
        val result = PerceptionFusion.merge(emptyList(), a11y)
        assertEquals(1, result.size)
        assertEquals(ScreenElement.Source.ACCESSIBILITY, result[0].source)
        assertEquals("设置", result[0].label)
    }

    @Test
    fun `unmatched vision detections are kept as vision`() {
        val vision = listOf(
            element(
                "Chrome",
                mockRect(200, 200, 300, 300),
                source = ScreenElement.Source.VISION,
                confidence = 0.85f
            )
        )
        val result = PerceptionFusion.merge(vision, emptyList())
        assertEquals(1, result.size)
        assertEquals(ScreenElement.Source.VISION, result[0].source)
        assertEquals(0.85f, result[0].confidence, 0.001f)
    }

    @Test
    fun `overlapping vision and accessibility detections are fused`() {
        val vision = listOf(
            element(
                "Chrome",
                mockRect(110, 110, 210, 210),
                source = ScreenElement.Source.VISION,
                confidence = 0.8f,
                id = "v1"
            )
        )
        val a11y = listOf(
            element(
                "Chrome",
                mockRect(100, 100, 200, 200),
                source = ScreenElement.Source.ACCESSIBILITY,
                confidence = 1f,
                id = "a1"
            )
        )

        val result = PerceptionFusion.merge(vision, a11y)
        assertEquals(1, result.size)

        val fused = result[0]
        assertEquals(ScreenElement.Source.FUSED, fused.source)
        // Accessibility bounds are preferred.
        assertEquals(100, fused.bounds.left)
        assertEquals(100, fused.bounds.top)
        assertEquals(200, fused.bounds.right)
        assertEquals(200, fused.bounds.bottom)
        // Combined confidence should be higher than either source alone.
        assertEquals(1f - (1f - 0.8f) * (1f - 1f), fused.confidence, 0.001f)
    }

    @Test
    fun `vision detection without sufficient overlap remains separate`() {
        val vision = listOf(
            element(
                "Chrome",
                mockRect(500, 500, 600, 600),
                source = ScreenElement.Source.VISION,
                confidence = 0.9f
            )
        )
        val a11y = listOf(
            element(
                "Settings",
                mockRect(100, 100, 200, 200),
                source = ScreenElement.Source.ACCESSIBILITY
            )
        )

        val result = PerceptionFusion.merge(vision, a11y)
        assertEquals(2, result.size)
        val labels = result.map { it.label }.toSet()
        assertTrue(labels.contains("Chrome"))
        assertTrue(labels.contains("Settings"))
    }

    @Test
    fun `label merging prefers the more specific label`() {
        val vision = listOf(
            element(
                "Chrome",
                mockRect(110, 110, 210, 210),
                source = ScreenElement.Source.VISION,
                confidence = 0.8f
            )
        )
        val a11y = listOf(
            element(
                "Google Chrome",
                mockRect(100, 100, 200, 200),
                source = ScreenElement.Source.ACCESSIBILITY
            )
        )

        val result = PerceptionFusion.merge(vision, a11y)
        assertEquals("Google Chrome", result[0].label)
    }

    @Test
    fun `label merging concatenates unrelated labels`() {
        val vision = listOf(
            element(
                "Browser",
                mockRect(110, 110, 210, 210),
                source = ScreenElement.Source.VISION,
                confidence = 0.8f
            )
        )
        val a11y = listOf(
            element(
                "Chrome",
                mockRect(100, 100, 200, 200),
                source = ScreenElement.Source.ACCESSIBILITY
            )
        )

        val result = PerceptionFusion.merge(vision, a11y)
        assertEquals("Chrome / Browser", result[0].label)
    }

    @Test
    fun `multiple matches each consume only one accessibility node`() {
        val vision = listOf(
            element(
                "A",
                mockRect(105, 105, 195, 195),
                source = ScreenElement.Source.VISION,
                confidence = 0.9f,
                id = "vA"
            ),
            element(
                "B",
                mockRect(305, 305, 395, 395),
                source = ScreenElement.Source.VISION,
                confidence = 0.9f,
                id = "vB"
            )
        )
        val a11y = listOf(
            element("A", mockRect(100, 100, 200, 200), id = "aA"),
            element("B", mockRect(300, 300, 400, 400), id = "aB")
        )

        val result = PerceptionFusion.merge(vision, a11y)
        assertEquals(2, result.size)
        assertTrue(result.all { it.source == ScreenElement.Source.FUSED })
    }
}