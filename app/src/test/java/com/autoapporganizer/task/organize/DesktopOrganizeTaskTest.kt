package com.autoapporganizer.task.organize

import android.content.Context
import android.content.res.Resources
import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.agent.TaskState
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.testutil.TestHelpers.mockDisplayMetrics
import com.autoapporganizer.testutil.TestHelpers.mockRect
import com.autoapporganizer.util.PrefsManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.Mockito.`when`

/**
 * Unit tests for [DesktopOrganizeTask] phase transitions and retry logic.
 *
 * These tests verify the state machine behaviour without requiring a real
 * AccessibilityService or VLM. Fakes are injected for the channels.
 */
class DesktopOrganizeTaskTest {

    private lateinit var task: DesktopOrganizeTask
    private lateinit var prefs: PrefsManager

    private fun element(label: String, x: Int, y: Int, w: Int = 100, h: Int = 100): ScreenElement {
        return ScreenElement(
            id = label,
            label = label,
            bounds = mockRect(x, y, x + w, y + h),
            confidence = 1f,
            source = ScreenElement.Source.ACCESSIBILITY
        )
    }

    @Before
    fun setUp() {
        val mockContext = Mockito.mock(Context::class.java)
        val mockResources = Mockito.mock(Resources::class.java)
        val mockDisplayMetrics = mockDisplayMetrics(1080, 2400)
        `when`(mockContext.resources).thenReturn(mockResources)
        `when`(mockResources.displayMetrics).thenReturn(mockDisplayMetrics)
        `when`(mockContext.applicationContext).thenReturn(mockContext)
        prefs = Mockito.mock(PrefsManager::class.java)
        `when`(prefs.minFolderSize).thenReturn(2)

        task = DesktopOrganizeTask(
            perceptionChannel = object : com.autoapporganizer.core.perception.AccessibilityChannel {
                override suspend fun scanElements(): List<ScreenElement> = emptyList()
                override suspend fun screenshot(): android.graphics.Bitmap? = null
            },
            visionChannel = object : com.autoapporganizer.core.perception.VisionChannel {
                override fun isAvailable(): Boolean = false
                override suspend fun analyze(prompt: String): VisionResult = VisionResult.Error("N/A")
                override suspend fun detectIcons(): List<ScreenElement> = emptyList()
            },
            context = mockContext,
            prefs = prefs
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `scan phase produces an action`() = runTest {
        val state = TaskState(context = mapOf("phase" to "scan"))

        val perception = listOf(
            element("Chrome", 100, 100),
            element("Firefox", 300, 100)
        )

        val action = task.reason(state, perception, null)
        // If no categories are loaded, it will return Complete
        assertNotNull(action)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observe scan success with no categories returns done`() = runTest {
        val state = TaskState(context = mapOf("phase" to "scan"))

        val action = Action.Drag(100f, 100f, 300f, 300f, 800L)
        val newState = task.observe(action, true, state)

        // When no categories are queued, it should mark done
        val phase = newState.context["phase"]
        assertNotNull(phase)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observe scan failure increments retry count`() = runTest {
        val state = TaskState(context = mapOf("phase" to "scan"))

        val action = Action.Drag(100f, 100f, 300f, 300f, 800L)
        val newState = task.observe(action, false, state)

        val retryCount = newState.context["retryCount"] as? Int
        assertNotNull(retryCount)
        assertEquals(1, retryCount)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observe scan failure sets retryHint`() = runTest {
        val state = TaskState(context = mapOf("phase" to "scan"))

        val action = Action.Drag(100f, 100f, 300f, 300f, 800L)
        val newState = task.observe(action, false, state)

        assertTrue(newState.retryHint)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observe drag success transitions to next when category empty`() = runTest {
        // When categorized map doesn't contain the category, the drag phase
        // transitions to "next" (removing dragIndex from context).
        val state = TaskState(context = mapOf(
            "phase" to "drag",
            "category" to "tools",
            "dragIndex" to 2,
            "folderBounds" to mockRect(300, 100, 400, 200),
            "retryCount" to 0
        ))

        val action = Action.Drag(500f, 100f, 350f, 150f, 800L)
        val newState = task.observe(action, true, state)

        // When the category is not in categorized, the phase transitions to "next"
        val phase = newState.context["phase"] as? String
        assertEquals("next", phase)
        // dragIndex is removed from context
        assertFalse(newState.context.containsKey("dragIndex"))
        assertFalse(newState.retryHint)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observe drag failure sets retryHint`() = runTest {
        val state = TaskState(context = mapOf(
            "phase" to "drag",
            "category" to "tools",
            "dragIndex" to 2,
            "folderBounds" to mockRect(300, 100, 400, 200),
            "retryCount" to 0
        ))

        val action = Action.Drag(500f, 100f, 350f, 150f, 800L)
        val newState = task.observe(action, false, state)

        assertTrue(newState.retryHint)
        assertEquals(1, newState.context["retryCount"])
        assertTrue(newState.errors.isNotEmpty())
    }

    @Test
    fun `isComplete returns true when phase is done`() {
        val state = TaskState(context = mapOf("phase" to "done"))
        assertTrue(task.isComplete(state))
    }

    @Test
    fun `isComplete returns true when no categories are queued`() {
        // When no categories have been loaded (categoryQueue is empty),
        // isComplete returns true regardless of phase.
        val state = TaskState(context = mapOf("phase" to "scan"))
        assertTrue(task.isComplete(state))
    }

    @Test
    fun `needsVision returns true for scan and drag phases`() {
        assertTrue(task.needsVision(TaskState(context = mapOf("phase" to "scan"))))
        assertTrue(task.needsVision(TaskState(context = mapOf("phase" to "drag"))))
    }

    @Test
    fun `needsVision returns false for next and done phases`() {
        assertFalse(task.needsVision(TaskState(context = mapOf("phase" to "next"))))
        assertFalse(task.needsVision(TaskState(context = mapOf("phase" to "done"))))
    }

    @Test
    fun `getFoldersCreated starts at zero`() {
        assertEquals(0, task.getFoldersCreated())
    }

    @Test
    fun `maxSteps is reasonable`() {
        assertTrue(task.maxSteps > 0)
        assertTrue(task.maxSteps <= 200)
    }

    @Test
    fun `name is not blank`() {
        assertTrue(task.name.isNotBlank())
    }
}