package com.autoapporganizer.core.classification

import com.autoapporganizer.core.model.VisionModelService
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser

/**
 * AI 驱动的语义分类器 —— 参考 Operit MemoryLibrary 的 autoCategorizeMemories 设计。
 *
 * 核心思路：
 * 1. 将桌面图标列表 + 可用分类列表发送给 VLM
 * 2. VLM 基于语义理解（而非关键词匹配）进行归类
 * 3. 优先复用现有分类，必要时创建新分类
 * 4. 每个分类决策附带置信度评分
 *
 * 与 [CategoryMatcher] 的关系：
 * - SemanticClassifier 是主要分类器（AI 语义理解）
 * - CategoryMatcher 作为兜底（低置信度时回退）
 * - ClassificationFusion 负责两者的融合
 */
class SemanticClassifier(
    private val vlm: VisionModelService,
    private val categoryMatcher: CategoryMatcher
) {
    companion object {
        private const val TAG = "SemanticClassifier"

        /**
         * 构建分类 prompt 模板。
         *
         * 参考 Operit FunctionalPrompts.buildMemoryAutoCategorizePrompt 的设计：
         * - 传入已有的分类列表，AI 优先复用
         * - 要求返回结构化 JSON
         * - 每个分类附带置信度
         */
        fun buildClassifyPrompt(
            icons: List<ScreenElement>,
            existingCategories: List<String>
        ): String {
            val iconList = icons.joinToString("\n") { el ->
                "- ${el.label}" + (el.packageName?.let { " (包名: $it)" } ?: "")
            }

            val categoriesText = if (existingCategories.isEmpty()) {
                "无已有分类，请根据应用语义自由创建分类"
            } else {
                "已有分类：${existingCategories.joinToString("、")}"
            }

            return """
你是一个 Android 桌面应用分类专家。请根据应用名称和功能语义，将以下桌面图标分配到合适的分类中。

$categoriesText

规则：
1. 优先使用已有分类，只有当应用确实不适合任何已有分类时才创建新分类
2. 每个分类决策需要给出置信度 (0.0~1.0)，置信度低于 0.5 的标记为 uncertain
3. 如果某个分类只有 1 个应用，直接归入"其他"分类
4. 不要使用 "工具" 作为兜底分类，不确定的归入 "其他"

桌面图标列表：
$iconList

返回严格 JSON（不要 markdown 代码块，不要其他文字）：
{
  "thought": "整体分类思路（一句话）",
  "categories": [
    {
      "category": "分类名",
      "isNewCategory": false,
      "apps": [
        {"label": "应用名", "confidence": 0.95, "reasoning": "属于社交类应用"}
      ]
    }
  ],
  "uncertain": [
    {"label": "不确定的应用名", "confidence": 0.3, "reasoning": "不确定原因"}
  ]
}
""".trimIndent()
        }
    }

    /**
     * 对桌面图标列表进行 AI 语义分类。
     *
     * @param icons 桌面图标元素列表（来自 PerceptionFusion 融合结果）
     * @param screenshot 桌面截图（用于视觉上下文，当前版本暂不传递）
     * @return 分类响应，如果 VLM 不可用或调用失败则返回 null
     */
    suspend fun classify(
        icons: List<ScreenElement>,
        screenshot: android.graphics.Bitmap? = null
    ): ClassificationResponse? {
        if (!vlm.isAvailable) {
            DiagnosticLogger.debug(TAG, "VLM not available, skipping semantic classification")
            return null
        }

        if (icons.isEmpty()) {
            DiagnosticLogger.debug(TAG, "No icons to classify")
            return null
        }

        val existingCategories = categoryMatcher.getAllCategories()
            .filter { it != "其他" } // 排除"其他"，让 AI 自行判断

        val prompt = buildClassifyPrompt(icons, existingCategories)
        DiagnosticLogger.debug(TAG, "Sending classification prompt (${prompt.length} chars, ${icons.size} icons)")

        return try {
            // 使用截图作为视觉上下文（如果可用），否则仅文本分类
            val result = if (screenshot != null) {
                vlm.analyze(screenshot, prompt)
            } else {
                // 无截图时，创建一个最小的占位 bitmap 用于 VLM 调用
                // 大多数 VLM 要求传入图片，但分类主要依赖文本 prompt
                val placeholder = android.graphics.Bitmap.createBitmap(
                    64, 64, android.graphics.Bitmap.Config.ARGB_8888
                ).apply {
                    eraseColor(android.graphics.Color.DKGRAY)
                }
                vlm.analyze(placeholder, prompt)
            }

            when (result) {
                is VisionResult.Success -> {
                    parseClassificationResponse(result.text)
                }
                is VisionResult.Error -> {
                    DiagnosticLogger.warn(TAG, "VLM classification failed: ${result.message}")
                    null
                }
            }
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "SemanticClassifier error: ${e.message}")
            null
        }
    }

    /**
     * 解析 VLM 返回的分类 JSON。
     *
     * 参考 Operit MemoryLibrary.parseAndApplyCategorization 的容错策略：
     * - 容忍 markdown 代码块包裹
     * - 逐个字段安全解析，跳过格式错误的项
     */
    private fun parseClassificationResponse(raw: String): ClassificationResponse? {
        return try {
            val json = extractJson(raw)
            val thought = json.get("thought")?.asString
            val categories = parseCategories(json.getAsJsonArray("categories"))
            val uncertain = parseUncertainApps(json.getAsJsonArray("uncertain"))

            if (categories.isEmpty() && uncertain.isEmpty()) {
                DiagnosticLogger.warn(TAG, "Parsed empty classification response")
                return null
            }

            DiagnosticLogger.debug(
                TAG,
                "Parsed ${categories.size} categories, ${uncertain.size} uncertain, " +
                    "total apps: ${categories.sumOf { it.apps.size }}"
            )
            ClassificationResponse(categories, uncertain, thought)
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Failed to parse classification response: ${e.message}")
            null
        }
    }

    private fun extractJson(raw: String): JsonObject {
        val cleaned = raw.trim()
        // 容忍 markdown 代码块
        val fenced = Regex("```(?:json)?\\s*([\\s\\S]*?)```").find(cleaned)?.groupValues?.get(1)
        val candidate = (fenced ?: cleaned).trim()
        return JsonParser.parseString(candidate).asJsonObject
    }

    private fun parseCategories(array: JsonArray?): List<ClassificationResult> {
        if (array == null) return emptyList()
        val results = mutableListOf<ClassificationResult>()

        for (element in array) {
            try {
                val obj = element.asJsonObject
                val category = obj.get("category")?.asString ?: continue
                val isNew = obj.get("isNewCategory")?.asBoolean ?: false
                val apps = parseApps(obj.getAsJsonArray("apps"), category)
                if (apps.isNotEmpty()) {
                    results.add(ClassificationResult(category, apps, isNew))
                }
            } catch (e: Exception) {
                DiagnosticLogger.warn(TAG, "Skipping malformed category: $element")
            }
        }
        return results
    }

    private fun parseApps(array: JsonArray?, category: String): List<ClassifiedApp> {
        if (array == null) return emptyList()
        val apps = mutableListOf<ClassifiedApp>()

        for (element in array) {
            try {
                val obj = element.asJsonObject
                val label = obj.get("label")?.asString ?: continue
                val confidence = obj.get("confidence")?.asFloat ?: 1f
                val reasoning = obj.get("reasoning")?.asString
                apps.add(ClassifiedApp(label, category, confidence, reasoning))
            } catch (e: Exception) {
                DiagnosticLogger.warn(TAG, "Skipping malformed app: $element")
            }
        }
        return apps
    }

    private fun parseUncertainApps(array: JsonArray?): List<ClassifiedApp> {
        if (array == null) return emptyList()
        val apps = mutableListOf<ClassifiedApp>()
        for (element in array) {
            try {
                val obj = element.asJsonObject
                val label = obj.get("label")?.asString ?: continue
                val confidence = obj.get("confidence")?.asFloat ?: 0.3f
                val reasoning = obj.get("reasoning")?.asString
                apps.add(ClassifiedApp(label, "其他", confidence, reasoning))
            } catch (e: Exception) {
                DiagnosticLogger.warn(TAG, "Skipping malformed uncertain app: $element")
            }
        }
        return apps
    }
}