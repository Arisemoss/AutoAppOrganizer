package com.autoapporganizer.core.strategy

/**
 * Abstraction for the vision-driven organizer.
 *
 * Implementations are typically the [AccessibilityService][android.accessibilityservice.AccessibilityService]
 * that coordinates screenshots, the VLM, and gesture execution.
 */
interface VisionOrganizer {

    /**
     * Run the vision-driven desktop organizing flow.
     *
     * @return a [StrategyResult] describing the outcome.
     */
    suspend fun organizeByVision(): StrategyResult
}
