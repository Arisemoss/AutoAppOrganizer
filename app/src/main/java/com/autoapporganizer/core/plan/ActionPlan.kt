package com.autoapporganizer.core.plan

import com.autoapporganizer.core.action.Action

/**
 * A structured plan returned by the vision model.
 *
 * @property thought The model's reasoning, useful for logging and debugging.
 * @property actions The concrete actions to execute on the device.
 */
data class ActionPlan(
    val thought: String,
    val actions: List<Action>
)
