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
 * 匹配规则：
 * - 顺序敏感：第一个命中即返回
 * - 更具体的分类（如「音乐」含 `qqmusic`）必须排在更宽泛的（如「社交」含 `qq`）之前
 * - 大小写不敏感
 * - 支持包名和应用名匹配
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

    private val categories: Map<String, List<String>> by lazy { loadCategories() }

    private fun loadCategories(): Map<String, List<String>> {
        return try {
            val stream = injectedStream ?: context!!.assets.open("categories.json")
            val reader = InputStreamReader(stream)
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            val result: Map<String, List<String>> = Gson().fromJson(reader, type)
            injectedStream?.close()
            DiagnosticLogger.info("CategoryMatcher", "Loaded ${result.size} categories, ${result.values.sumOf { it.size }} keywords")
            result
        } catch (e: Exception) {
            DiagnosticLogger.error("CategoryMatcher", "Failed to load categories: ${e.message}")
            emptyMap()
        }
    }

    /**
     * 根据应用名称匹配分类。
     *
     * 匹配是顺序敏感的：遍历 [categories] 时第一个命中即返回，因此更具体的分类
     * （如「音乐」含 `qqmusic`）必须在 JSON 里排在更宽泛的（如「社交」含 `qq`）之前，
     * 否则 `com.tencent.qqmusic` 会被误分到「社交」。这一不变量由
     * `CategoryMatcherTest.qq_music_must_not_match_social_first` 守护。
     */
    fun matchCategory(appName: String?): String {
        if (appName.isNullOrEmpty()) {
            return "其他"
        }

        val lowerName = appName.lowercase()

        for ((category, keywords) in categories) {
            for (keyword in keywords) {
                if (lowerName.contains(keyword.lowercase())) {
                    return category
                }
            }
        }

        return "其他"
    }

    /**
     * 获取所有分类
     */
    fun getAllCategories(): List<String> {
        return categories.keys.toList() + "其他"
    }

    /**
     * 获取分类数量
     */
    fun getCategoryCount(): Int = categories.size

    /**
     * 获取总关键词数量
     */
    fun getKeywordCount(): Int = categories.values.sumOf { it.size }

    /**
     * 获取指定分类的关键词列表
     */
    fun getKeywordsForCategory(category: String): List<String> {
        return categories[category] ?: emptyList()
    }

    /**
     * 检查分类是否存在
     */
    fun hasCategory(category: String): Boolean {
        return categories.containsKey(category)
    }

    /**
     * 获取所有分类及其关键词数量（用于调试/统计）
     */
    fun getCategoryStats(): Map<String, Int> {
        return categories.mapValues { it.value.size }
    }
}
