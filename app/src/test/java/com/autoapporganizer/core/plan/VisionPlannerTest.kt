package com.autoapporganizer.core.plan

import android.graphics.Rect
import com.autoapporganizer.core.action.Action
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [VisionPlanner].
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class VisionPlannerTest {

    private val screenBounds = Rect(0, 0, 1080, 2400)

    @Test
    fun `parse simple plan with thought and actions`() {
        val raw = """
            {
              "thought": "Drag icon A onto icon B",
              "actions": [
                {"type": "drag", "fromX": 100, "fromY": 200, "toX": 300, "toY": 400},
                {"type": "wait", "ms": 250},
                {"type": "complete"}
              ]
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        assertEquals("Drag icon A onto icon B", plan.thought)
        assertEquals(3, plan.actions.size)

        val drag = plan.actions[0] as Action.Drag
        assertEquals(100f, drag.fromX, 0.001f)
        assertEquals(200f, drag.fromY, 0.001f)
        assertEquals(300f, drag.toX, 0.001f)
        assertEquals(400f, drag.toY, 0.001f)
        assertEquals(800L, drag.durationMs)

        val wait = plan.actions[1] as Action.Wait
        assertEquals(250L, wait.ms)

        assertTrue(plan.actions[2] is Action.Complete)
    }

    @Test
    fun `parse fenced markdown response`() {
        val raw = """
            Some explanation here.
            ```json
            {
              "thought": "Click the button",
              "actions": [
                {"type": "click", "x": 540, "y": 1200}
              ]
            }
            ```
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        assertEquals(1, plan.actions.size)
        val click = plan.actions[0] as Action.Click
        assertEquals(540f, click.x, 0.001f)
        assertEquals(1200f, click.y, 0.001f)
    }

    @Test
    fun `normalize ratio coordinates to screen bounds`() {
        val raw = """
            {
              "thought": "Tap center",
              "actions": [
                {"type": "click", "x": 0.5, "y": 0.5}
              ]
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        val click = plan.actions[0] as Action.Click
        assertEquals(540f, click.x, 0.001f)
        assertEquals(1200f, click.y, 0.001f)
    }

    @Test
    fun `string numeric fields are tolerated`() {
        val raw = """
            {
              "thought": "Swipe up",
              "actions": [
                {"type": "swipe", "fromX": "100", "fromY": "2000", "toX": "100", "toY": "100", "durationMs": "300"}
              ]
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        val swipe = plan.actions[0] as Action.Swipe
        assertEquals(100f, swipe.fromX, 0.001f)
        assertEquals(2000f, swipe.fromY, 0.001f)
        assertEquals(300L, swipe.durationMs)
    }

    @Test
    fun `unknown action types are skipped`() {
        val raw = """
            {
              "thought": "Mixed",
              "actions": [
                {"type": "unknown"},
                {"type": "home"}
              ]
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        assertEquals(1, plan.actions.size)
        assertTrue(plan.actions[0] is Action.Home)
    }

    @Test
    fun `invalid json returns empty plan with error thought`() {
        val raw = "this is not json"
        val plan = VisionPlanner.parse(raw, screenBounds)
        assertTrue(plan.actions.isEmpty())
        assertTrue(plan.thought.contains("Parse error"))
    }

    @Test
    fun `missing actions array yields no actions`() {
        val raw = """
            {
              "thought": "Nothing to do"
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        assertEquals("Nothing to do", plan.thought)
        assertTrue(plan.actions.isEmpty())
    }

    @Test
    fun `parse all supported action variants`() {
        val raw = """
            {
              "thought": "Full suite",
              "actions": [
                {"type": "click", "x": 1, "y": 2},
                {"type": "longpress", "x": 3, "y": 4, "durationMs": 1000},
                {"type": "long_press", "x": 5, "y": 6},
                {"type": "drag", "fromX": 7, "fromY": 8, "toX": 9, "toY": 10},
                {"type": "swipe", "fromX": 11, "fromY": 12, "toX": 13, "toY": 14},
                {"type": "type", "text": "hello"},
                {"type": "wait"},
                {"type": "home"},
                {"type": "back"},
                {"type": "complete"}
              ]
            }
        """.trimIndent()

        val plan = VisionPlanner.parse(raw, screenBounds)
        assertEquals(10, plan.actions.size)
        assertTrue(plan.actions[0] is Action.Click)
        assertEquals(1000L, (plan.actions[1] as Action.LongPress).durationMs)
        assertTrue(plan.actions[2] is Action.LongPress)
        assertTrue(plan.actions[3] is Action.Drag)
        assertTrue(plan.actions[4] is Action.Swipe)
        assertEquals("hello", (plan.actions[5] as Action.Type).text)
        assertEquals(500L, (plan.actions[6] as Action.Wait).ms)
        assertTrue(plan.actions[7] is Action.Home)
        assertTrue(plan.actions[8] is Action.Back)
        assertTrue(plan.actions[9] is Action.Complete)
    }
}
