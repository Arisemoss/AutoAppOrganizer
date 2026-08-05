package com.autoapporganizer.core.perception

import android.accessibilityservice.AccessibilityService
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

/**
 * Unit tests for [AccessibilityChannelImpl], focusing on the node-tree traversal
 * ([AccessibilityChannelImpl.traverse]).
 *
 * The traversal walks the accessibility tree and collects clickable, labeled nodes
 * within a valid size range as [ScreenElement]s. These tests verify that:
 *  - Clickable children with labels are correctly extracted.
 *  - Non-clickable or unlabeled nodes are skipped.
 *  - Multi-level (grandchild) trees are fully traversed.
 *  - Out-of-bounds nodes (too small / too large) are filtered out.
 *
 * Regression context: a prior version of `traverse` did not recycle child
 * [AccessibilityNodeInfo] objects after traversal, leaking node handles on every
 * scan call (up to 60 per organize session). The fix adds `child.recycle()` after
 * the recursive call, matching the pattern in `AutoAppOrganizerService.traverseNodes`
 * and `NodeDumper.dumpRecursive`.
 */
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class AccessibilityChannelTest {

    /** Minimal AccessibilityService subclass for constructing AccessibilityChannelImpl. */
    private class TestService : AccessibilityService() {
        override fun onAccessibilityEvent(event: AccessibilityEvent?) {}
        override fun onInterrupt() {}
    }

    private fun createChannel(): AccessibilityChannelImpl {
        val service = Robolectric.buildService(TestService::class.java).create().get()
        return AccessibilityChannelImpl(service)
    }

    private fun makeNode(
        label: String? = null,
        clickable: Boolean = false,
        bounds: Rect = Rect(0, 0, 100, 100),
        packageName: String? = null
    ): AccessibilityNodeInfo {
        val node = AccessibilityNodeInfo.obtain()
        node.isClickable = clickable
        if (label != null) node.contentDescription = label
        node.setBoundsInScreen(Rect(bounds))
        if (packageName != null) node.packageName = packageName
        return node
    }

    @Test
    fun `traverse extracts clickable labeled children`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        val child1 = makeNode(label = "Settings", clickable = true, bounds = Rect(100, 100, 200, 200))
        val child2 = makeNode(label = "Camera", clickable = true, bounds = Rect(300, 100, 400, 200))
        root.addChild(child1)
        root.addChild(child2)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(2, out.size)
        val labels = out.map { it.label }
        assertTrue("Settings should be extracted", labels.contains("Settings"))
        assertTrue("Camera should be extracted", labels.contains("Camera"))
        assertEquals(ScreenElement.Source.ACCESSIBILITY, out[0].source)
    }

    @Test
    fun `traverse skips non-clickable nodes`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        val clickable = makeNode(label = "Clickable", clickable = true, bounds = Rect(100, 100, 200, 200))
        val notClickable = makeNode(label = "NotClickable", clickable = false, bounds = Rect(300, 100, 400, 200))
        root.addChild(clickable)
        root.addChild(notClickable)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(1, out.size)
        assertEquals("Clickable", out[0].label)
    }

    @Test
    fun `traverse skips nodes without labels`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        val withLabel = makeNode(label = "WithLabel", clickable = true, bounds = Rect(100, 100, 200, 200))
        val noLabel = makeNode(label = null, clickable = true, bounds = Rect(300, 100, 400, 200))
        root.addChild(withLabel)
        root.addChild(noLabel)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(1, out.size)
        assertEquals("WithLabel", out[0].label)
    }

    @Test
    fun `traverse handles multi-level trees`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        val container = makeNode(label = "Container", clickable = false, bounds = Rect(0, 0, 1080, 1200))
        val grandchild = makeNode(label = "DeepApp", clickable = true, bounds = Rect(500, 500, 600, 600))
        root.addChild(container)
        container.addChild(grandchild)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(1, out.size)
        assertEquals("DeepApp", out[0].label)
    }

    @Test
    fun `traverse filters out-of-bounds nodes`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        // Too small (< 40px)
        val tooSmall = makeNode(label = "Tiny", clickable = true, bounds = Rect(100, 100, 120, 120))
        // Valid size
        val valid = makeNode(label = "Valid", clickable = true, bounds = Rect(100, 100, 200, 200))
        // Too large (> 800px)
        val tooLarge = makeNode(label = "Huge", clickable = true, bounds = Rect(0, 0, 900, 900))
        root.addChild(tooSmall)
        root.addChild(valid)
        root.addChild(tooLarge)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(1, out.size)
        assertEquals("Valid", out[0].label)
    }

    @Test
    fun `traverse handles empty tree`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        // Root is not clickable, no children

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertTrue(out.isEmpty())
    }

    @Test
    fun `traverse preserves package name from node`() {
        val channel = createChannel()

        val root = makeNode(bounds = Rect(0, 0, 1080, 2400))
        val child = makeNode(
            label = "TestApp",
            clickable = true,
            bounds = Rect(100, 100, 200, 200),
            packageName = "com.example.testapp"
        )
        root.addChild(child)

        val out = mutableListOf<ScreenElement>()
        channel.traverse(root, out)

        assertEquals(1, out.size)
        assertEquals("com.example.testapp", out[0].packageName)
    }
}
