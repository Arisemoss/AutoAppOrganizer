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
          "音乐": ["qqmusic", "netease.cloudmusic", "music", "网易云音乐"],
          "视频": ["tiktok", "douyin", "bilibili", "youtube", "aweme", "抖音"],
          "游戏": ["game", "tencent.tmgp", "mihoyo"],
          "社交": ["wechat", "tencent.mm", "tencent.mobileqq", "qq", "微信"],
          "购物": ["taobao", "jingdong", "pinduoduo", "淘宝"],
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

    // ── Package-name evidence (new two-argument overload) ───────────────────────

    @Test
    fun `package_name_wins_over_ambiguous_label`() {
        // Label "QQ音乐" alone would hit 社交 via "qq" (音乐's "qqmusic" does not match
        // the Chinese label), but the package name is unambiguous evidence.
        val m = matcher()
        assertEquals("音乐", m.matchCategory("QQ音乐", "com.tencent.qqmusic"))
    }

    @Test
    fun `blank_label_with_known_package_still_classifies`() {
        val m = matcher()
        assertEquals("社交", m.matchCategory("", "com.tencent.mm"))
        assertEquals("社交", m.matchCategory(null, "com.tencent.mobileqq"))
    }

    @Test
    fun `unknown_package_with_known_label_falls_back_to_label`() {
        val m = matcher()
        assertEquals("社交", m.matchCategory("微信", "com.some.unknown.pkg"))
    }

    @Test
    fun `one_argument_overload_still_works`() {
        val m = matcher()
        assertEquals("购物", m.matchCategory("淘宝"))
    }

    // ── Fuzzy matching (typos / punctuation / case variants) ────────────────────

    @Test
    fun `fuzzy_match_recovers_typo_in_english_keyword`() {
        // "yotube" (missing a 'u') is one edit away from "youtube".
        val m = matcher()
        assertEquals("视频", m.matchCategory("Yotube"))
    }

    @Test
    fun `fuzzy_match_recovers_punctuation_noise`() {
        // normalize() strips dots and lowercases: "We.Chat" -> "wechat".
        val m = matcher()
        assertEquals("社交", m.matchCategory("We.Chat"))
    }

    @Test
    fun `fuzzy_match_does_not_force_unrelated_apps`() {
        // "com.unknown.randomapp" shares no meaningful similarity with any keyword.
        val m = matcher()
        assertEquals("其他", m.matchCategory("com.unknown.randomapp"))
    }

    @Test
    fun `fuzzy_match_rejects_short_inputs`() {
        // Very short names have no discriminative power for edit distance.
        val m = matcher()
        assertEquals("其他", m.matchCategory("q"))
    }
}
