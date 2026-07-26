package com.autoapporganizer.core.action

import android.graphics.Bitmap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [ActionExecutor].
 */
class ActionExecutorTest {

    private class FakeGestureEngine : GestureEngine {
        val executed = mutableListOf<Action>()
        var failCount = 0
        var nextThrows: Throwable? = null
        override var screenWidth: Int = 1080
        override var screenHeight: Int = 2400

        override suspend fun execute(action: Action): Boolean {
            nextThrows?.let {
                nextThrows = null
                throw it
            }
            if (failCount > 0) {
                failCount--
                return false
            }
            executed.add(action)
            return true
        }

        override suspend fun performClick(x: Float, y: Float): Boolean = true
        override suspend fun performLongPress(x: Float, y: Float, durationMs: Long): Boolean = true
        override suspend fun performDrag(
            fromX: Float, fromY: Float,
            toX: Float, toY: Float,
            durationMs: Long
        ): Boolean = true

        override suspend fun performDrag(
            fromX: Float, fromY: Float,
            toX: Float, toY: Float,
            holdMs: Long, dragMs: Long
        ): Boolean = true

        override suspend fun takeScreenshot(): Bitmap? = null
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute returns true on success and records action`() = runTest {
        val engine = FakeGestureEngine()
        val executor = ActionExecutor(engine, settleDelayMs = 0)

        val action = Action.Click(100f, 200f)
        assertTrue(executor.execute(action))
        assertEquals(listOf(action), engine.executed)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute retries and succeeds after transient failures`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 2 }
        val executor = ActionExecutor(engine, maxRetries = 2, settleDelayMs = 0)

        assertTrue(executor.execute(Action.Home))
        assertEquals(1, engine.executed.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute returns false when retries are exhausted`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 5 }
        val executor = ActionExecutor(engine, maxRetries = 2, settleDelayMs = 0)

        assertFalse(executor.execute(Action.Back))
        assertTrue(engine.executed.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `non retryable action fails on first failure`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 5 }
        val executor = ActionExecutor(engine, maxRetries = 2, settleDelayMs = 0)

        assertFalse(executor.execute(Action.Click(1f, 2f), retryable = false))
        assertTrue(engine.executed.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute sequence succeeds for all actions`() = runTest {
        val engine = FakeGestureEngine()
        val executor = ActionExecutor(engine, settleDelayMs = 0)

        val actions = listOf(Action.Click(1f, 2f), Action.Wait(100), Action.Home)
        assertEquals(3, executor.executeSequence(actions))
        assertEquals(actions, engine.executed)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute sequence aborts on failure by default`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 1 }
        val executor = ActionExecutor(engine, maxRetries = 0, settleDelayMs = 0)

        val actions = listOf(Action.Click(1f, 2f), Action.Home)
        assertEquals(0, executor.executeSequence(actions))
        assertTrue(engine.executed.isEmpty())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute sequence continues on failure when abort is disabled`() = runTest {
        val engine = FakeGestureEngine().apply { failCount = 1 }
        val executor = ActionExecutor(engine, maxRetries = 0, settleDelayMs = 0)

        val actions = listOf(Action.Click(1f, 2f), Action.Home)
        assertEquals(1, executor.executeSequence(actions, abortOnFailure = false))
        assertEquals(listOf(Action.Home), engine.executed)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `execute retries after engine exception`() = runTest {
        val engine = FakeGestureEngine().apply {
            nextThrows = RuntimeException("boom")
        }
        val executor = ActionExecutor(engine, maxRetries = 1, settleDelayMs = 0)

        assertTrue(executor.execute(Action.Click(1f, 2f)))
        assertEquals(1, engine.executed.size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `go home and screenshot reports success with null screenshot`() = runTest {
        val engine = FakeGestureEngine()
        val executor = ActionExecutor(engine, settleDelayMs = 0)

        val result = executor.goHomeAndScreenshot()
        assertTrue(result.success)
        assertNull(result.screenshot)
        assertTrue(engine.executed.any { it is Action.Home })
    }
}
