package com.autoapporganizer.core.plan

import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.util.DiagnosticLogger
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * Parses a vision model's raw text response into a structured [ActionPlan].
 *
 * Expected format (JSON, markdown code fences tolerated):
 * ```json
 * {
 *   "thought": "I see a folder named Games and the Chrome icon. I should drag Chrome into the Games folder.",
 *   "actions": [
 *     {"type": "drag", "fromX": 120, "fromY": 400, "toX": 300, "toY": 400, "durationMs": 800},
 *     {"type": "wait", "ms": 500},
 *     {"type": "complete"}
 *   ]
 * }
 * ```
 *
 * Coordinates may be given as absolute pixels or as normalized ratios (0..1).
 * Ratios are scaled by the supplied [screenBounds].
 */
object VisionPlanner {

    private const val TAG = "VisionPlanner"

    /**
     * Parse [rawResponse] into an [ActionPlan].
     *
     * @param rawResponse the VLM response; may contain markdown fences
     * @param screenBounds current screen bounds used to normalize ratio coordinates
     * @return parsed plan, or a plan containing a single [Action.Complete] on error
     */
    fun parse(rawResponse: String, screenBounds: Rect): ActionPlan {
        return try {
            val json = extractJson(rawResponse)
            val thought = safeString(json.get("thought"), "No reasoning provided").trim()
            val actionsArray = json.getAsJsonArray("actions") ?: JsonArray()
            val actions = actionsArray.mapNotNull { parseAction(it, screenBounds) }
            ActionPlan(thought, actions)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Failed to parse plan: ${e.message}")
            ActionPlan("Parse error: ${e.message}", emptyList())
        }
    }

    private fun extractJson(raw: String): JsonObject {
        val cleaned = raw.trim()
        val fenced = Regex("```(?:json)?\\s*([\\s\\S]*?)```").find(cleaned)?.groupValues?.get(1)
        val candidate = (fenced ?: cleaned).trim()
        return JsonParser.parseString(candidate).asJsonObject
    }

    private fun parseAction(element: JsonElement, screenBounds: Rect): Action? {
        if (!element.isJsonObject) return null
        val obj = element.asJsonObject
        val type = obj.get("type")?.asString?.lowercase() ?: return null

        val width = screenBounds.width().toFloat().coerceAtLeast(1f)
        val height = screenBounds.height().toFloat().coerceAtLeast(1f)

        fun resolveX(raw: JsonElement?): Float {
            val v = safeFloat(raw) ?: return 0f
            return if (v in 0f..1f) screenBounds.left + v * width else v
        }

        fun resolveY(raw: JsonElement?): Float {
            val v = safeFloat(raw) ?: return 0f
            return if (v in 0f..1f) screenBounds.top + v * height else v
        }

        return try {
            when (type) {
                "click" -> Action.Click(
                    resolveX(obj.get("x")),
                    resolveY(obj.get("y"))
                )
                "longpress", "long_press" -> Action.LongPress(
                    resolveX(obj.get("x")),
                    resolveY(obj.get("y")),
                    safeLong(obj.get("durationMs"), 600L)
                )
                "drag" -> Action.Drag(
                    resolveX(obj.get("fromX")),
                    resolveY(obj.get("fromY")),
                    resolveX(obj.get("toX")),
                    resolveY(obj.get("toY")),
                    safeLong(obj.get("durationMs"), 800L)
                )
                "swipe" -> Action.Swipe(
                    resolveX(obj.get("fromX")),
                    resolveY(obj.get("fromY")),
                    resolveX(obj.get("toX")),
                    resolveY(obj.get("toY")),
                    safeLong(obj.get("durationMs"), 300L)
                )
                "type" -> Action.Type(safeString(obj.get("text"), ""))
                "wait" -> Action.Wait(safeLong(obj.get("ms"), 500L))
                "home" -> Action.Home
                "back" -> Action.Back
                "complete" -> Action.Complete
                else -> {
                    DiagnosticLogger.warn(TAG, "Unknown action type: $type")
                    null
                }
            }
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Error parsing action ($type): ${e.message}")
            null
        }
    }

    private fun safeFloat(element: JsonElement?): Float? {
        if (element == null || element.isJsonNull) return null
        return try {
            when {
                element.isJsonPrimitive -> element.asFloat
                else -> element.asString.toFloatOrNull()
            }
        } catch (e: Exception) {
            null
        }
    }

    private fun safeLong(element: JsonElement?, default: Long = 0L): Long {
        if (element == null || element.isJsonNull) return default
        return try {
            when {
                element.isJsonPrimitive && element.asJsonPrimitive.isNumber -> element.asLong
                element.isJsonPrimitive -> element.asString.toLongOrNull() ?: default
                else -> default
            }
        } catch (e: Exception) {
            default
        }
    }

    private fun safeString(element: JsonElement?, default: String = ""): String {
        if (element == null || element.isJsonNull) return default
        return try {
            if (element.isJsonPrimitive) element.asString else default
        } catch (e: Exception) {
            default
        }
    }
}
