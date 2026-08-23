package com.autoapporganizer.util

import com.autoapporganizer.model.OrganizeSession
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit tests for [HistoryManager] data structures.
 *
 * Note: Full integration tests require Android context (SharedPreferences/files).
 * These tests verify the data model and serialization logic.
 */
class HistoryManagerTest {

    @Test
    fun `OrganizeSession data class works correctly`() {
        val session = OrganizeSession(
            timestamp = 1234567890L,
            folderCount = 5,
            appCount = 20,
            categories = mapOf("社交" to 4, "视频" to 3, "工具" to 2),
            launcher = "com.miui.home"
        )

        assertEquals(1234567890L, session.timestamp)
        assertEquals(5, session.folderCount)
        assertEquals(20, session.appCount)
        assertEquals(3, session.categories.size)
        assertEquals(4, session.categories["社交"])
        assertEquals("com.miui.home", session.launcher)
    }

    @Test
    fun `OrganizeSession with null launcher`() {
        val session = OrganizeSession(
            timestamp = System.currentTimeMillis(),
            folderCount = 0,
            appCount = 0,
            categories = emptyMap(),
            launcher = null
        )

        assertNull(session.launcher)
    }

    @Test
    fun `OrganizeSession categories map is correct`() {
        val categories = mapOf(
            "音乐" to 3,
            "视频" to 5,
            "游戏" to 8,
            "社交" to 4,
            "购物" to 2,
            "工具" to 6
        )

        val session = OrganizeSession(
            timestamp = System.currentTimeMillis(),
            folderCount = categories.size,
            appCount = categories.values.sum(),
            categories = categories,
            launcher = "com.android.launcher3"
        )

        assertEquals(6, session.folderCount)
        assertEquals(28, session.appCount)
        assertEquals(8, session.categories["游戏"])
    }
}
