package com.autoapporganizer.model

import android.graphics.Rect
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [DesktopItem].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DesktopItemTest {

    @Test
    fun `app item has correct type and properties`() {
        val item = DesktopItem.app("微信", "com.tencent.mm", Rect(100, 100, 180, 180))
        assertTrue(item.isApp)
        assertFalse(item.isWidget)
        assertFalse(item.isFolder)
        assertEquals("微信", item.name)
        assertEquals("com.tencent.mm", item.packageName)
        assertTrue(item.hasPackageName)
        assertTrue(item.hasName)
    }

    @Test
    fun `widget item has correct type`() {
        val item = DesktopItem.widget(Rect(0, 0, 200, 100))
        assertFalse(item.isApp)
        assertTrue(item.isWidget)
        assertFalse(item.isFolder)
        assertFalse(item.hasPackageName)
    }

    @Test
    fun `folder item has correct type and children`() {
        val children = listOf(
            DesktopItem.app("微信", "com.tencent.mm", Rect(0, 0, 80, 80)),
            DesktopItem.app("QQ", "com.tencent.mobileqq", Rect(80, 0, 160, 80))
        )
        val item = DesktopItem.folder("社交", Rect(0, 0, 160, 80), children)
        assertFalse(item.isApp)
        assertFalse(item.isWidget)
        assertTrue(item.isFolder)
        assertEquals(2, item.childCount)
    }

    @Test
    fun `displayLabel prefers name over packageName`() {
        val withName = DesktopItem.app("微信", "com.tencent.mm", Rect(0, 0, 80, 80))
        assertEquals("微信", withName.displayLabel)

        val withoutName = DesktopItem(type = DesktopItem.ItemType.APP, packageName = "com.tencent.mm", bounds = Rect(0, 0, 80, 80))
        assertEquals("com.tencent.mm", withoutName.displayLabel)

        val unknown = DesktopItem(type = DesktopItem.ItemType.UNKNOWN, bounds = Rect(0, 0, 80, 80))
        assertEquals("未知", unknown.displayLabel)
    }

    @Test
    fun `center coordinates are correct`() {
        val item = DesktopItem.app("Test", "com.test", Rect(100, 200, 200, 300))
        assertEquals(150, item.centerX)
        assertEquals(250, item.centerY)
    }

    @Test
    fun `width and height are correct`() {
        val item = DesktopItem.app("Test", "com.test", Rect(100, 200, 200, 300))
        assertEquals(100, item.width)
        assertEquals(100, item.height)
    }

    @Test
    fun `contains checks bounds correctly`() {
        val item = DesktopItem.app("Test", "com.test", Rect(100, 100, 200, 200))
        assertTrue(item.contains(150, 150))
        assertTrue(item.contains(100, 100))
        assertFalse(item.contains(50, 50))
        assertFalse(item.contains(250, 250))
    }

    @Test
    fun `distanceTo calculates correctly`() {
        val item1 = DesktopItem.app("A", "com.a", Rect(0, 0, 80, 80))
        val item2 = DesktopItem.app("B", "com.b", Rect(100, 0, 180, 80))
        assertEquals(100.0, item1.distanceTo(item2), 1.0)
    }

    @Test
    fun `factory methods create correct types`() {
        val app = DesktopItem.app("Test", "com.test", Rect(0, 0, 80, 80))
        assertTrue(app.isApp)

        val widget = DesktopItem.widget(Rect(0, 0, 200, 100))
        assertTrue(widget.isWidget)

        val folder = DesktopItem.folder("Test", Rect(0, 0, 80, 80))
        assertTrue(folder.isFolder)
    }
}
