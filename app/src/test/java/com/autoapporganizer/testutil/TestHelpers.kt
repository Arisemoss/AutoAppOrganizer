package com.autoapporganizer.testutil

import android.graphics.Rect
import android.util.DisplayMetrics
import org.mockito.Mockito

/**
 * Test helpers for creating Android framework objects that throw "Stub!"
 * when instantiated directly in unit tests.
 */
object TestHelpers {

    /**
     * Create a mocked [Rect] with the given bounds and properly set up
     * computed methods (width, height, centerX, centerY, etc.).
     */
    fun mockRect(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): Rect {
        val rect = Mockito.mock(Rect::class.java)
        setRectFields(rect, left, top, right, bottom)
        return rect
    }

    /**
     * Set the bounds of an already-mocked [Rect] and update computed methods.
     */
    fun setRectFields(rect: Rect, left: Int, top: Int, right: Int, bottom: Int) {
        setField(rect, "left", left)
        setField(rect, "top", top)
        setField(rect, "right", right)
        setField(rect, "bottom", bottom)

        val w = right - left
        val h = bottom - top
        val cx = (left + right) / 2
        val cy = (top + bottom) / 2
        val ecx = (left + right) / 2f
        val ecy = (top + bottom) / 2f

        Mockito.`when`(rect.width()).thenReturn(w)
        Mockito.`when`(rect.height()).thenReturn(h)
        Mockito.`when`(rect.centerX()).thenReturn(cx)
        Mockito.`when`(rect.centerY()).thenReturn(cy)
        Mockito.`when`(rect.exactCenterX()).thenReturn(ecx)
        Mockito.`when`(rect.exactCenterY()).thenReturn(ecy)
        Mockito.`when`(rect.isEmpty).thenReturn(left >= right || top >= bottom)
    }

    /**
     * Create a mocked [DisplayMetrics] with the given pixel dimensions.
     */
    fun mockDisplayMetrics(
        widthPixels: Int = 1080,
        heightPixels: Int = 2400,
        density: Float = 1f,
        densityDpi: Int = 160
    ): DisplayMetrics {
        val dm = Mockito.mock(DisplayMetrics::class.java)
        setField(dm, "widthPixels", widthPixels)
        setField(dm, "heightPixels", heightPixels)
        setField(dm, "density", density)
        setField(dm, "densityDpi", densityDpi)
        setField(dm, "scaledDensity", density)
        setField(dm, "xdpi", densityDpi.toFloat())
        setField(dm, "ydpi", densityDpi.toFloat())
        return dm
    }

    private fun setField(obj: Any, name: String, value: Any) {
        val field = obj.javaClass.getDeclaredField(name).apply { isAccessible = true }
        when (value) {
            is Int -> field.setInt(obj, value)
            is Float -> field.setFloat(obj, value)
            is Boolean -> field.setBoolean(obj, value)
            else -> field.set(obj, value)
        }
    }
}