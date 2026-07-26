package com.autoapporganizer.core.strategy

/**
 * Traditional accessibility-only strategy.
 *
 * This strategy delegates to the existing desktop-organizer implementation,
 * preserving the battle-tested categorization and drag logic.
 */
class LegacyStrategy(
    private val organizer: LegacyOrganizer
) : OrganizeStrategy {

    override val key = "legacy"
    override val displayName = "传统无障碍"

    override suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在使用传统模式整理…")
        return organizer.organizeDesktop()
    }
}
