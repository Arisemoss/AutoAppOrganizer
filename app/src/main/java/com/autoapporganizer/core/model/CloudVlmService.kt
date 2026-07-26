package com.autoapporganizer.core.model

import android.graphics.Bitmap
import android.util.Base64
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonPrimitive
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

/**
 * A [VisionModelService] backed by a remote vision-language model reached over HTTP.
 *
 * Three providers are supported and are selected via [PrefsManager.vlmProvider]:
 *  - `"openai"` — OpenAI-compatible chat completions (`image_url` content parts).
 *  - `"glm"`    — GLM-4V, same wire format as OpenAI (`image_url` content parts).
 *  - `"gemini"` — Google Gemini `generateContent` (`inline_data` parts).
 *
 * The service is considered available only when a provider other than `"none"` is selected
 * **and** an API key has been configured. All network work runs on [Dispatchers.IO].
 *
 * Transient failures (HTTP 429, 5xx) are automatically retried with exponential backoff.
 *
 * @param prefs User preferences carrying provider/key/endpoint/model configuration.
 */
class CloudVlmService(private val prefs: PrefsManager) : VisionModelService {

    companion object {
        private const val TAG = "CloudVlmService"

        private const val PROVIDER_NONE = "none"
        private const val PROVIDER_OPENAI = "openai"
        private const val PROVIDER_GEMINI = "gemini"
        private const val PROVIDER_GLM = "glm"

        private const val OPENAI_ENDPOINT = "https://api.openai.com/v1/chat/completions"
        private const val GEMINI_ENDPOINT_TEMPLATE =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent"
        private const val GLM_ENDPOINT = "https://open.bigmodel.cn/api/paas/v4/chat/completions"

        private const val DEFAULT_MODEL_OPENAI = "gpt-4o"
        private const val DEFAULT_MODEL_GEMINI = "gemini-2.0-flash"
        private const val DEFAULT_MODEL_GLM = "glm-4v"

        private const val CONNECT_TIMEOUT_MS = 30_000
        private const val READ_TIMEOUT_MS = 60_000

        /** Maximum number of retries for transient failures. */
        private const val MAX_RETRIES = 2

        /** Base backoff in ms (doubles each retry). */
        private const val RETRY_BASE_MS = 1_000L
    }

    override val isAvailable: Boolean
        get() = prefs.vlmProvider != PROVIDER_NONE && prefs.vlmApiKey.isNotEmpty()

    override suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult =
        withContext(Dispatchers.IO) {
            if (!isAvailable) {
                DiagnosticLogger.warn(TAG, "VLM not available (provider=${prefs.vlmProvider})")
                return@withContext VisionResult.Error("VLM未配置：请先设置提供商和API Key")
            }

            val provider = prefs.vlmProvider
            val model = resolveModel(provider)
            val endpoint = resolveEndpoint(provider, model)
            val base64 = encodeBitmapToPngBase64(bitmap)
            DiagnosticLogger.debug(
                TAG,
                "Encoded ${bitmap.width}x${bitmap.height} -> ${base64.length} base64 chars; " +
                    "provider=$provider model=$model"
            )

            val body = buildRequestBody(provider, model, base64, prompt)

            // Retry loop for transient failures
            var lastError: String? = null
            for (attempt in 0..MAX_RETRIES) {
                if (attempt > 0) {
                    val backoff = RETRY_BASE_MS * (1L shl (attempt - 1))
                    DiagnosticLogger.warn(TAG, "Retry $attempt/$MAX_RETRIES after ${backoff}ms")
                    delay(backoff)
                }

                try {
                    val response = sendRequest(endpoint, body, prefs.vlmApiKey)
                    DiagnosticLogger.debug(TAG, "Received response (${response.length} chars)")
                    return@withContext parseResponse(provider, response)
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
                            "VLM请求失败: $lastError",
                            if (attempt == MAX_RETRIES) e else null
                        )
                    }
                }
            }

            VisionResult.Error("VLM请求失败: $lastError")
        }

    // ---------------------------------------------------------------------------------------------
    // Bitmap encoding
    // ---------------------------------------------------------------------------------------------

    private fun encodeBitmapToPngBase64(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
    }

    // ---------------------------------------------------------------------------------------------
    // Endpoint / model resolution
    // ---------------------------------------------------------------------------------------------

    private fun resolveModel(provider: String): String {
        val configured = prefs.vlmModel
        if (configured.isNotEmpty()) return configured
        return when (provider) {
            PROVIDER_OPENAI -> DEFAULT_MODEL_OPENAI
            PROVIDER_GEMINI -> DEFAULT_MODEL_GEMINI
            PROVIDER_GLM -> DEFAULT_MODEL_GLM
            else -> DEFAULT_MODEL_OPENAI
        }
    }

    private fun resolveEndpoint(provider: String, model: String): String {
        val custom = prefs.vlmEndpoint
        if (custom.isNotEmpty()) return custom
        return when (provider) {
            PROVIDER_OPENAI -> OPENAI_ENDPOINT
            PROVIDER_GEMINI -> GEMINI_ENDPOINT_TEMPLATE.format(model)
            PROVIDER_GLM -> GLM_ENDPOINT
            else -> OPENAI_ENDPOINT
        }
    }

    /**
     * Determine if an error is transient (worth retrying).
     * Transient: HTTP 429 (rate limit), 5xx (server error), connection timeout.
     * Non-transient: HTTP 4xx (auth, bad request), parse errors.
     */
    private fun isTransientError(e: Exception): Boolean {
        val msg = e.message ?: return false
        return when {
            msg.contains("429") -> true
            msg.contains("500") || msg.contains("502") || msg.contains("503") -> true
            msg.contains("504") -> true
            msg.contains("timeout", ignoreCase = true) -> true
            msg.contains("connect", ignoreCase = true) -> true
            msg.contains("EOF", ignoreCase = true) -> true
            else -> false
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Request body building
    // ---------------------------------------------------------------------------------------------

    private fun buildRequestBody(provider: String, model: String, base64: String, prompt: String): String {
        return when (provider) {
            PROVIDER_GEMINI -> buildGeminiBody(prompt, base64)
            else -> buildOpenAiCompatibleBody(model, prompt, base64)
        }
    }

    private fun buildOpenAiCompatibleBody(model: String, prompt: String, base64: String): String {
        val root = JsonObject()
        root.addProperty("model", model)
        root.addProperty("max_tokens", 2000)

        val messages = JsonArray()
        val userMessage = JsonObject()
        userMessage.addProperty("role", "user")

        val content = JsonArray()
        val textPart = JsonObject().apply {
            addProperty("type", "text")
            addProperty("text", prompt)
        }
        val imagePart = JsonObject().apply {
            addProperty("type", "image_url")
            val imageUrl = JsonObject().apply {
                addProperty("url", "data:image/png;base64,$base64")
            }
            add("image_url", imageUrl)
        }
        content.add(textPart)
        content.add(imagePart)
        userMessage.add("content", content)

        messages.add(userMessage)
        root.add("messages", messages)
        return root.toString()
    }

    private fun buildGeminiBody(prompt: String, base64: String): String {
        val root = JsonObject()

        val contents = JsonArray()
        val content = JsonObject()

        val parts = JsonArray()
        val textPart = JsonObject().apply { addProperty("text", prompt) }
        val imagePart = JsonObject().apply {
            val inlineData = JsonObject().apply {
                addProperty("mime_type", "image/png")
                addProperty("data", base64)
            }
            add("inline_data", inlineData)
        }
        parts.add(textPart)
        parts.add(imagePart)

        content.add("parts", parts)
        contents.add(content)
        root.add("contents", contents)
        return root.toString()
    }

    // ---------------------------------------------------------------------------------------------
    // HTTP transport
    // ---------------------------------------------------------------------------------------------

    private fun sendRequest(endpoint: String, body: String, apiKey: String): String {
        val url = URL(endpoint)
        val connection = (url.openConnection() as HttpURLConnection).apply {
            requestMethod = "POST"
            connectTimeout = CONNECT_TIMEOUT_MS
            readTimeout = READ_TIMEOUT_MS
            doOutput = true
            setRequestProperty("Content-Type", "application/json; charset=utf-8")
            setRequestProperty("Accept", "application/json")
        }

        when (prefs.vlmProvider) {
            PROVIDER_GEMINI -> connection.setRequestProperty("x-goog-api-key", apiKey)
            else -> connection.setRequestProperty("Authorization", "Bearer $apiKey")
        }

        try {
            connection.outputStream.use { os ->
                OutputStreamWriter(os, Charsets.UTF_8).use { writer ->
                    writer.write(body)
                    writer.flush()
                }
            }

            val code = connection.responseCode
            val stream = if (code in 200..299) connection.inputStream else connection.errorStream
            val responseText = stream?.bufferedReader(Charsets.UTF_8)?.use { it.readText() } ?: ""

            if (code !in 200..299) {
                val detail = responseText.take(500)
                DiagnosticLogger.error(TAG, "HTTP $code: $detail")
                throw RuntimeException("HTTP $code: $detail")
            }
            return responseText
        } finally {
            connection.disconnect()
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Response parsing
    // ---------------------------------------------------------------------------------------------

    private fun parseResponse(provider: String, response: String): VisionResult {
        return try {
            val root = JsonParser.parseString(response).asJsonObject
            val text = extractContentText(provider, root)
            if (text.isBlank()) {
                DiagnosticLogger.warn(TAG, "Empty content text in VLM response")
                return VisionResult.Error("VLM返回了空内容")
            }
            val items = parseDetectedItems(text)
            DiagnosticLogger.debug(TAG, "Parsed ${items.size} detected items from response")
            VisionResult.Success(items, text)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "parseResponse failed: ${e.message}")
            VisionResult.Error("解析VLM响应失败: ${e.message}", e)
        }
    }

    private fun extractContentText(provider: String, root: JsonObject): String {
        return when (provider) {
            PROVIDER_GEMINI -> {
                val candidates = root.getAsJsonArray("candidates") ?: return ""
                val first = candidates.firstOrNull()?.asJsonObject ?: return ""
                val content = first.getAsJsonObject("content") ?: return ""
                val parts = content.getAsJsonArray("parts") ?: return ""
                val firstPart = parts.firstOrNull()?.asJsonObject ?: return ""
                firstPart.get("text").asSafeString()
            }
            else -> {
                val choices = root.getAsJsonArray("choices") ?: return ""
                val first = choices.firstOrNull()?.asJsonObject ?: return ""
                val message = first.getAsJsonObject("message") ?: return ""
                message.get("content").asSafeString()
            }
        }
    }

    /**
     * Extracts the JSON array of detected items embedded in the model's free-text answer.
     *
     * The VLM is instructed to emit a JSON array; this method locates the first `[` and the
     * last `]` in the text to tolerate surrounding prose/markdown fences. Field names are
     * matched leniently (e.g. `label`/`name`, `x`/`boundsX`).
     */
    private fun parseDetectedItems(text: String): List<VisionDetectedItem> {
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

                    if (width > 0 && height > 0) {
                        items.add(VisionDetectedItem(label, x, y, width, height, confidence))
                    } else {
                        DiagnosticLogger.debug(TAG, "Skipping item with invalid dimensions: $label")
                    }
                } catch (e: Exception) {
                    DiagnosticLogger.debug(TAG, "Skipping malformed item: ${e.message}")
                }
            }
        } catch (e: Exception) {
            DiagnosticLogger.warn(TAG, "parseDetectedItems failed: ${e.message}")
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