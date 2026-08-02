package com.autoapporganizer.core.feedback

import android.content.Context
import android.content.SharedPreferences
import com.autoapporganizer.core.classification.ClassifiedApp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * 分类结果缓存 —— 参考 Operit 的知识图谱更新机制。
 *
 * 将 AI 分类结果持久化到本地，下次整理时作为先验知识：
 * 1. 缓存高置信度分类结果，加速后续整理
 * 2. 支持用户纠正（手动修改分类后更新缓存）
 * 3. 缓存有过期时间，避免过时数据影响分类
 *
 * 存储格式（SharedPreferences JSON）：
 * {
 *   "com.tencent.mm": {"category": "社交", "confidence": 0.98, "timestamp": 1234567890},
 *   "com.ss.android.ugc.aweme": {"category": "视频", "confidence": 0.95, "timestamp": 1234567890}
 * }
 */
class ClassificationCache(context: Context) {

    companion object {
        private const val PREFS_NAME = "classification_cache"
        private const val KEY_CACHE = "cached_classifications"
        /** 缓存有效期：7 天 */
        private const val CACHE_TTL_MS = 7 * 24 * 60 * 60 * 1000L
    }

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    private val gson = Gson()

    /**
     * 缓存单条分类结果。
     */
    fun cache(label: String, category: String, confidence: Float) {
        val all = loadAll().toMutableMap()
        all[label] = CachedClassification(category, confidence, System.currentTimeMillis())
        saveAll(all)
    }

    /**
     * 批量缓存分类结果。
     */
    fun cacheBatch(apps: List<ClassifiedApp>) {
        val all = loadAll().toMutableMap()
        val now = System.currentTimeMillis()
        for (app in apps) {
            if (app.confidence >= 0.7f) { // 仅缓存高置信度结果
                all[app.label] = CachedClassification(app.category, app.confidence, now)
            }
        }
        saveAll(all)
    }

    /**
     * 查询缓存中的分类（仅返回未过期的结果）。
     *
     * @return 分类名，如果缓存不存在或已过期则返回 null
     */
    fun lookup(label: String): String? {
        val cached = loadAll()[label] ?: return null
        if (System.currentTimeMillis() - cached.timestamp > CACHE_TTL_MS) {
            // 已过期，清除
            val all = loadAll().toMutableMap()
            all.remove(label)
            saveAll(all)
            return null
        }
        return cached.category
    }

    /**
     * 用户纠正：更新缓存中的分类。
     */
    fun correct(label: String, newCategory: String) {
        cache(label, newCategory, 1.0f) // 用户纠正的置信度为 1.0
    }

    /**
     * 清除所有缓存。
     */
    fun clear() {
        prefs.edit().clear().apply()
    }

    /**
     * 获取缓存的分类数量。
     */
    fun size(): Int = loadAll().size

    private fun loadAll(): Map<String, CachedClassification> {
        val json = prefs.getString(KEY_CACHE, "{}") ?: "{}"
        return try {
            val type = object : TypeToken<Map<String, CachedClassification>>() {}.type
            gson.fromJson(json, type) ?: emptyMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    private fun saveAll(map: Map<String, CachedClassification>) {
        prefs.edit().putString(KEY_CACHE, gson.toJson(map)).apply()
    }

    /**
     * 缓存的单条分类记录。
     */
    data class CachedClassification(
        val category: String,
        val confidence: Float,
        val timestamp: Long
    )
}