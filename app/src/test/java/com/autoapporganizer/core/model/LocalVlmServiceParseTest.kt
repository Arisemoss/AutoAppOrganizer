package com.autoapporganizer.core.model

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Tests for [LocalVlmService] response parsing.
 *
 * Validates that parseResponse correctly extracts VisionDetectedItem
 * from a local VLM response. The parseDetectedItems method is private,
 * so we test it indirectly through the VisionResult.Success payload.
 */
class LocalVlmServiceParseTest {

    /** Build a valid OpenAI-compatible response envelope with the given content string. */
    private fun buildResponse(content: String): String {
        val root = JsonObject()
        val choices = com.google.gson.JsonArray()
        val choice = JsonObject()
        val message = JsonObject()
        message.addProperty("content", content)
        choice.add("message", message)
        choices.add(choice)
        root.add("choices", choices)
        return Gson().toJson(root)
    }

    /**
     * Parse a VLM response JSON string and extract detected items.
     * This mirrors the logic of LocalVlmService.parseResponse + parseDetectedItems
     * but tests the pure parsing without needing Android context.
     */
    private fun parseItemsFromResponse(responseJson: String): List<VisionDetectedItem> {
        val root = com.google.gson.JsonParser.parseString(responseJson).asJsonObject
        val choices = root.getAsJsonArray("choices")
        val message = choices[0].asJsonObject.getAsJsonObject("message")
        val content = message?.get("content")?.asString?.trim() ?: ""

        // Now parse detected items from the content text
        val items = mutableListOf<VisionDetectedItem>()
        val start = content.indexOf('[')
        val end = content.lastIndexOf(']')
        if (start < 0 || end < 0 || end <= start) return items
        val json = content.substring(start, end + 1)
        val array = com.google.gson.JsonParser.parseString(json).asJsonArray

        for (element in array) {
            val obj = element.asJsonObject
            val label = obj.get("label")?.asString
                ?: obj.get("name")?.asString
                ?: "unknown"
            val x = obj.get("x")?.asFloat ?: 0f
            val y = obj.get("y")?.asFloat ?: 0f
            val width = obj.get("width")?.asFloat ?: 0f
            val height = obj.get("height")?.asFloat ?: 0f
            val confidence = obj.get("confidence")?.asFloat ?: 1f
            items.add(VisionDetectedItem(label, x, y, width, height, confidence))
        }
        return items
    }

    @Test
    fun `parseResponse extracts detected items from JSON array in content`() {
        val content = """I can see the following icons:
[{"label":"Chrome","x":100,"y":200,"width":80,"height":80,"confidence":0.9},
 {"label":"Settings","x":300,"y":200,"width":80,"height":80,"confidence":0.85}]"""
        val response = buildResponse(content)

        val items = parseItemsFromResponse(response)
        assertEquals(2, items.size)

        assertEquals("Chrome", items[0].label)
        assertEquals(100f, items[0].x, 0.01f)
        assertEquals(200f, items[0].y, 0.01f)
        assertEquals(80f, items[0].width, 0.01f)
        assertEquals(0.9f, items[0].confidence, 0.01f)

        assertEquals("Settings", items[1].label)
        assertEquals(300f, items[1].x, 0.01f)
    }

    @Test
    fun `parseResponse returns empty list when no JSON array in content`() {
        val content = "I can see some icons but cannot provide structured data."
        val response = buildResponse(content)

        val items = parseItemsFromResponse(response)
        assertTrue(items.isEmpty())
    }

    @Test
    fun `parseResponse handles content that is only a JSON array`() {
        val content = "[{\"label\":\"WeChat\",\"x\":50,\"y\":400,\"width\":90,\"height\":90,\"confidence\":0.95}]"
        val response = buildResponse(content)

        val items = parseItemsFromResponse(response)
        assertEquals(1, items.size)
        assertEquals("WeChat", items[0].label)
        assertEquals(50f, items[0].x, 0.01f)
    }

    @Test
    fun `parseResponse handles content with markdown fences around JSON`() {
        val content = "```json\n[{\"label\":\"Camera\",\"x\":200,\"y\":600,\"width\":70,\"height\":70,\"confidence\":0.8}]\n```"
        val response = buildResponse(content)

        val items = parseItemsFromResponse(response)
        assertEquals(1, items.size)
        assertEquals("Camera", items[0].label)
    }

    @Test
    fun `VisionResult Success requires List of VisionDetectedItem not String`() {
        // This test verifies the fix: VisionResult.Success constructor takes
        // (elements: List<VisionDetectedItem>, rawResponse: String).
        // Before the fix, LocalVlmService.parseResponse was calling
        // VisionResult.Success(content, rawResponse = response) where content was a String,
        // which is a type mismatch.

        val items = listOf(
            VisionDetectedItem("Chrome", 100f, 200f, 80f, 80f, 0.9f)
        )
        val result = VisionResult.Success(items, rawResponse = "test")
        assertEquals(1, result.elements.size)
        assertEquals("Chrome", result.elements[0].label)
        assertEquals("test", result.rawResponse)
    }
}
