package com.autoapporganizer.core.strategy

/**
 * Abstraction for the traditional accessibility-only organizer.
 *
 * Implementations are typically the [AccessibilityService][android.accessibilityservice.AccessibilityService];
 * the interface lets strategies invoke the legacy flow without knowing the concrete class.
 */
interface LegacyOrganizer {

    /**
     * Run the original desktop organizing flow (home → scan → categorize → drag into folders).
     *
     * @return a [StrategyResult] describing how many folders were created.
     */
    suspend fun organizeDesktop(): StrategyResult
}
