package com.autoapporganizer.model

import android.graphics.Rect
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [DesktopBackup].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class DesktopBackupTest {

    private fun appItem(name: String, pkg: String, x: Int = 100, y: Int = 100): DesktopItem {
        return DesktopItem.app(name, pkg, Rect(x, y, x + 80, y + 80))
    }

    private fun widgetItem(x: Int = 200, y: Int = 200): DesktopItem {
        return DesktopItem.widget(Rect(x, y, x + 200, y + 100))
    }

    private fun folderItem(name: String, x: Int = 300, y: Int = 300): DesktopItem {
        return DesktopItem.folder(name, Rect(x, y, x + 80, y + 80))
    }

    @Test
    fun `empty backup has zero counts`() {
        val backup = DesktopBackup()
        assertEquals(0, backup.appCount)
        assertEquals(0, backup.widgetCount)
        assertEquals(0, backup.folderCount)
        assertTrue(backup.isEmpty)
    }

    @Test
    fun `backup counts items correctly`() {
        val backup = DesktopBackup(
            items = listOf(
                appItem("微信", "com.tencent.mm"),
                appItem("QQ", "com.tencent.mobileqq"),
                widgetItem(),
                folderItem("社交")
            )
        )
        assertEquals(2, backup.appCount)
        assertEquals(1, backup.widgetCount)
        assertEquals(1, backup.folderCount)
        assertFalse(backup.isEmpty)
    }

    @Test
    fun `apps filter returns only APP items`() {
        val backup = DesktopBackup(
            items = listOf(
                appItem("微信", "com.tencent.mm"),
                widgetItem(),
                folderItem("社交")
            )
        )
        val apps = backup.apps
        assertEquals(1, apps.size)
        assertEquals("微信", apps[0].name)
    }

    @Test
    fun `findByPackageName finds correct item`() {
        val backup = DesktopBackup(
            items = listOf(
                appItem("微信", "com.tencent.mm"),
                appItem("QQ", "com.tencent.mobileqq")
            )
        )
        val found = backup.findByPackageName("com.tencent.mm")
        assertNotNull(found)
        assertEquals("微信", found?.name)

        val notFound = backup.findByPackageName("com.unknown.app")
        assertNull(notFound)
    }

    @Test
    fun `findByName finds correct item`() {
        val backup = DesktopBackup(
            items = listOf(
                appItem("微信", "com.tencent.mm"),
                appItem("QQ", "com.tencent.mobileqq")
            )
        )
        val found = backup.findByName("QQ")
        assertNotNull(found)
        assertEquals("com.tencent.mobileqq", found?.packageName)
    }

    @Test
    fun `getSummary returns formatted string`() {
        val backup = DesktopBackup(
            timestamp = 1700000000000L,
            items = listOf(
                appItem("微信", "com.tencent.mm"),
                widgetItem(),
                folderItem("社交")
            )
        )
        val summary = backup.getSummary()
        assertTrue(summary.contains("1 个应用"))
        assertTrue(summary.contains("1 个小组件"))
        assertTrue(summary.contains("1 个文件夹"))
    }
}
