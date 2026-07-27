package com.autoapporganizer.core.model

import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Tests for CloudVlmService JSON parsing — specifically the coordinate fallback logic.
 *
 * The old implementation used `if (it == 0f) fallback else it`, which silently
 * replaced legitimate zero coordinates with fallback values. The new implementation
 * uses nullable accessors (`asSafeFloatOrNull`) so that only *absent* fields fall
 * back to the alternate key.
 */
class CloudVlmServiceParseTest {

    // ── Replicate the accessor logic from CloudVlmService for direct testing ──

    private fun JsonPrimitive?.asSafeFloat(default: Float = 0f): Float {
        if (this == null) return default
        return try {
            if (isNumber) asFloat else asString.toFloatOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }

    private fun asSafeFloat(element: Any?, default: Float = 0f): Float {
        val primitive = element as? JsonPrimitive ?: return default
        return try {
            if (primitive.isNumber) primitive.asFloat else primitive.asString.toFloatOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }

    private fun asSafeFloatOrNull(element: Any?): Float? {
        if (element == null) return null
        val primitive = element as? JsonPrimitive ?: return null
        return try {
            if (primitive.isNumber) primitive.asFloat else primitive.asString.toFloatOrNull()
        } catch (e: Exception) {
            null
        }
    }

    @Test
    fun `x=0 is not replaced by boundsX when x is explicitly zero`() {
        val json = """{"label": "Camera", "x": 0, "y": 200, "width": 100, "height": 100, "boundsX": 540}"""
        val obj = JsonParser.parseString(json).asJsonObject

        // OLD logic: x=0 treated as missing → replaced by boundsX=540 (WRONG)
        val oldX = asSafeFloat(obj.get("x"))
            .let { if (it == 0f) asSafeFloat(obj.get("boundsX")) else it }
        assertEquals(540f, oldX, 0.001f) // old logic corrupts the coordinate

        // NEW logic: x=0 is a legitimate value, kept as-is
        val newX = asSafeFloatOrNull(obj.get("x"))
            ?: asSafeFloat(obj.get("boundsX"))
        assertEquals(0f, newX, 0.001f) // new logic preserves the zero
    }

    @Test
    fun `missing x falls back to boundsX`() {
        val json = """{"label": "Camera", "y": 200, "width": 100, "height": 100, "boundsX": 540}"""
        val obj = JsonParser.parseString(json).asJsonObject

        val x = asSafeFloatOrNull(obj.get("x"))
            ?: asSafeFloat(obj.get("boundsX"))
        assertEquals(540f, x, 0.001f) // falls back correctly
    }

    @Test
    fun `nonzero x is preserved`() {
        val json = """{"label": "Camera", "x": 100, "y": 200, "width": 50, "height": 50}"""
        val obj = JsonParser.parseString(json).asJsonObject

        val x = asSafeFloatOrNull(obj.get("x"))
            ?: asSafeFloat(obj.get("boundsX"))
        assertEquals(100f, x, 0.001f)
    }

    @Test
    fun `confidence=0 is not replaced by score`() {
        val json = """{"label": "App", "x": 100, "y": 200, "width": 50, "height": 50, "confidence": 0, "score": 0.9}"""
        val obj = JsonParser.parseString(json).asJsonObject

        // OLD logic: confidence=0 treated as missing → replaced by score=0.9 (WRONG)
        val oldConf = asSafeFloat(obj.get("confidence"), 1f)
            .let { if (it == 0f) asSafeFloat(obj.get("score"), 1f) else it }
        assertEquals(0.9f, oldConf, 0.001f) // old logic silently replaces zero confidence

        // NEW logic: confidence=0 is legitimate
        val newConf = asSafeFloatOrNull(obj.get("confidence"))
            ?: asSafeFloat(obj.get("score"), 1f)
        assertEquals(0f, newConf, 0.001f) // new logic preserves zero confidence
    }

    @Test
    fun `missing confidence falls back to score`() {
        val json = """{"label": "App", "x": 100, "y": 200, "width": 50, "height": 50, "score": 0.85}"""
        val obj = JsonParser.parseString(json).asJsonObject

        val conf = asSafeFloatOrNull(obj.get("confidence"))
            ?: asSafeFloat(obj.get("score"), 1f)
        assertEquals(0.85f, conf, 0.001f)
    }

    @Test
    fun `width=0 is preserved when explicitly zero`() {
        val json = """{"label": "Dot", "x": 540, "y": 1200, "width": 0, "height": 0, "w": 100, "h": 100}"""
        val obj = JsonParser.parseString(json).asJsonObject

        // OLD logic would replace width=0 with w=100
        val oldW = asSafeFloat(obj.get("width"))
            .let { if (it == 0f) asSafeFloat(obj.get("w")) else it }
        assertEquals(100f, oldW, 0.001f)

        // NEW logic preserves width=0
        val newW = asSafeFloatOrNull(obj.get("width"))
            ?: asSafeFloat(obj.get("w"))
        assertEquals(0f, newW, 0.001f)
    }
}
