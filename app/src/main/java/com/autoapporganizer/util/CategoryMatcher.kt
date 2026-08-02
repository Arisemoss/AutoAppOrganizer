package com.autoapporganizer.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStream
import java.io.InputStreamReader

/**
 * 分类匹配工具类
 *
 * 主构造器 [CategoryMatcher(Context)] 从应用的 assets 加载 `categories.json`。
 * 测试用的 secondary constructor [CategoryMatcher(InputStream)] 接收任意输入流，
 * 让单元测试可以注入测试 JSON 而无需 Robolectric 或 mock AssetManager。
 *
 * 匹配优先级（从强到弱）：
 *  1. **包名匹配** — packageName 是系统提供的稳定证据（如 `com.tencent.mm`），
 *     且 `categories.json` 里已包含大量包名片段关键词，因此最先尝试它。
 *  2. **名称包含匹配** — 图标的可见名称（label）包含关键词（历史行为，顺序敏感）。
 *  3. **模糊匹配** — 名称与关键词的归一化编辑距离足够近时（错字、大小写、多余
 *     空白/标点），按相似度兜底归类；相似度不足（拿不准）时归入「其他」，
 *     绝不硬塞进错误的文件夹。
 */
class CategoryMatcher private constructor(
    private val context: Context?,
    private val injectedStream: InputStream?
) {
    /** Production constructor: load categories from the app's asset manager. */
    constructor(context: Context) : this(context, injectedStream = null)

    /**
     * Test-friendly constructor: load categories from [stream] instead of the asset
     * manager. The stream is consumed and closed by the loader.
     */
    constructor(stream: InputStream) : this(context = null, injectedStream = stream)

    /**
     * 归一化相似度达到该值（0..1）时，模糊匹配才认为两者是同一个应用。
     * 低于此值的输入一律归入「其他」，避免把不相关的应用硬塞进文件夹。
     */
    private val FUZZY_MATCH_THRESHOLD = 0.85f

    private val categories: Map<String, List<String>> by lazy { loadCategories() }

    private fun loadCategories(): Map<String, List<String>> {
        return try {
            val stream = injectedStream ?: context!!.assets.open("categories.json")
            val reader = InputStreamReader(stream)
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            val result: Map<String, List<String>> = Gson().fromJson(reader, type)
            injectedStream?.close()
            result
        } catch (e: Exception) {
            emptyMap()
        }
    }

    /**
     * 根据应用名称匹配分类（向后兼容：不提供包名时只按名称匹配）。
     */
    fun matchCategory(appName: String?): String = matchCategory(appName, packageName = null)

    /**
     * 根据应用名称与包名匹配分类。
     *
     * 匹配是顺序敏感的：遍历 [categories] 时第一个命中即返回，因此更具体的分类
     * （如「音乐」含 `qqmusic`）必须在 JSON 里排在更宽泛的（如「社交」含 `qq`）之前，
     * 否则 `com.tencent.qqmusic` 会被误分到「社交」。这一不变量由
     * `CategoryMatcherTest.qq_music_must_not_match_social_first` 守护。
     *
     * @param appName     图标的可见名称（可能为空或来自 VLM 的识别结果）。
     * @param packageName 系统无障碍树提供的包名（可能为空），是最强的分类证据。
     */
    fun matchCategory(appName: String?, packageName: String?): String {
        if (appName.isNullOrEmpty() && packageName.isNullOrEmpty()) {
            return "其他"
        }

        // 1) 包名优先：系统提供的证据比 VLM/名称识别更可靠。
        val pkg = packageName?.takeIf { it.isNotBlank() }?.let { normalize(it) }
        if (!pkg.isNullOrEmpty()) {
            for ((category, keywords) in categories) {
                for (keyword in keywords) {
                    val k = normalize(keyword)
                    if (k.isNotEmpty() && pkg.contains(k)) {
                        return category
                    }
                }
            }
        }

        val name = appName?.takeIf { it.isNotBlank() }?.let { normalize(it) }
        if (name.isNullOrEmpty()) {
            return "其他"
        }

        // 2) 名称包含匹配（历史行为）。
        for ((category, keywords) in categories) {
            for (keyword in keywords) {
                val k = normalize(keyword)
                if (k.isNotEmpty() && name.contains(k)) {
                    return category
                }
            }
        }

        // 3) 模糊匹配：处理错字 / 大小写 / 多余空白标点等轻微变体。
        //    只对足够长的输入启用（短字符串的编辑距离没有判别力），
        //    且只接受高相似度；低于阈值（拿不准）时返回「其他」而不是硬塞。
        if (name.length >= FUZZY_MIN_INPUT_LENGTH) {
            var bestCategory: String? = null
            var bestSim = 0f
            for ((category, keywords) in categories) {
                for (keyword in keywords) {
                    val k = normalize(keyword)
                    if (k.length < FUZZY_MIN_KEYWORD_LENGTH) continue
                    val sim = similarity(name, k)
                    if (sim > bestSim) {
                        bestSim = sim
                        bestCategory = category
                    }
                }
            }
            if (bestSim >= FUZZY_MATCH_THRESHOLD && bestCategory != null) {
                DiagnosticLogger.debug(
                    TAG,
                    "fuzzy match: '$appName' ~ ${bestCategory} (similarity=$bestSim)"
                )
                return bestCategory
            }
            DiagnosticLogger.debug(
                TAG,
                "no confident match for '$appName' (best similarity=$bestSim)"
            )
        }

        return "其他"
    }

    /**
     * 获取所有分类
     */
    fun getAllCategories(): List<String> {
        return categories.keys.toList() + "其他"
    }

    // ---------------------------------------------------------------------------------------------
    // Normalization & fuzzy matching
    // ---------------------------------------------------------------------------------------------

    /** 模糊匹配只考虑长度不低于该值的输入名。 */
    private val FUZZY_MIN_INPUT_LENGTH = 3

    /** 模糊匹配只考虑长度不低于该值的关键词（太短的关键词没有判别力）。 */
    private val FUZZY_MIN_KEYWORD_LENGTH = 3

    /** 统一小写并去掉所有非字母数字字符（空白、标点、下划线等）。 */
    private fun normalize(s: String): String =
        s.lowercase().filter { it.isLetterOrDigit() }

    /** Levenshtein 编辑距离（迭代 DP，空间 O(len(b))）。 */
    private fun levenshtein(a: String, b: String): Int {
        if (a == b) return 0
        if (a.isEmpty()) return b.length
        if (b.isEmpty()) return a.length
        val dp = IntArray(b.length + 1) { it }
        for (i in 1..a.length) {
            var prev = dp[0]
            dp[0] = i
            for (j in 1..b.length) {
                val tmp = dp[j]
                dp[j] = minOf(
                    dp[j] + 1,
                    dp[j - 1] + 1,
                    prev + if (a[i - 1] == b[j - 1]) 0 else 1
                )
                prev = tmp
            }
        }
        return dp[b.length]
    }

    /** 归一化相似度：1 - 编辑距离 / 较长串长度，取值 0..1。 */
    private fun similarity(a: String, b: String): Float {
        if (a.isEmpty() && b.isEmpty()) return 1f
        val dist = levenshtein(a, b)
        return 1f - dist.toFloat() / maxOf(a.length, b.length)
    }

    companion object {
        private const val TAG = "CategoryMatcher"
    }
}