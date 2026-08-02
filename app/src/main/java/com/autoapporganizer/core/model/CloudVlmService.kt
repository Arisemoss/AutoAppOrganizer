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
    }

    override val isAvailable: Boolean
        get() = prefs.vlmProvider != PROVIDER_NONE && prefs.vlmApiKey.isNotEmpty()

    override suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult =
        withContext(Dispatchers.IO) {
            if (!isAvailable) {
                DiagnosticLogger.warn(TAG, "VLM not available (provider=${prefs.vlmProvider})")
                return@withContext VisionResult.Error("VLM not available")
            }
            try {
                val provider = prefs.vlmProvider
                val model = resolveModel(provider)
                val endpoint = resolveEndpoint(provider, model)
                val base64 = encodeBitmapToPngBase64(bitmap)
                DiagnosticLogger.debug(
                    TAG,
                    "Encoded ${bitmap.width}x${bitmap.height} bitmap -> ${base64.length} base64 chars; " +
                        "provider=$provider model=$model endpoint=$endpoint"
                )

                val body = buildRequestBody(provider, model, base64, prompt)
                val response = sendRequest(endpoint, body, prefs.vlmApiKey)
                DiagnosticLogger.debug(TAG, "Received response (${response.length} chars)")

                parseResponse(provider, response)
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "analyze failed: ${e.message}")
                VisionResult.Error(e.message ?: "Unknown error", e)
            }
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
        // A custom endpoint always wins, regardless of provider.
        val custom = prefs.vlmEndpoint
        if (custom.isNotEmpty()) return custom
        return when (provider) {
            PROVIDER_OPENAI -> OPENAI_ENDPOINT
            PROVIDER_GEMINI -> GEMINI_ENDPOINT_TEMPLATE.format(model)
            PROVIDER_GLM -> GLM_ENDPOINT
            else -> OPENAI_ENDPOINT
        }
    }

    // ---------------------------------------------------------------------------------------------
    // Request body building
    // ---------------------------------------------------------------------------------------------

    private fun buildRequestBody(provider: String, model: String, base64: String, prompt: String): String {
        return when (provider) {
            PROVIDER_GEMINI -> buildGeminiBody(prompt, base64)
            else -> buildOpenAiCompatibleBody(model, prompt, base64) // openai + glm share this format
        }
    }

    /**
     * Builds an OpenAI-compatible chat-completions body with multimodal content parts.
     * Used by both the `openai` and `glm` providers.
     */
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

    /**
     * Builds a Gemini `generateContent` body using an `inline_data` part for the image.
     */
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

        // Provider-specific auth. Gemini uses an API-key header (also accepts ?key=...);
        // OpenAI and GLM both use a Bearer token.
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
                DiagnosticLogger.error(TAG, "HTTP $code body=${responseText.take(500)}")
                throw RuntimeException("HTTP $code: ${responseText.take(500)}")
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
            if (text.isEmpty()) {
                DiagnosticLogger.warn(TAG, "Empty content text in response")
                return VisionResult.Error("Empty response content text")
            }
            val items = VisionResponseParser.parseDetectedItems(text)
            DiagnosticLogger.debug(TAG, "Parsed ${items.size} detected items from response")
            VisionResult.Success(items, text)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "parseResponse failed: ${e.message}")
            VisionResult.Error("Parse error: ${e.message}", e)
        }
    }

    /**
     * Pulls the model's textual answer out of the provider-specific response envelope.
     */
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


}
