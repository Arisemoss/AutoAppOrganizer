package com.autoapporganizer.core.agent

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Unit tests for [TaskState].
 *
 * These tests pin down the data-class contract after the #4 cleanup, in which the
 * `foldersCreated` field was removed (it had been a divergent counter that was never
 * incremented in `observe()` — the real count lives in the task instance via
 * `AgentTask.getFoldersCreated()`).
 *
 * What we are guarding:
 *  - `TaskState()` has no `foldersCreated` parameter (compile-time check would catch
 *    this, but the test makes the intent explicit and survives refactors that might
 *    re-add the field via copy-paste).
 *  - `copy(...)` does not silently preserve a stale folder count.
 *  - The remaining fields (step, itemsOrganized, errors, context) behave as expected.
 */
class TaskStateTest {

    @Test
    fun `default_state_has_zero_step_zero_items_no_errors_empty_context`() {
        val s = TaskState()
        assertEquals(0, s.step)
        assertEquals(0, s.itemsOrganized)
        assertTrue(s.errors.isEmpty())
        assertTrue(s.context.isEmpty())
        assertFalse(s.retryHint)
    }

    @Test
    fun `copy_with_step_increments_independently_of_itemsOrganized`() {
        val s = TaskState(step = 3, itemsOrganized = 5)
        val next = s.copy(step = s.step + 1)
        assertEquals(4, next.step)
        // itemsOrganized must NOT change just because step did.
        assertEquals(5, next.itemsOrganized)
    }

    @Test
    fun `copy_with_itemsOrganized_does_not_touch_step`() {
        val s = TaskState(step = 7, itemsOrganized = 2)
        val next = s.copy(itemsOrganized = s.itemsOrganized + 1)
        assertEquals(3, next.itemsOrganized)
        assertEquals(7, next.step)
    }

    @Test
    fun `errors_list_is_immutable_from_caller_perspective_after_copy`() {
        // The pattern used by DesktopOrganizeTask.observe() is
        //   val errors = if (!result) state.errors + "..." else state.errors
        //   state.copy(errors = errors)
        // Verify that appending to the new list does not mutate the old state's list.
        val s = TaskState(errors = listOf("first"))
        val newErrors = s.errors + "second"
        val next = s.copy(errors = newErrors)

        assertEquals(listOf("first"), s.errors)        // original unchanged
        assertEquals(listOf("first", "second"), next.errors)
    }

    @Test
    fun `context_map_is_replaced_not_mutated_in_place_by_copy`() {
        // DesktopOrganizeTask.observe() does:
        //   val newContext = state.context.toMutableMap()
        //   newContext[...] = ...
        //   state.copy(context = newContext)
        // Verify the original state's context is not mutated.
        val originalContext = mapOf<String, Any>("phase" to "scan")
        val s = TaskState(context = originalContext)

        val newContext = s.context.toMutableMap()
        newContext["phase"] = "drag"
        val next = s.copy(context = newContext)

        assertEquals("scan", s.context["phase"])   // original untouched
        assertEquals("drag", next.context["phase"])
    }

    @Test
    fun `two_states_with_same_fields_are_equal`() {
        val a = TaskState(step = 1, itemsOrganized = 2, errors = listOf("e"), context = mapOf("k" to "v"))
        val b = TaskState(step = 1, itemsOrganized = 2, errors = listOf("e"), context = mapOf("k" to "v"))
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())
    }

    @Test
    fun `states_with_different_step_are_not_equal`() {
        val a = TaskState(step = 1)
        val b = TaskState(step = 2)
        assertNotEquals(a, b)
    }

    @Test
    fun `retryHint_is_false_by_default`() {
        val s = TaskState()
        assertFalse(s.retryHint)
    }

    @Test
    fun `retryHint_can_be_set_via_copy`() {
        val s = TaskState()
        val withRetry = s.copy(retryHint = true)
        assertTrue(withRetry.retryHint)
        assertFalse(s.retryHint) // original unchanged
    }

    /**
     * Regression guard for #4: if someone re-adds a `foldersCreated` field to [TaskState],
     * this test will fail to compile (because `copy(foldersCreated = ...)` would be a valid
     * call that we explicitly want to forbid). Keeping the test makes the cleanup intent
     * explicit and self-documenting.
     *
     * The canonical counter source is [AgentTask.getFoldersCreated]; see its kdoc.
     */
    @Test
    fun `task_state_has_no_foldersCreated_parameter_after_cleanup`() {
        // If foldersCreated were still a constructor parameter, this call would compile
        // and silently pass. After #4 it must not compile — which is exactly what we want.
        // We express the invariant indirectly by asserting the field does not exist on
        // the data class via reflection.
        val fields = TaskState::class.java.declaredFields.map { it.name }
        assertFalse(
            "TaskState must NOT have a foldersCreated field after #4 cleanup; found: $fields",
            fields.contains("foldersCreated")
        )
    }
}
