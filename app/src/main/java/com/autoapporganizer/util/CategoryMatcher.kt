package com.autoapporganizer.util

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

/**
 * 分类匹配工具类
 */
class CategoryMatcher(private val context: Context) {
    
    private val categories: Map<String, List<String>> by lazy {
        loadCategories()
    }
    
    private fun loadCategories(): Map<String, List<String>> {
        return try {
            val inputStream = context.assets.open("categories.json")
            val reader = InputStreamReader(inputStream)
            val type = object : TypeToken<Map<String, List<String>>>() {}.type
            Gson().fromJson(reader, type)
        } catch (e: Exception) {
            emptyMap()
        }
    }
    
    /**
     * 根据应用名称匹配分类
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
}
