package com.autoapporganizer.core.strategy

/**
 * Outcome of an organizing strategy run.
 *
 * @property success Whether the strategy completed its goal.
 * @property message Human-readable summary for the UI.
 * @property foldersCreated Number of folders created (or 0).
 * @property appsOrganized Number of apps moved/categorized (or 0).
 */
data class StrategyResult(
    val success: Boolean,
    val message: String,
    val foldersCreated: Int = 0,
    val appsOrganized: Int = 0
)
