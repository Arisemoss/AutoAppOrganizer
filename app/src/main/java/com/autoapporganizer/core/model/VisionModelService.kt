package com.autoapporganizer.core.model

import android.graphics.Bitmap

/**
 * Abstraction over a vision-language model (VLM) capable of analysing a screen [Bitmap]
 * together with a textual [prompt] and returning a [VisionResult].
 *
 * Implementations are expected to be safe to call from a coroutine context; [analyze] is
 * suspending so implementations may perform network I/O off the main thread.
 */
interface VisionModelService {

    /**
     * `true` when a provider is configured and ready to serve requests (e.g. a non-"none"
     * provider plus a non-empty API key). Implementations should make this check cheap so
     * callers can gate expensive work behind it.
     */
    val isAvailable: Boolean

    /**
     * Run vision analysis on [bitmap] using [prompt].
     *
     * @param bitmap The screen image to analyse.
     * @param prompt The instruction/question for the VLM.
     * @return A [VisionResult.Success] with detected items, or a [VisionResult.Error].
     */
    suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult
}
