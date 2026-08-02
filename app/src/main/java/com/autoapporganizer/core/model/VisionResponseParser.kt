package com.autoapporganizer.core.model

import com.autoapporganizer.util.DiagnosticLogger
import com.google.gson.JsonElement
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive

/**
 * Shared parser that extracts [VisionDetectedItem]s from a VLM's free-text answer.
 *
 * Both [CloudVlmService] and [LocalVlmService] receive the model's textual response and
 * need to normalize it into structured detections. This object centralizes the lenient
 * extraction so the two services behave identically regardless of provider.
 */
object VisionResponseParser {

    private const val TAG = "VisionResponseParser"

    /**
     * Extracts the JSON array of detected items embedded in the model's free-text answer.
     *
     * The VLM is instructed to emit a JSON array; this method locates the first `[` and the
     * last `]` in the text to tolerate surrounding prose/markdown fences. Field names are
     * matched leniently (e.g. `label`/`name`, `x`/`boundsX`).
     */
    fun parseDetectedItems(text: String): List<VisionDetectedItem> {
        val items = mutableListOf<VisionDetectedItem>()
        try {
            val start = text.indexOf('[')
            val end = text.lastIndexOf(']')
            if (start < 0 || end < 0 || end <= start) {
                DiagnosticLogger.warn(TAG, "No JSON array found in VLM text")
                return items
            }
            val json = text.substring(start, end + 1)
            val array = JsonParser.parseString(json).asJsonArray

            for (element in array) {
                try {
                    val obj = element.asJsonObject
                    val label = obj.get("label").asSafeString()
                        .ifEmpty { obj.get("name").asSafeString("unknown") }
                        .ifEmpty { "unknown" }
                    val x = obj.get("x").asSafeFloat()
                        .let { if (it == 0f) obj.get("boundsX").asSafeFloat() else it }
                    val y = obj.get("y").asSafeFloat()
                        .let { if (it == 0f) obj.get("boundsY").asSafeFloat() else it }
                    val width = obj.get("width").asSafeFloat()
                        .let { if (it == 0f) obj.get("w").asSafeFloat() else it }
                    val height = obj.get("height").asSafeFloat()
                        .let { if (it == 0f) obj.get("h").asSafeFloat() else it }
                    val confidence = obj.get("confidence").asSafeFloat(1f)
                        .let { if (it == 0f) obj.get("score").asSafeFloat(1f) else it }
                    items.add(VisionDetectedItem(label, x, y, width, height, confidence))
                } catch (e: Exception) {
                    DiagnosticLogger.warn(TAG, "Skipping malformed item: $element")
                }
            }
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "parseDetectedItems failed: ${e.message}")
        }
        return items
    }

    // ---------------------------------------------------------------------------------------------
    // Defensive JsonElement accessors
    // ---------------------------------------------------------------------------------------------

    private fun JsonElement?.asSafeString(default: String = ""): String {
        val primitive = this as? JsonPrimitive ?: return default
        return try {
            primitive.asString
        } catch (e: Exception) {
            default
        }
    }

    private fun JsonElement?.asSafeFloat(default: Float = 0f): Float {
        val primitive = this as? JsonPrimitive ?: return default
        return try {
            if (primitive.isNumber) primitive.asFloat else primitive.asString.toFloatOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }
}