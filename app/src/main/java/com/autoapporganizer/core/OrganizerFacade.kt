package com.autoapporganizer.core

import com.autoapporganizer.core.strategy.LegacyOrganizer
import com.autoapporganizer.core.strategy.OrganizeSessionContext
import com.autoapporganizer.core.strategy.StrategyResult
import com.autoapporganizer.core.strategy.VisionOrganizer
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.PrefsManager

/**
 * Single entry point for organizing the home screen.
 *
 * Selects the active strategy based on user preferences and dispatches the request.
 * Available strategies:
 *  - "hybrid": try vision first, fall back to legacy
 *  - "vision": vision-only via VLM
 *  - "legacy": accessibility-only
 */
class OrganizerFacade(
    private val prefs: PrefsManager,
    private val legacyOrganizer: LegacyOrganizer,
    private val visionOrganizer: VisionOrganizer
) {
    companion object {
        private const val TAG = "OrganizerFacade"

        const val STRATEGY_HYBRID = "hybrid"
        const val STRATEGY_VISION = "vision"
        const val STRATEGY_LEGACY = "legacy"

        /** All available strategy keys for settings UI. */
        val STRATEGY_KEYS = listOf(STRATEGY_HYBRID, STRATEGY_VISION, STRATEGY_LEGACY)
    }

    /**
     * Run the organizing strategy selected in preferences.
     */
    suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        return when (prefs.organizeStrategy) {
            STRATEGY_VISION -> runVision(context)
            STRATEGY_LEGACY -> runLegacy(context)
            else -> runHybrid(context) // default: hybrid
        }
    }

    private suspend fun runHybrid(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在尝试视觉 AI 模式…")

        val visionResult = runCatching { visionOrganizer.organizeByVision() }
            .getOrElse { e ->
                DiagnosticLogger.error(TAG, "Vision organizer crashed: ${e.message}")
                StrategyResult(false, "视觉模块异常：${e.message}", 0, 0)
            }

        return if (visionResult.success || visionResult.foldersCreated > 0) {
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

    private suspend fun runVision(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在使用视觉 AI 模式整理…")
        return visionOrganizer.organizeByVision()
    }

    private suspend fun runLegacy(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在使用传统模式整理…")
        return legacyOrganizer.organizeDesktop()
    }
}