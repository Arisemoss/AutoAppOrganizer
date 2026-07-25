package com.autoapporganizer.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.ByteArrayInputStream

/**
 * Unit tests for [CategoryMatcher].
 *
 * These tests use the test-friendly [CategoryMatcher]`(InputStream)` constructor to inject
 * a controlled JSON, so they run on a plain JVM without Robolectric or AssetManager mocking.
 *
 * The most important test here is [qq_music_must_not_match_social_first]: it guards the
 * ordering invariant documented in [CategoryMatcher.matchCategory] — that more specific
 * categories (e.g. "音乐" containing `qqmusic`) must be listed in `categories.json`
 * *before* broader ones (e.g. "社交" containing `qq`), otherwise a package like
 * `com.tencent.qqmusic` would be wrongly classified as social.
 */
class CategoryMatcherTest {

    /**
     * Same ordering as the real `assets/categories.json`:
     * 音乐 (qqmusic) comes before 社交 (qq), so QQ Music is not swallowed by 社交.
     */
    private val realisticJson = """
        {
          "音乐": ["qqmusic", "netease.cloudmusic", "music"],
          "视频": ["tiktok", "douyin", "bilibili"],
          "游戏": ["game", "tencent.tmgp", "mihoyo"],
          "社交": ["wechat", "tencent.mm", "tencent.mobileqq", "qq"],
          "购物": ["taobao", "jingdong", "pinduoduo"],
          "工具": ["calculator", "calendar", "clock", "settings"],
          "系统": ["设置", "相机"]
        }
    """.trimIndent()

    private fun matcher(json: String = realisticJson): CategoryMatcher =
        CategoryMatcher(ByteArrayInputStream(json.toByteArray()))

    // ── Critical ordering invariant ───────────────────────────────────────────────

    @Test
    fun `qq_music_must_not_match_social_first`() {
        // This is the regression guard for the #11 refactor.
        // If someone reorders categories.json and puts 社交 before 音乐,
        // com.tencent.qqmusic would match 社交 (via "qq") before 音乐 (via "qqmusic").
        val m = matcher()
        assertEquals("音乐", m.matchCategory("com.tencent.qqmusic"))
    }

    @Test
    fun `real_qq_must_match_social_not_music`() {
        // The mirror invariant: real QQ (mobileqq) should still hit 社交,
        // not be stolen by 音乐's "music" keyword.
        val m = matcher()
        assertEquals("社交", m.matchCategory("com.tencent.mobileqq"))
    }

    // ── Basic positive cases ──────────────────────────────────────────────────────

    @Test
    fun `package_name_match_returns_expected_category`() {
        val m = matcher()
        assertEquals("社交", m.matchCategory("com.tencent.mm"))        // WeChat
        assertEquals("购物", m.matchCategory("com.taobao.taobao"))     // Taobao
        assertEquals("游戏", m.matchCategory("com.miHoYo.GenshinImpact")) // Genshin
        assertEquals("视频", m.matchCategory("com.ss.android.ugc.aweme")) // Douyin (legacy)
        assertEquals("音乐", m.matchCategory("com.netease.cloudmusic"))   // NetEase Music
    }

    @Test
    fun `chinese_app_name_match_returns_expected_category`() {
        val m = matcher()
        // No package name — the production code path in #11 falls back to item.name.
        assertEquals("购物", m.matchCategory("淘宝"))
        assertEquals("视频", m.matchCategory("抖音"))
        assertEquals("音乐", m.matchCategory("网易云音乐"))
    }

    // ── Negative cases ────────────────────────────────────────────────────────────

    @Test
    fun `unknown_app_returns_other`() {
        val m = matcher()
        assertEquals("其他", m.matchCategory("com.unknown.randomapp"))
    }

    @Test
    fun `null_or_empty_input_returns_other`() {
        val m = matcher()
        assertEquals("其他", m.matchCategory(null))
        assertEquals("其他", m.matchCategory(""))
    }

    // ── Case insensitivity ────────────────────────────────────────────────────────

    @Test
    fun `matching_is_case_insensitive`() {
        val m = matcher()
        assertEquals("社交", m.matchCategory("COM.TENCENT.MM"))
        assertEquals("社交", m.matchCategory("WeChat"))
    }

    // ── Malformed input / loading robustness ──────────────────────────────────────

    @Test
    fun `malformed_json_yields_empty_categories_so_everything_is_other`() {
        val m = CategoryMatcher(ByteArrayInputStream("not json {".toByteArray()))
        // loadCategories() catches the parse exception and returns emptyMap(),
        // so matchCategory() falls through to "其他".
        assertEquals("其他", m.matchCategory("com.tencent.mm"))
    }

    @Test
    fun `empty_json_object_yields_empty_categories`() {
        val m = CategoryMatcher(ByteArrayInputStream("{}".toByteArray()))
        assertEquals("其他", m.matchCategory("com.tencent.mm"))
    }

    // ── getAllCategories ──────────────────────────────────────────────────────────

    @Test
    fun `getAllCategories_includes_other_at_end`() {
        val m = matcher()
        val all = m.getAllCategories()
        assertTrue("must contain 音乐", all.contains("音乐"))
        assertTrue("must contain 社交", all.contains("社交"))
        assertEquals("其他", all.last())
    }
}
