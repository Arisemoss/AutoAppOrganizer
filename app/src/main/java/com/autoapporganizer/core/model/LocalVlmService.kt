package com.autoapporganizer.core.model

import android.graphics.Bitmap
import android.util.Base64
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * Local/off-premise VLM service using an OpenAI-compatible chat completion endpoint.
 *
 * Typical targets: Ollama (`/v1/chat/completions`), llama.cpp server, vLLM, LM Studio, etc.
 * The service sends a PNG base64 image and a text prompt, then parses the JSON response.
 *
 * Transient failures (connection refused, timeout) are automatically retried with
 * exponential backoff.
 */
class LocalVlmService(private val prefs: PrefsManager) : VisionModelService {

    companion object {
        private const val TAG = "LocalVlmService"
        private const val DEFAULT_ENDPOINT = "http://localhost:11434/v1/chat/completions"
        private const val DEFAULT_MODEL = "llava"
        private const val CONNECT_TIMEOUT_MS = 15_000
        private const val READ_TIMEOUT_MS = 120_000

        /** Maximum retries for transient failures. */
        private const val MAX_RETRIES = 2

        /** Base backoff in ms (doubles each retry). */
        private const val RETRY_BASE_MS = 1_000L
    }

    override val isAvailable: Boolean
        get() = prefs.vlmProvider == PrefsManager.PROVIDER_LOCAL

    override suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult =
        withContext(Dispatchers.IO) {
            if (!isAvailable) {
                DiagnosticLogger.warn(TAG, "Local VLM not selected (provider=${prefs.vlmProvider})")
                return@withContext VisionResult.Error("本地VLM未配置：请先在设置中切换到本地模式")
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

                var lastError: String? = null
                for (attempt in 0..MAX_RETRIES) {
                    if (attempt > 0) {
                        val backoff = RETRY_BASE_MS * (1L shl (attempt - 1))
                        DiagnosticLogger.warn(TAG, "Retry $attempt/$MAX_RETRIES after ${backoff}ms")
                        delay(backoff)
                    }

                    try {
                        val response = sendRequest(endpoint, body, apiKey)
                        return@withContext parseResponse(response)
                    } catch (e: Exception) {
                        lastError = e.message ?: "Unknown error"
                        val isTransient = isTransientError(e)
                        DiagnosticLogger.warn(
                            TAG,
                            "analyze attempt $attempt failed: $lastError (transient=$isTransient)"
                        )
                        if (!isTransient || attempt == MAX_RETRIES) {
                            DiagnosticLogger.error(TAG, "analyze failed after $attempt attempts: $lastError")
                            return@withContext VisionResult.Error(
                                "本地VLM请求失败: $lastError",
                                if (attempt == MAX_RETRIES) e else null
                            )
                        }
                    }
                }

                VisionResult.Error("本地VLM请求失败: $lastError")
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "analyze failed: ${e.message}")
                VisionResult.Error(e.message ?: "Unknown error", e)
            }
        }

    private fun isTransientError(e: Exception): Boolean {
        val msg = e.message ?: return false
        return when {
            msg.contains("refused", ignoreCase = true) -> true
            msg.contains("timeout", ignoreCase = true) -> true
            msg.contains("connect", ignoreCase = true) -> true
            msg.contains("EOF", ignoreCase = true) -> true
            msg.contains("500") || msg.contains("502") || msg.contains("503") -> true
            else -> false
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
                throw IllegalStateException("HTTP $code: ${text.take(300)}")
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
                return VisionResult.Error("本地VLM响应中没有choices")
            }
            val message = choices[0].asJsonObject.getAsJsonObject("message")
            val content = message?.get("content")?.asString?.trim() ?: ""
            if (content.isEmpty()) {
                return VisionResult.Error("本地VLM返回了空内容")
            }
            val items = parseItemsFromText(content)
            VisionResult.Success(items, rawResponse = content)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "parseResponse failed: ${e.message}")
            VisionResult.Error("解析本地VLM响应失败: ${e.message}", e)
        }
    }

    /**
     * Extract detected items from the model's text response.
     * Tries to find a JSON array of objects with label/x/y/width/height/confidence fields.
     */
    private fun parseItemsFromText(text: String): List<VisionDetectedItem> {
        val items = mutableListOf<VisionDetectedItem>()
        try {
            val start = text.indexOf('[')
            val end = text.lastIndexOf(']')
            if (start < 0 || end < 0 || end <= start) {
                DiagnosticLogger.debug(TAG, "No JSON array found in text (first 100 chars: ${text.take(100)})")
                return items
            }
            val json = text.substring(start, end + 1)
            val array = JsonParser.parseString(json).asJsonArray
            for (element in array) {
                try {
                    val obj = element.asJsonObject
                    val label = (obj.get("label")?.asString ?: obj.get("name")?.asString ?: "unknown")
                    val x = (obj.get("x")?.asFloat ?: obj.get("boundsX")?.asFloat ?: 0f)
                    val y = (obj.get("y")?.asFloat ?: obj.get("boundsY")?.asFloat ?: 0f)
                    val width = (obj.get("width")?.asFloat ?: obj.get("w")?.asFloat ?: 60f)
                    val height = (obj.get("height")?.asFloat ?: obj.get("h")?.asFloat ?: 60f)
                    val confidence = (obj.get("confidence")?.asFloat ?: obj.get("score")?.asFloat ?: 0.8f)
                    if (width > 0 && height > 0) {
                        items.add(VisionDetectedItem(label, x, y, width, height, confidence))
                    }
                } catch (_: Exception) { /* skip malformed items */ }
            }
        } catch (_: Exception) { /* non-JSON response, treat as empty items */ }
        return items
    }
}