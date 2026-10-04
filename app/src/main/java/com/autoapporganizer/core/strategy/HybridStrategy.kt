package com.autoapporganizer.core.strategy

import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.CancellationException

/**
 * Hybrid strategy: try the vision-driven organizer first, and fall back to the
 * legacy accessibility organizer if the vision model is unavailable or fails.
 */
class HybridStrategy(
    private val visionOrganizer: VisionOrganizer,
    private val legacyOrganizer: LegacyOrganizer
) : OrganizeStrategy {

    companion object {
        private const val TAG = "HybridStrategy"
    }

    override val key = "hybrid"
    override val displayName = "混合增强（推荐）"

    override suspend fun organize(context: OrganizeSessionContext): StrategyResult {
        context.onProgress(5, "正在尝试视觉 AI 模式…")

        // runCatching 会连 CancellationException 一起吞掉：5 分钟总超时触发时，
        // 视觉路径的取消会先被转成 legacy 回退而不是立即终止。显式放行取消。
        val visionResult = try {
            visionOrganizer.organizeByVision()
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "Vision organizer crashed: ${e.message}")
            StrategyResult(false, "视觉模块异常：${e.message}", 0, 0)
        }

        // 保留建出过文件夹的部分成果；否则回退 legacy 整理。
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
}
