package com.autoapporganizer.util

import org.junit.Assert.*
import org.junit.Test
import java.io.ByteArrayInputStream

/**
 * Advanced unit tests for [CategoryMatcher].
 */
class CategoryMatcherAdvancedTest {

    private val categoriesJson = """
        {
          "音乐": ["qqmusic", "netease.cloudmusic", "music", "spotify"],
          "视频": ["tiktok", "douyin", "bilibili", "youtube"],
          "社交": ["wechat", "tencent.mm", "tencent.mobileqq", "qq"],
          "购物": ["taobao", "jingdong", "pinduoduo"],
          "工具": ["calculator", "calendar", "clock", "settings"]
        }
    """.trimIndent()

    private fun matcher(): CategoryMatcher =
        CategoryMatcher(ByteArrayInputStream(categoriesJson.toByteArray()))

    @Test
    fun `getCategoryCount returns correct count`() {
        val m = matcher()
        assertEquals(5, m.getCategoryCount())
    }

    @Test
    fun `getKeywordCount returns total keywords`() {
        val m = matcher()
        // 音乐:4 + 视频:4 + 社交:4 + 购物:3 + 工具:4 = 19
        assertEquals(19, m.getKeywordCount())
    }

    @Test
    fun `getKeywordsForCategory returns correct keywords`() {
        val m = matcher()
        val musicKeywords = m.getKeywordsForCategory("音乐")
        assertEquals(4, musicKeywords.size)
        assertTrue(musicKeywords.contains("qqmusic"))
        assertTrue(musicKeywords.contains("spotify"))
    }

    @Test
    fun `getKeywordsForCategory returns empty for unknown category`() {
        val m = matcher()
        val unknown = m.getKeywordsForCategory("未知分类")
        assertTrue(unknown.isEmpty())
    }

    @Test
    fun `hasCategory returns true for existing category`() {
        val m = matcher()
        assertTrue(m.hasCategory("音乐"))
        assertTrue(m.hasCategory("社交"))
    }

    @Test
    fun `hasCategory returns false for unknown category`() {
        val m = matcher()
        assertFalse(m.hasCategory("未知"))
        assertFalse(m.hasCategory("其他"))
    }

    @Test
    fun `getCategoryStats returns correct stats`() {
        val m = matcher()
        val stats = m.getCategoryStats()
        assertEquals(5, stats.size)
        assertEquals(4, stats["音乐"])
        assertEquals(4, stats["视频"])
        assertEquals(4, stats["社交"])
        assertEquals(3, stats["购物"])
        assertEquals(4, stats["工具"])
    }

    @Test
    fun `getAllCategories includes other at end`() {
        val m = matcher()
        val all = m.getAllCategories()
        assertEquals(6, all.size) // 5 categories + "其他"
        assertEquals("其他", all.last())
    }

    @Test
    fun `empty matcher has zero counts`() {
        val m = CategoryMatcher(ByteArrayInputStream("{}".toByteArray()))
        assertEquals(0, m.getCategoryCount())
        assertEquals(0, m.getKeywordCount())
    }

    @Test
    fun `malformed json has zero counts`() {
        val m = CategoryMatcher(ByteArrayInputStream("not json".toByteArray()))
        assertEquals(0, m.getCategoryCount())
        assertEquals(0, m.getKeywordCount())
    }
}
