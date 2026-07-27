package com.autoapporganizer.core.perception

import android.accessibilityservice.AccessibilityService
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.view.Display
import android.view.accessibility.AccessibilityNodeInfo
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume

/**
 * Source of ground-truth screen information coming directly from the platform
 * AccessibilityService: the node tree ([scanElements]) and screen captures ([screenshot]).
 */
interface AccessibilityChannel {

    /**
     * Walk the currently active window's accessibility tree and return the interactable
     * elements (icons/buttons) found, as [ScreenElement]s with [ScreenElement.Source.ACCESSIBILITY].
     *
     * Renamed from `scan()` to disambiguate from [VisionChannel.detectIcons] (which invokes
     * a cloud VLM) and from `AutoAppOrganizerService.scanDesktop()` (which returns
     * `DesktopItem`s, not `ScreenElement`s). All three were previously called `scan()`
     * despite returning different types and having different costs.
     */
    suspend fun scanElements(): List<ScreenElement>

    /**
     * Capture a screenshot of the default display. Returns `null` when the platform API
     * is unavailable (pre-API 30) or the capture fails/times out.
     */
    suspend fun screenshot(): Bitmap?
}

/**
 * [AccessibilityChannel] backed by a real [AccessibilityService].
 *
 * @param service The hosting accessibility service used to read the node tree and take screenshots.
 */
class AccessibilityChannelImpl(private val service: AccessibilityService) : AccessibilityChannel {

    companion object {
        private const val TAG = "AccessibilityChannel"

        /** Minimum side length (px) for a node to be considered an icon-sized target. */
        private const val MIN_SIDE = 40
        /** Maximum side length (px) for a node to be considered an icon-sized target. */
        private const val MAX_SIDE = 800
        /** Screenshot capture timeout. */
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
    }

    override suspend fun scanElements(): List<ScreenElement> {
        val elements = mutableListOf<ScreenElement>()
        var root: AccessibilityNodeInfo? = null
        try {
            root = service.rootInActiveWindow
            if (root == null) {
                DiagnosticLogger.warn(TAG, "rootInActiveWindow is null")
                return emptyList()
            }
            traverse(root, elements)
            DiagnosticLogger.debug(TAG, "Scan found ${elements.size} candidate element(s)")
            if (elements.isNotEmpty()) {
                elements.forEach { el ->
                    DiagnosticLogger.debug(
                        TAG,
                        "  element: label='${el.label}' bounds=${el.bounds} pkg=${el.packageName}"
                    )
                }
            }
        } catch (e: Exception) {
            DiagnosticLogger.error(TAG, "scan failed: ${e.message}")
        } finally {
            try {
                root?.recycle()
            } catch (e: Exception) {
                DiagnosticLogger.warn(TAG, "Failed to recycle root node: ${e.message}")
            }
        }
        return elements
    }

    /**
     * Depth-first traversal of the node tree. A node is collected when it is clickable and
     * carries a usable label (content description or text) within the configured size range.
     */
    private fun traverse(node: AccessibilityNodeInfo, out: MutableList<ScreenElement>) {
        try {
            val bounds = Rect()
            node.getBoundsInScreen(bounds)
            val w = bounds.width()
            val h = bounds.height()

            val label = node.contentDescription?.toString()?.takeIf { it.isNotBlank() }
                ?: node.text?.toString()?.takeIf { it.isNotBlank() }

            if (node.isClickable && label != null && w in MIN_SIDE..MAX_SIDE && h in MIN_SIDE..MAX_SIDE) {
                out.add(
                    ScreenElement(
                        id = "a11y_${node.hashCode()}",
                        label = label,
                        bounds = bounds,
                        confidence = 1f,
                        source = ScreenElement.Source.ACCESSIBILITY,
                        packageName = node.packageName?.toString()
                    )
                )
            }
        } catch (e: Exception) {
            // A single bad node should never abort the whole scan.
            DiagnosticLogger.warn(TAG, "Skipping node during traversal: ${e.message}")
        }

        for (i in 0 until node.childCount) {
            val child = try {
                node.getChild(i)
            } catch (e: Exception) {
                DiagnosticLogger.warn(TAG, "getChild($i) failed: ${e.message}")
                null
            }
            if (child != null) {
                traverse(child, out)
            }
        }
    }

    override suspend fun screenshot(): Bitmap? {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            DiagnosticLogger.warn(TAG, "takeScreenshot requires API 30+ (current=${Build.VERSION.SDK_INT})")
            return null
        }
        return withTimeoutOrNull(SCREENSHOT_TIMEOUT_MS) {
            suspendCancellableCoroutine<Bitmap?> { cont ->
                try {
                    service.takeScreenshot(
                        Display.DEFAULT_DISPLAY,
                        ContextCompat.getMainExecutor(service),
                        object : AccessibilityService.TakeScreenshotCallback {
                            override fun onSuccess(result: AccessibilityService.ScreenshotResult) {
                                val bitmap = result.bitmap
                                DiagnosticLogger.debug(
                                    TAG,
                                    "Screenshot captured: ${bitmap.width}x${bitmap.height}"
                                )
                                result.close()
                                if (cont.isActive) cont.resume(bitmap)
                            }

                            override fun onFailure(errorCode: Int) {
                                DiagnosticLogger.error(
                                    TAG,
                                    "takeScreenshot onFailure errorCode=$errorCode",
                                    null
                                )
                                if (cont.isActive) cont.resume(null)
                            }
                        }
                    )
                } catch (e: Exception) {
                    DiagnosticLogger.error(TAG, "takeScreenshot threw: ${e.message}")
                    if (cont.isActive) cont.resume(null)
                }
            }
        }.also {
            if (it == null) DiagnosticLogger.warn(TAG, "Screenshot capture returned null (timeout or failure)")
        }
    }
}
