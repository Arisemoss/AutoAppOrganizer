package com.autoapporganizer.core.layout

import android.graphics.Rect
import com.autoapporganizer.core.perception.ScreenElement
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [SpatialClusterer].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class SpatialClustererTest {

    private fun element(
        label: String,
        x: Int,
        y: Int,
        size: Int = 80
    ): ScreenElement = ScreenElement(
        id = label,
        label = label,
        bounds = Rect(x, y, x + size, y + size),
        confidence = 1f,
        source = ScreenElement.Source.ACCESSIBILITY
    )

    @Test
    fun `sortByProximity returns single element for single input`() {
        val elements = listOf(element("A", 100, 100))
        val result = SpatialClusterer.sortByProximity(elements)
        assertEquals(listOf(0), result)
    }

    @Test
    fun `sortByProximity orders by distance to centroid`() {
        val elements = listOf(
            element("Far", 500, 500),    // far from centroid
            element("Center", 200, 200), // near centroid
            element("Mid", 300, 300)     // mid distance
        )
        val result = SpatialClusterer.sortByProximity(elements)

        // Center should be first (closest to centroid ~333,333)
        assertEquals(1, result[0]) // Center
    }

    @Test
    fun `computeCentroid returns correct center`() {
        val elements = listOf(
            element("A", 100, 100),
            element("B", 300, 300)
        )
        val centroid = SpatialClusterer.computeCentroid(elements)
        assertEquals(240f, centroid.x, 1f) // (100+40)/2 + (300+40)/2 = 140+170 = 310/2... actually centerX
        // centerX of A = 100+40 = 140, centerX of B = 300+40 = 340, avg = 240
        assertEquals(240f, centroid.y, 1f)
    }

    @Test
    fun `computeCentroid returns origin for empty list`() {
        val centroid = SpatialClusterer.computeCentroid(emptyList())
        assertEquals(0f, centroid.x, 0.001f)
        assertEquals(0f, centroid.y, 0.001f)
    }

    @Test
    fun `findAnchorPair returns two closest to centroid`() {
        val elements = listOf(
            element("Far1", 0, 0),
            element("Center", 200, 200),
            element("Near", 220, 220),
            element("Far2", 500, 500)
        )
        val (anchor, second) = SpatialClusterer.findAnchorPair(elements)

        // Anchor should be closest to centroid
        assertEquals(1, anchor) // Center
        // Second should be closest to anchor
        assertEquals(2, second) // Near
    }

    @Test(expected = IllegalArgumentException::class)
    fun `findAnchorPair throws for single element`() {
        SpatialClusterer.findAnchorPair(listOf(element("A", 100, 100)))
    }

    @Test
    fun `optimizeDragSequence returns empty for single element`() {
        val result = SpatialClusterer.optimizeDragSequence(listOf(element("A", 100, 100)))
        assertTrue(result.isEmpty())
    }

    @Test
    fun `optimizeDragSequence creates correct steps`() {
        val elements = listOf(
            element("A", 100, 100),
            element("B", 200, 200),
            element("C", 300, 300)
        )
        val steps = SpatialClusterer.optimizeDragSequence(elements)

        // Should have 3 steps: create folder + 2 drags
        assertEquals(3, steps.size)
        // First step is folder creation
        assertTrue(steps[0].isFolderCreation)
        // Remaining steps are not folder creation
        assertFalse(steps[1].isFolderCreation)
        assertFalse(steps[2].isFolderCreation)
    }
}
