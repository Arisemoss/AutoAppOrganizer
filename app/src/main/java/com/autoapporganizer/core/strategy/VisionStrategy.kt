package com.autoapporganizer.core.strategy

/**
 * Pure vision-driven strategy.
 *
 * It relies entirely on the vision model to perceive the screen, plan actions,
 * and execute them. If the vision layer is unavailable, the organizer should
 * return a failed result; callers who need automatic fallback should use
 * [HybridStrategy].
 */
class VisionStrategy(
    private val organizer: VisionOrganizer
) : OrganizeStrategy {

    override val key = "vision"
    override val displayName = "视觉 AI"

    override suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在使用视觉 AI 模式整理…")
        return organizer.organizeByVision()
    }
}
