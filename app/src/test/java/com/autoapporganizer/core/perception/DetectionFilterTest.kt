package com.autoapporganizer.core.perception

import android.graphics.Rect
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [DetectionFilter].
 *
 * Robolectric is used only to provide the Android [Rect] class on the JVM;
 * the filter logic itself is pure Kotlin. Screen size 1080x2400 is used throughout,
 * matching a typical modern phone.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DetectionFilterTest {

    private val screenWidth = 1080
    private val screenHeight = 2400

    private fun element(
        label: String,
        bounds: Rect,
        confidence: Float = 1f,
        id: String = label
    ): ScreenElement = ScreenElement(
        id = id,
        label = label,
        bounds = bounds,
        confidence = confidence,
        source = ScreenElement.Source.VISION
    )

    // ── NMS ─────────────────────────────────────────────────────────────────────

    @Test
    fun `nms keeps highest confidence duplicate`() {
        val dupLow = element("微信", Rect(100, 100, 220, 220), confidence = 0.6f, id = "low")
        val dupHigh = element("微信", Rect(110, 110, 230, 230), confidence = 0.95f, id = "high")
        val result = DetectionFilter.nms(listOf(dupLow, dupHigh))
        assertEquals(1, result.size)
        assertEquals("high", result[0].id)
    }

    @Test
    fun `nms keeps non overlapping detections`() {
        val a = element("微信", Rect(0, 0, 100, 100))
        val b = element("QQ", Rect(300, 300, 400, 400))
        val result = DetectionFilter.nms(listOf(a, b))
        assertEquals(2, result.size)
    }

    @Test
    fun `nms suppresses box contained inside another`() {
        val outer = element("A", Rect(0, 0, 300, 300), confidence = 0.9f)
        val inner = element("B", Rect(50, 50, 250, 250), confidence = 0.8f)
        val result = DetectionFilter.nms(listOf(inner, outer))
        assertEquals(1, result.size)
        assertEquals("A", result[0].id)
    }

    @Test
    fun `nms empty and single lists are unchanged`() {
        assertTrue(DetectionFilter.nms(emptyList()).isEmpty())
        val one = element("A", Rect(0, 0, 100, 100))
        assertEquals(listOf(one), DetectionFilter.nms(listOf(one)))
    }

    // ── IoU ─────────────────────────────────────────────────────────────────────

    @Test
    fun `iou of identical boxes is one`() {
        assertEquals(1f, DetectionFilter.iou(Rect(0, 0, 100, 100), Rect(0, 0, 100, 100)), 0.001f)
    }

    @Test
    fun `iou of disjoint boxes is zero`() {
        assertEquals(0f, DetectionFilter.iou(Rect(0, 0, 100, 100), Rect(200, 200, 300, 300)), 0.001f)
    }

    // ── Size constraints ────────────────────────────────────────────────────────

    @Test
    fun `tiny hallucination boxes are dropped`() {
        val tiny = element("幻觉", Rect(500, 500, 510, 510)) // 10px — way below 4% of 1080
        val result = DetectionFilter.apply(listOf(tiny), screenWidth, screenHeight)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `folder sized boxes are dropped`() {
        // 600px wide is beyond 26% of 1080 (280px) — looks like a folder, not an icon.
        val folder = element("文件夹", Rect(100, 100, 700, 700))
        val result = DetectionFilter.apply(listOf(folder), screenWidth, screenHeight)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `typical icon size survives`() {
        // 120x120 at 1080 width is a normal launcher icon.
        val icon = element("微信", Rect(100, 200, 220, 320))
        val result = DetectionFilter.apply(listOf(icon), screenWidth, screenHeight)
        assertEquals(1, result.size)
        assertEquals("微信", result[0].label)
    }

    // ── Confidence floor ────────────────────────────────────────────────────────

    @Test
    fun `low confidence detections are dropped`() {
        val weak = element("微信", Rect(100, 100, 220, 220), confidence = 0.2f)
        val result = DetectionFilter.apply(listOf(weak), screenWidth, screenHeight)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `default confidence detections survive`() {
        // Legacy VLMs that omit confidence default to 1f and must not be dropped.
        val icon = element("微信", Rect(100, 100, 220, 220), confidence = 1f)
        val result = DetectionFilter.apply(listOf(icon), screenWidth, screenHeight)
        assertEquals(1, result.size)
    }

    // ── Out-of-screen handling ──────────────────────────────────────────────────

    @Test
    fun `fully off screen boxes are dropped`() {
        val off = element("微信", Rect(-300, -300, -100, -100))
        val result = DetectionFilter.apply(listOf(off), screenWidth, screenHeight)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `partially visible boxes are clamped onto the screen`() {
        val partial = element("微信", Rect(-50, 100, 100, 250))
        val result = DetectionFilter.apply(listOf(partial), screenWidth, screenHeight)
        assertEquals(1, result.size)
        assertEquals(0, result[0].bounds.left)
        assertEquals(100, result[0].bounds.top)
    }

    // ── Combined pipeline ───────────────────────────────────────────────────────

    @Test
    fun `apply deduplicates and filters in one pass`() {
        val dup1 = element("微信", Rect(100, 100, 220, 220), confidence = 0.9f)
        val dup2 = element("微信", Rect(110, 105, 230, 225), confidence = 0.7f)
        val tiny = element("幻觉", Rect(500, 500, 512, 512))
        val icon = element("QQ", Rect(300, 100, 420, 220), confidence = 0.95f)

        val result = DetectionFilter.apply(
            listOf(dup1, dup2, tiny, icon),
            screenWidth,
            screenHeight
        )
        assertEquals(2, result.size)
        val labels = result.map { it.label }.toSet()
        assertTrue(labels.contains("微信"))
        assertTrue(labels.contains("QQ"))
    }
}