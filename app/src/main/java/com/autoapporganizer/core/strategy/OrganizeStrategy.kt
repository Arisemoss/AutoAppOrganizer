package com.autoapporganizer.core.strategy

/**
 * Strategy pattern entry point for organizing the home screen.
 *
 * Concrete implementations may use pure accessibility (legacy), a vision model,
 * or a hybrid of both.
 */
interface OrganizeStrategy {

    /** Unique key used in persistence and UI selection. */
    val key: String

    /** Human-readable name for the UI. */
    val displayName: String

    /**
     * Execute the strategy.
     *
     * @param context session preferences and progress callbacks
     * @return the result of the organizing attempt
     */
    suspend fun organize(context: OrganizeSessionContext): StrategyResult
}
