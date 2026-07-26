package com.autoapporganizer.core

import com.autoapporganizer.core.strategy.HybridStrategy
import com.autoapporganizer.core.strategy.LegacyOrganizer
import com.autoapporganizer.core.strategy.LegacyStrategy
import com.autoapporganizer.core.strategy.OrganizeSessionContext
import com.autoapporganizer.core.strategy.OrganizeStrategy
import com.autoapporganizer.core.strategy.StrategyResult
import com.autoapporganizer.core.strategy.VisionOrganizer
import com.autoapporganizer.core.strategy.VisionStrategy
import com.autoapporganizer.util.PrefsManager

/**
 * Single entry point for organizing the home screen.
 *
 * The facade selects the active [OrganizeStrategy] based on user preferences
 * and dispatches the request. This keeps UI and service code decoupled from
 * the strategy implementations.
 */
class OrganizerFacade(
    private val prefs: PrefsManager,
    legacyOrganizer: LegacyOrganizer,
    visionOrganizer: VisionOrganizer
) {

    private val strategies: Map<String, OrganizeStrategy> = listOf(
        LegacyStrategy(legacyOrganizer),
        VisionStrategy(visionOrganizer),
        HybridStrategy(visionOrganizer, legacyOrganizer)
    ).associateBy { it.key }

    /** All available strategies, useful for settings UI. */
    val availableStrategies: List<OrganizeStrategy> = strategies.values.toList()

    /** Strategy currently selected in preferences (defaults to hybrid). */
    val activeStrategy: OrganizeStrategy
        get() = strategies[prefs.organizeStrategy] ?: strategies["hybrid"] ?: strategies.values.first()

    /**
     * Run the active organizing strategy.
     */
    suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        return activeStrategy.organize(context)
    }
}
