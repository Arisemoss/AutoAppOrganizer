package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [GestureEngine] / [ActionExecutor]
 * to perform on the device. Being a sealed class allows exhaustive `when` matching over all variants.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY) quickly (e.g. scroll a page). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds (e.g. to wait for an animation). */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    /**
     * A short, human-readable representation useful for logging. Each subclass overrides
     * via the exhaustive `when` below so newly added actions are forced to provide a label.
     */
    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}\u2192${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}\u2192${toX.toInt()},${toY.toInt()})"
        is Type -> "Type(${text.length}chars)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
