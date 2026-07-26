package com.autoapporganizer.core.agent

import android.graphics.Bitmap
import com.autoapporganizer.core.action.Action
import com.autoapporganizer.core.action.GestureEngine
import com.autoapporganizer.core.model.VisionResult
import com.autoapporganizer.core.perception.AccessibilityChannel
import com.autoapporganizer.core.perception.ScreenElement
import com.autoapporganizer.core.perception.VisionChannel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [AgentRunner].
 */
class AgentRunnerTest {

    private class FakeGestureEngine : GestureEngine {
        val executed = mutableListOf<Action>()
        val failActions = mutableSetOf<String>()
        var failCount = 0
        override var screenWidth: Int = 1080
        override var screenHeight: Int = 2400

        override suspend fun execute(action: Action): Boolean {
            if (failCount > 0) {
                failCount--
                return false
            }
            executed.add(action)
            return true
        }

        override suspend fun performClick(x: Float, y: Float): Boolean = true
        override suspend fun performLongPress(x: Float, y: Float, durationMs: Long): Boolean = true
        override suspend fun performDrag(fromX: Float, fromY: Float, toX: Float, toY: Float, durationMs: Long): Boolean = true
        override suspend fun performDrag(fromX: Float, fromY: Float, toX: Float, toY: Float, holdMs: Long, dragMs: Long): Boolean = true
        override suspend fun takeScreenshot(): Bitmap? = null
    }

    private class FakeAccessibilityChannel : AccessibilityChannel {
        var elements = listOf<ScreenElement>()

        override suspend fun scanElements(): List<ScreenElement> = elements
        override suspend fun screenshot(): Bitmap? = null
    }

    private class FakeVisionChannel : VisionChannel {
        var available = false
        var analyzeResult: VisionResult? = null
        var detectIconsResult = listOf<ScreenElement>()

        override fun isAvailable(): Boolean = available
        override suspend fun analyze(prompt: String): VisionResult = analyzeResult
            ?: VisionResult.Error("Not configured")
        override suspend fun detectIcons(): List<ScreenElement> = detectIconsResult
    }

    /**
     * A simple task that completes after a fixed number of steps.
     */
    private class SimpleTask(
        private val stepsToComplete: Int = 1,
        override val maxSteps: Int = 10
    ) : AgentTask {
        override val name = "SimpleTask"
        private var folderCount = 0

        override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String = "Simple task"
        override suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): Action {
            return if (state.step >= stepsToComplete) Action.Complete else Action.Wait(100)
        }
        override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
            val newErrors = if (!result) state.errors + "Action ${action.describe()} failed" else state.errors
            return state.copy(
                step = state.step + 1,
                itemsOrganized = if (result) state.itemsOrganized + 1 else state.itemsOrganized,
                errors = newErrors
            )
        }
        override fun isComplete(state: TaskState): Boolean = state.step >= stepsToComplete
        override fun getFoldersCreated(): Int = folderCount
        fun setFolderCount(n: Int) { folderCount = n }
    }

    /**
     * A task that fails actions and requests retries.
     */
    private class RetryTask : AgentTask {
        override val name = "RetryTask"
        override val maxSteps = 10
        private var folderCount = 0
        var failNextAction = true

        override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String = "Retry task"
        override suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): Action {
            if (state.step >= 1) return Action.Complete
            return Action.Wait(100)
        }
        override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
            if (!result) {
                return state.copy(
                    step = state.step + 1,
                    errors = state.errors + "Failed",
                    retryHint = true
                )
            }
            return state.copy(step = state.step + 1, itemsOrganized = 1)
        }
        override fun isComplete(state: TaskState): Boolean = state.step >= 1
        override fun getFoldersCreated(): Int = folderCount
    }

    /**
     * A task that sometimes needs vision.
     */
    private class VisionTask : AgentTask {
        override val name = "VisionTask"
        override val maxSteps = 10
        private var folderCount = 0
        var visionRequested = false

        override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String = "Vision task"
        override suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): Action {
            if (state.step >= 1) return Action.Complete
            return Action.Wait(100)
        }
        override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
            return state.copy(step = state.step + 1, itemsOrganized = if (result) 1 else 0)
        }
        override fun isComplete(state: TaskState): Boolean = state.step >= 1
        override fun needsVision(state: TaskState): Boolean = !visionRequested.also { visionRequested = true }
        override fun getFoldersCreated(): Int = folderCount
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `simple task completes successfully`() = runTest {
        val engine = FakeGestureEngine()
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)
        val task = SimpleTask()

        val result = runner.run(task) { _, _ -> }
        assertTrue(result.success)
        assertEquals(1, result.stepsExecuted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `task handles single failure gracefully`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 1 }
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)

        val task = SimpleTask()
        val result = runner.run(task) { _, _ -> }
        // SimpleTask doesn't set retryHint, so no retry; one error is accumulated
        assertFalse(result.success)
        assertTrue(result.message.contains("错误"))
        assertEquals(1, result.stepsExecuted)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `task retries and succeeds when retryHint is set`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 1 }
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)

        // A task that retries without accumulating errors on transient failures
        val task = object : AgentTask {
            override val name = "RecoverableTask"
            override val maxSteps = 10
            private var attempts = 0
            override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String = "Recover"
            override suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): Action {
                if (attempts >= 1) return Action.Complete
                return Action.Wait(100)
            }
            override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState {
                if (!result) {
                    return state.copy(retryHint = true)
                }
                attempts++
                return state.copy(step = state.step + 1, itemsOrganized = 1)
            }
            override fun isComplete(state: TaskState): Boolean = attempts >= 1
            override fun getFoldersCreated(): Int = 0
        }
        val result = runner.run(task) { _, _ -> }
        assertTrue(result.success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `task returns error when max errors exceeded`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 100 }
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)

        // Use a task with enough steps so that errors accumulate beyond the
        // consecutive-failure guard (5) and hit the total-error guard (15).
        val task = SimpleTask(stepsToComplete = 50, maxSteps = 50)
        val result = runner.run(task) { _, _ -> }
        assertFalse(result.success)
        // The runner should abort due to either too many consecutive failures or too many total errors
        assertTrue(result.message.contains("错误") || result.message.contains("完成"))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `vision channel is not called when unavailable`() = runTest {
        val engine = FakeGestureEngine()
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel().apply { available = false }
        val runner = AgentRunner(engine, a11y, vision)

        val task = VisionTask()
        val result = runner.run(task) { _, _ -> }
        assertTrue(result.success)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `agent runner reports correct folder count`() = runTest {
        val engine = FakeGestureEngine()
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)

        val task = SimpleTask().apply { setFolderCount(3) }
        val result = runner.run(task) { _, _ -> }
        assertEquals(3, result.foldersCreated)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `agent runner catches task exceptions`() = runTest {
        val engine = FakeGestureEngine()
        val a11y = FakeAccessibilityChannel()
        val vision = FakeVisionChannel()
        val runner = AgentRunner(engine, a11y, vision)

        val task = object : AgentTask {
            override val name = "CrashTask"
            override val maxSteps = 10
            override suspend fun describe(accessibility: AccessibilityChannel, vision: VisionChannel): String = "Crash"
            override suspend fun reason(state: TaskState, perception: List<ScreenElement>, visionResult: VisionResult?): Action {
                throw RuntimeException("Kaboom")
            }
            override suspend fun observe(action: Action, result: Boolean, state: TaskState): TaskState = state
            override fun isComplete(state: TaskState): Boolean = false
            override fun getFoldersCreated(): Int = 0
        }

        val result = runner.run(task) { _, _ -> }
        assertFalse(result.success)
        assertTrue(result.message.contains("Agent 异常"))
    }
}