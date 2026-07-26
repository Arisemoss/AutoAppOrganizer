package com.autoapporganizer.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [VisionResult] and [VisionDetectedItem].
 *
 * Guard against the regression where [VisionResult.Success] was constructed with
 * a String in the `elements` position (the data class expects List<VisionDetectedItem>).
 */
class VisionResultTest {

    @Test
    fun `Success elements field is List of VisionDetectedItem`() {
        val items = listOf(
            VisionDetectedItem("Chrome", 100f, 200f, 80f, 80f, 0.9f),
            VisionDetectedItem("Settings", 300f, 200f, 80f, 80f, 0.85f)
        )
        val result = VisionResult.Success(items, rawResponse = "test")
        assertEquals(2, result.elements.size)
        assertEquals("Chrome", result.elements[0].label)
        assertEquals("Settings", result.elements[1].label)
        assertEquals("test", result.rawResponse)
    }

    @Test
    fun `Success with empty elements list is valid`() {
        val result = VisionResult.Success(emptyList(), rawResponse = "{}")
        assertTrue(result.elements.isEmpty())
        assertEquals("{}", result.rawResponse)
    }

    @Test
    fun `Error carries message and optional exception`() {
        val result = VisionResult.Error("failed", RuntimeException("boom"))
        assertEquals("failed", result.message)
        assertTrue(result.exception is RuntimeException)
    }

    @Test
    fun `Error without exception has null exception field`() {
        val result = VisionResult.Error("failed")
        assertEquals("failed", result.message)
        assertEquals(null, result.exception)
    }
}
