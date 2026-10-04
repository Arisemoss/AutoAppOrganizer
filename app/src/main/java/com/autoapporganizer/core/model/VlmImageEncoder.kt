package com.autoapporganizer.core.model

import android.graphics.Bitmap
import android.util.Base64
import java.io.ByteArrayOutputStream

/**
 * Prepares screenshots for VLM upload.
 *
 * Full-resolution PNG captures of a 2K+ screen encode to several MB of base64
 * (slow upload, memory spikes, wasted tokens — the models downstream resize the
 * image anyway). Screenshots taken via `takeScreenshot` come back in
 * [Bitmap.Config.HARDWARE], which cannot be compressed directly.
 *
 * The encoder copies hardware bitmaps into software memory, downscales so the
 * longest edge is at most [MAX_DIMENSION_PX], and emits a quality-[JPEG_QUALITY]
 * JPEG as base64. Icon-scale detail survives: a 1440p screen at 1280 px keeps
 * ~88% linear resolution, far above what a VLM consumes.
 */
internal object VlmImageEncoder {

    private const val MAX_DIMENSION_PX = 1280
    private const val JPEG_QUALITY = 80

    fun encodeToJpegBase64(bitmap: Bitmap): String {
        val software = if (bitmap.config == Bitmap.Config.HARDWARE) {
            bitmap.copy(Bitmap.Config.ARGB_8888, false) ?: bitmap
        } else {
            bitmap
        }

        val scaled = downscale(software)
        try {
            val baos = ByteArrayOutputStream()
            scaled.compress(Bitmap.CompressFormat.JPEG, JPEG_QUALITY, baos)
            return Base64.encodeToString(baos.toByteArray(), Base64.NO_WRAP)
        } finally {
            // Only recycle what we own and what is not the caller's original.
            if (scaled !== software && scaled !== bitmap) scaled.recycle()
            if (software !== bitmap) software.recycle()
        }
    }

    private fun downscale(bitmap: Bitmap): Bitmap {
        val longest = maxOf(bitmap.width, bitmap.height)
        if (longest <= MAX_DIMENSION_PX) return bitmap
        val scale = MAX_DIMENSION_PX.toFloat() / longest
        val w = (bitmap.width * scale).toInt().coerceAtLeast(1)
        val h = (bitmap.height * scale).toInt().coerceAtLeast(1)
        return Bitmap.createScaledBitmap(bitmap, w, h, true)
    }
}
