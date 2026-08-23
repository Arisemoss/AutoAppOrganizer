package com.autoapporganizer.core.layout

import android.graphics.Rect
import com.autoapporganizer.core.perception.ScreenElement
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [DragOptimizer].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DragOptimizerTest {

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
    fun `prioritizeCategories orders by count then dispersion`() {
        val categories = mapOf(
            "Small" to listOf(element("A", 100, 100), element("B", 200, 200)),
            "Large" to listOf(
                element("C", 300, 300), element("D", 310, 310),
                element("E", 320, 320), element("F", 330, 330)
            ),
            "Medium" to listOf(element("G", 400, 400), element("H", 410, 410), element("I", 420, 420))
        )

        val result = DragOptimizer.prioritizeCategories(categories)

        // Large (4 items) should be first
        assertEquals("Large", result[0])
        // Medium (3 items) should be second
        assertEquals("Medium", result[1])
        // Small (2 items) should be last
        assertEquals("Small", result[2])
    }

    @Test
    fun `prioritizeCategories handles empty map`() {
        val result = DragOptimizer.prioritizeCategories(emptyMap())
        assertTrue(result.isEmpty())
    }

    @Test
    fun `optimizeCategory returns empty plan for single element`() {
        val elements = listOf(element("A", 100, 100))
        val plan = DragOptimizer.optimizeCategory(elements)

        assertNull(plan.anchor)
        assertEquals(1, plan.ordered.size)
        assertTrue(plan.dragSteps.isEmpty())
    }

    @Test
    fun `optimizeCategory creates plan with correct steps`() {
        val elements = listOf(
            element("A", 100, 100),
            element("B", 200, 200),
            element("C", 300, 300)
        )
        val plan = DragOptimizer.optimizeCategory(elements)

        assertNotNull(plan.anchor)
        assertEquals(3, plan.ordered.size)
        assertEquals(3, plan.dragSteps.size)
        assertTrue(plan.dragSteps[0].isFolderCreation)
    }

    @Test
    fun `optimizeCategory handles two elements`() {
        val elements = listOf(
            element("A", 100, 100),
            element("B", 200, 200)
        )
        val plan = DragOptimizer.optimizeCategory(elements)

        assertNotNull(plan.anchor)
        assertEquals(2, plan.ordered.size)
        assertEquals(1, plan.dragSteps.size) // Only folder creation
        assertTrue(plan.dragSteps[0].isFolderCreation)
    }
}
