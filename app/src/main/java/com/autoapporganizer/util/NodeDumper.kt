package com.autoapporganizer.util

import android.graphics.Rect
import android.view.accessibility.AccessibilityNodeInfo

/**
 * Pretty-prints an accessibility node tree for diagnostics.
 *
 * Extracted from [com.autoapporganizer.service.AutoAppOrganizerService] (#6) because it is
 * a pure, self-contained diagnostic utility: it only reads from [AccessibilityNodeInfo] and
 * writes to [DiagnosticLogger], with no dependency on the service's state or other methods.
 *
 * The dumper uses a "class name + screen bounds" fingerprint to detect already-visited
 * nodes, which is more stable than [AccessibilityNodeInfo.hashCode] (the system may recycle
 * and reuse node objects between scans, so identity-based visited sets are unreliable).
 *
 * @param tag Log tag passed through to [DiagnosticLogger].
 */
object NodeDumper {

    /**
     * Print [node] and its descendants up to [maxDepth] levels deep. Leaf nodes are always
     * printed regardless of depth, so no information is lost at the boundary.
     *
     * Safe to call with `node == null` — logs an error and returns.
     */
    fun dump(node: AccessibilityNodeInfo?, maxDepth: Int = 4, tag: String = "NodeDumper") {
        if (node == null) {
            DiagnosticLogger.error(tag, "dumpNodeTree: node is null")
            return
        }

        DiagnosticLogger.info(tag, "========== 节点树转储 (maxDepth=$maxDepth) ==========")
        val total = dumpRecursive(node, 0, maxDepth, mutableSetOf(), tag)
        DiagnosticLogger.info(tag, "========== 总计 $total 个节点 ==========")
    }

    private fun dumpRecursive(
        node: AccessibilityNodeInfo,
        depth: Int,
        maxDepth: Int,
        visited: MutableSet<String>,
        tag: String
    ): Int {
        // 用「类名 + 屏幕坐标」作为节点指纹，避免 hashCode 碰撞导致节点被误判为已访问而跳过。
        val bounds = Rect().also { node.getBoundsInScreen(it) }
        val id = "${node.className}@${bounds.left},${bounds.top},${bounds.right},${bounds.bottom}"
        if (id in visited) return 0
        visited.add(id)

        val indent = "  ".repeat(depth)
        val cls = node.className?.toString()?.substringAfterLast('.') ?: "?"
        val pkg = node.packageName?.toString() ?: ""
        val text = (node.text?.toString() ?: "").take(30)
        val desc = (node.contentDescription?.toString() ?: "").take(30)
        val bStr = "[${bounds.left},${bounds.top}-${bounds.right},${bounds.bottom}]"

        val flags = mutableListOf<String>()
        if (node.isClickable) flags.add("CLICK")
        if (node.isFocusable) flags.add("FOCUS")
        if (node.isEnabled) flags.add("EN")
        if (node.isScrollable) flags.add("SCROLL")
        if (node.childCount > 0) flags.add("children=${node.childCount}")

        // 只在关键深度或叶子节点时打印
        if (depth <= maxDepth || node.childCount == 0) {
            val flagStr = if (flags.isNotEmpty()) " [${flags.joinToString(",")}]" else ""
            DiagnosticLogger.debug(tag, "$indent$cls pkg=$pkg text='$text' desc='$desc' $bStr$flagStr")
        }

        var count = 1
        if (depth < maxDepth) {
            for (i in 0 until node.childCount) {
                val child = node.getChild(i) ?: continue
                count += dumpRecursive(child, depth + 1, maxDepth, visited, tag)
                child.recycle()
            }
        }
        return count
    }
}
