package com.autoapporganizer.core.classification

import android.graphics.Rect
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.util.CategoryMatcher
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.io.ByteArrayInputStream

/**
 * Unit tests for [ClassificationFusion].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class ClassificationFusionTest {

    private val categoriesJson = """
        {
          "音乐": ["qqmusic", "netease.cloudmusic", "music"],
          "视频": ["tiktok", "douyin", "bilibili"],
          "社交": ["wechat", "tencent.mm", "qq"],
          "购物": ["taobao", "jingdong"],
          "工具": ["calculator", "calendar", "settings"]
        }
    """.trimIndent()

    private fun matcher(): CategoryMatcher =
        CategoryMatcher(ByteArrayInputStream(categoriesJson.toByteArray()))

    private fun element(
        label: String,
        x: Int = 100,
        y: Int = 100
    ): ScreenElement = ScreenElement(
        id = label,
        label = label,
        bounds = Rect(x, y, x + 80, y + 80),
        confidence = 1f,
        source = ScreenElement.Source.ACCESSIBILITY
    )

    private fun classifiedApp(
        label: String,
        category: String,
        confidence: Float = 0.9f
    ): ClassifiedApp = ClassifiedApp(
        label = label,
        category = category,
        confidence = confidence,
        reasoning = null
    )

    @Test
    fun `null AI response falls back to keyword matching`() {
        val elements = listOf(element("微信"), element("QQ"), element("计算器"))
        val result = ClassificationFusion.fuse(null, elements, matcher())

        // 微信 and QQ should be in 社交, 计算器 in 工具
        assertTrue(result.containsKey("社交"))
        assertTrue(result.containsKey("工具"))
        assertEquals(2, result["社交"]?.size)
        assertEquals(1, result["工具"]?.size)
    }

    @Test
    fun `AI high confidence classifications are accepted`() {
        val elements = listOf(element("微信"), element("抖音"))
        val aiResponse = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(classifiedApp("微信", "社交", 0.95f))),
                CategoryResult("视频", listOf(classifiedApp("抖音", "视频", 0.92f)))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        val result = ClassificationFusion.fuse(aiResponse, elements, matcher())

        assertEquals(1, result["社交"]?.size)
        assertEquals(1, result["视频"]?.size)
    }

    @Test
    fun `low confidence items fall back to keyword matching`() {
        val elements = listOf(element("微信"), element("计算器"))
        val aiResponse = ClassificationResponse(
            categories = listOf(
                // Low confidence - should fall back to keyword
                CategoryResult("视频", listOf(classifiedApp("微信", "视频", 0.3f))),
                CategoryResult("工具", listOf(classifiedApp("计算器", "工具", 0.9f)))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        val result = ClassificationFusion.fuse(aiResponse, elements, matcher())

        // 微信 should fall back to keyword → 社交 (not 视频)
        assertTrue(result.containsKey("社交"))
        assertEquals(1, result["社交"]?.size)
        // 计算器 should stay in 工具
        assertTrue(result.containsKey("工具"))
        assertEquals(1, result["工具"]?.size)
    }

    @Test
    fun `uncertain items use cache then keyword fallback`() {
        val elements = listOf(element("微信"), element("计算器"))
        val aiResponse = ClassificationResponse(
            categories = emptyList(),
            uncertain = listOf(classifiedApp("微信", "不确定", 0.4f)),
            thought = "test"
        )

        // With cache entry
        val cached = mapOf("微信" to "即时通讯")
        val result = ClassificationFusion.fuse(aiResponse, elements, matcher(), cached)

        assertTrue(result.containsKey("即时通讯"))
        assertEquals(1, result["即时通讯"]?.size)
    }

    @Test
    fun `unclassified elements fall back to keyword matching`() {
        val elements = listOf(element("微信"), element("计算器"), element("未知应用"))
        val aiResponse = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(classifiedApp("微信", "社交", 0.95f)))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        val result = ClassificationFusion.fuse(aiResponse, elements, matcher())

        // 微信 in 社交
        assertTrue(result.containsKey("社交"))
        // 计算器 in 工具 (keyword)
        assertTrue(result.containsKey("工具"))
        // 未知应用 in 其他 (no match)
        assertTrue(result.containsKey("其他"))
    }

    @Test
    fun `small categories are merged into other`() {
        val elements = listOf(element("微信"), element("QQ"), element("计算器"))
        val aiResponse = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    classifiedApp("微信", "社交", 0.95f),
                    classifiedApp("QQ", "社交", 0.92f)
                )),
                // Only 1 element in 工具 - should be merged to 其他
                CategoryResult("工具", listOf(classifiedApp("计算器", "工具", 0.9f)))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        val result = ClassificationFusion.fuse(aiResponse, elements, matcher())

        assertTrue(result.containsKey("社交"))
        assertEquals(2, result["社交"]?.size)
        // 工具 has only 1 element, should be merged to 其他
        assertFalse(result.containsKey("工具"))
        assertTrue(result.containsKey("其他"))
        assertEquals(1, result["其他"]?.size)
    }

    @Test
    fun `empty elements returns empty result`() {
        val aiResponse = ClassificationResponse(
            categories = emptyList(),
            uncertain = emptyList(),
            thought = "test"
        )

        val result = ClassificationFusion.fuse(aiResponse, emptyList(), matcher())
        assertTrue(result.isEmpty())
    }
}
