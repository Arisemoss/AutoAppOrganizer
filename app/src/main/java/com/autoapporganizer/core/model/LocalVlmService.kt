package com.autoapporganizer.core.model

import android.graphics.Bitmap
import android.util.Base64
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Local/off-premise VLM service using an OpenAI-compatible chat completion endpoint.
 *
 * Typical targets: Ollama (`/v1/chat/completions`), llama.cpp server, vLLM, LM Studio, etc.
 * The service sends a PNG base64 image and a text prompt, then parses the JSON response.
 */
class LocalVlmService(private val prefs: PrefsManager) : VisionModelService {

    companion object {
        private const val TAG = "LocalVlmService"
        private const val DEFAULT_ENDPOINT = "http://localhost:11434/v1/chat/completions"
        private const val DEFAULT_MODEL = "llava"
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val READ_TIMEOUT_MS = 120_000
    }

    override val isAvailable: Boolean
        get() = prefs.vlmProvider == PrefsManager.PROVIDER_LOCAL

    override suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult =
        withContext(Dispatchers.IO) {
            if (!isAvailable) {
                DiagnosticLogger.warn(TAG, "Local VLM not selected (provider=${prefs.vlmProvider})")
                return@withContext VisionResult.Error("Local VLM not configured")
            }
            try {
                val endpoint = prefs.vlmEndpoint.ifEmpty { DEFAULT_ENDPOINT }
                val model = prefs.vlmModel.ifEmpty { DEFAULT_MODEL }
                val apiKey = prefs.vlmApiKey

                val base64 = encodeBitmapToPngBase64(bitmap)
                DiagnosticLogger.debug(
                    TAG,
                    "Analyzing ${bitmap.width}x${bitmap.height} with local model=$model endpoint=$endpoint"
                )

                val body = buildRequestBody(model, prompt, base64)
                val response = sendRequest(endpoint, body, apiKey)
                parseResponse(response)
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "analyze failed: ${e.message}")
                VisionResult.Error(e.message ?: "Unknown error", e)
            }
        }

    private fun encodeBitmapToPngBase64(bitmap: Bitmap): String {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return Base64.encodeToString(stream.toByteArray(), Base64.NO_WRAP)
    }

    private fun buildRequestBody(model: String, prompt: String, base64Image: String): String {
        val content = JsonArray().apply {
            add(JsonObject().apply { addProperty("type", "text"); addProperty("text", prompt) })
            add(JsonObject().apply {
                addProperty("type", "image_url")
                add("image_url", JsonObject().apply { addProperty("url", "data:image/png;base64,$base64Image") })
            })
        }
        val message = JsonObject().apply {
            addProperty("role", "user")
            add("content", content)
        }
        return JsonObject().apply {
            addProperty("model", model)
            add("messages", JsonArray().apply { add(message) })
            addProperty("temperature", 0.2)
            addProperty("max_tokens", 2048)
        }.toString()
    }

    private fun sendRequest(endpoint: String, body: String, apiKey: String): String {
        val url = URL(endpoint)
        val conn = url.openConnection() as HttpURLConnection
        return try {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json")
            conn.setRequestProperty("Accept", "application/json")
            if (apiKey.isNotBlank()) {
                conn.setRequestProperty("Authorization", "Bearer $apiKey")
            }
            conn.connectTimeout = CONNECT_TIMEOUT_MS
            conn.readTimeout = READ_TIMEOUT_MS
            conn.doOutput = true
            conn.outputStream.use { it.write(body.toByteArray(Charsets.UTF_8)) }

            val code = conn.responseCode
            val input = if (code in 200..299) conn.inputStream else conn.errorStream
            val text = input.bufferedReader(Charsets.UTF_8).use { it.readText() }
            if (code !in 200..299) {
                throw IllegalStateException("HTTP $code: $text")
            }
            text
        } finally {
            conn.disconnect()
        }
    }

    private fun parseResponse(response: String): VisionResult {
        return try {
            val root = JsonParser.parseString(response).asJsonObject
            val choices = root.getAsJsonArray("choices")
            if (choices == null || choices.size() == 0) {
                return VisionResult.Error("No choices in response")
            }
            val message = choices[0].asJsonObject.getAsJsonObject("message")
            val content = message?.get("content")?.asString?.trim() ?: ""
            if (content.isEmpty()) {
                return VisionResult.Error("Empty content in response")
            }
            val items = parseDetectedItems(content)
            VisionResult.Success(items, response)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "parseResponse failed: ${e.message}")
            VisionResult.Error("Parse error: ${e.message}", e)
        }
    }

    /**
     * Extract the JSON array of detected items embedded in the model's free-text answer.
     * Mirrors [CloudVlmService.parseDetectedItems] for the local provider.
     */
    private fun parseDetectedItems(text: String): List<VisionDetectedItem> {
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

    private fun com.google.gson.JsonElement?.asSafeString(default: String = ""): String {
        val primitive = this as? com.google.gson.JsonPrimitive ?: return default
        return try {
            primitive.asString
        } catch (e: Exception) {
            default
        }
    }

    private fun com.google.gson.JsonElement?.asSafeFloat(default: Float = 0f): Float {
        val primitive = this as? com.google.gson.JsonPrimitive ?: return default
        return try {
            if (primitive.isNumber) primitive.asFloat else primitive.asString.toFloatOrNull() ?: default
        } catch (e: Exception) {
            default
        }
    }
}
