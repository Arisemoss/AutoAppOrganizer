package com.autoapporganizer.core.strategy

import com.autoapporganizer.util.DiagnosticLogger

/**
 * Hybrid strategy: try the vision-driven organizer first, and fall back to the
 * legacy accessibility organizer if the vision model is unavailable or fails
 * repeatedly.
 */
class HybridStrategy(
    private val visionOrganizer: VisionOrganizer,
    private val legacyOrganizer: LegacyOrganizer,
    private val maxVisionFailures: Int = 2
) : OrganizeStrategy {

    companion object {
        private const val TAG = "HybridStrategy"
    }

    override val key = "hybrid"
    override val displayName = "混合增强（推荐）"

    override suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在尝试视觉 AI 模式…")

        val visionResult = runCatching { visionOrganizer.organizeByVision() }
            .getOrElse { e ->
                DiagnosticLogger.error(TAG, "Vision organizer crashed: ${e.message}")
                StrategyResult(false, "视觉模块异常：${e.message}", 0, 0)
            }

        return if (visionResult.success || shouldUseResult(visionResult)) {
            visionResult
        } else {
            DiagnosticLogger.warn(
                TAG,
                "Vision failed (${visionResult.message}), falling back to legacy organizer"
            )
            context.onProgress(10, "视觉模式不可用，正在切换到传统模式…")
            legacyOrganizer.organizeDesktop()
        }
    }

    /**
     * Heuristic: keep the vision result if it created folders, even if it reported
     * partial failure; otherwise trust the success flag.
     */
    private fun shouldUseResult(result: StrategyResult): Boolean =
        result.success || result.foldersCreated > 0
}
