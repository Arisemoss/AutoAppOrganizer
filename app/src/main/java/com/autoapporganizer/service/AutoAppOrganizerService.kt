package com.autoapporganizer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.app.usage.UsageStatsManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Path
import android.graphics.Rect
import android.os.Build
import android.provider.Settings
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.autoapporganizer.model.DesktopBackup
import com.autoapporganizer.model.DesktopItem
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.*

/**
 * 桌面整理无障碍服务 — Android 15 适配版
 *
 * 核心改进：
 * 1. 强制切回桌面 + 等待窗口稳定（解决扫描自己窗口的问题）
 * 2. 通用图标识别（不依赖类名，靠 childCount + clickable + contentDescription）
 * 3. dumpNodeTree() 深度诊断
 * 4. UsageStatsManager 辅助分类（常用/非常用）
 * 5. 小米 MIUI 专属适配
 */
class AutoAppOrganizerService : AccessibilityService() {

    interface OrganizeCallback {
        fun onProgress(progress: Int, message: String)
        fun onComplete(success: Boolean, folderCount: Int, message: String)
    }

    companion object {
        private const val TAG = "AutoOrganizerService"

        /** 长按保持时长（ms）—— 需大于多数 Launcher 的长按阈值（约 400ms） */
        private const val LONG_PRESS_MS = 600L

        /** 拖动时长（ms） */
        private const val DRAG_MS = 500L

        /** 「不常用」阈值：7 天内前台时长低于此值（1 分钟）的应用归入不常用 */
        private const val RARELY_USED_THRESHOLD_MS = 60_000L

        /** 已知桌面包名列表（检测当前窗口用）—— 必须与 accessibility_service_config.xml 的 packageNames 保持一致 */
        val LAUNCHER_PACKAGES = setOf(
            "com.miui.home",           // 小米 MIUI / HyperOS
            "com.android.launcher",    // AOSP / Pixel
            "com.google.android.apps.nexuslauncher", // Pixel Launcher
            "com.sec.android.app.launcher",           // 三星 OneUI
            "com.huawei.android.launcher",            // 华为 EMUI
            "com.oppo.launcher",       // OPPO ColorOS
            "com.vivo.launcher",       // vivo
            "com.bbk.launcher2",       // vivo (旧)
            "net.oneplus.launcher",    // OnePlus
            "com.teslacoilsw.launcher",// Nova
            "ch.deletescape.lawnchair",// Lawnchair
            "com.coloros.launcher",    // OPPO ColorOS 新版
            "com.funtouch.launcher",   // vivo FuntouchOS
            "com.android.launcher3",   // Launcher3 (AOSP/Lineage)
        )

        var instance: AutoAppOrganizerService? = null
            private set

        var isOrganizing = false
            private set

        var organizeProgress: Int = 0
            private set

        var organizeCallback: OrganizeCallback? = null

        /** 上次扫描到的桌面包名 */
        var detectedLauncherPkg: String? = null
            private set
    }

    private val serviceScope = MainScope()
    private lateinit var categoryMatcher: CategoryMatcher
    private lateinit var backupManager: BackupManager
    private lateinit var usageStatsManager: UsageStatsManager

    private var currentBackup: DesktopBackup? = null

    override fun onCreate() {
        super.onCreate()
        instance = this
        categoryMatcher = CategoryMatcher(this)
        backupManager = BackupManager(this)
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this

        DiagnosticLogger.clear()
        DiagnosticLogger.info(TAG, "服务已连接")
        DiagnosticLogger.info(TAG, "设备: ${Build.MANUFACTURER} ${Build.MODEL}")
        DiagnosticLogger.info(TAG, "Android: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
        DiagnosticLogger.info(TAG, "品牌: ${Build.BRAND}")

        val info = serviceInfo ?: AccessibilityServiceInfo().also { setServiceInfo(it) }
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        // 注意：不开启 FLAG_REQUEST_TOUCH_EXPLORATION_MODE —— 触摸探索模式会改变
        // 触摸事件分发方式，与 dispatchGesture 派发的拖拽手势冲突，导致图标无法被拖动。
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                AccessibilityServiceInfo.FLAG_REPORT_VIEW_IDS or
                AccessibilityServiceInfo.FLAG_RETRIEVE_INTERACTIVE_WINDOWS
        setServiceInfo(info)

        DiagnosticLogger.info(TAG, "服务配置完成")
    }

    override fun onDestroy() {
        super.onDestroy()
        instance = null
        serviceScope.cancel()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        // 仅用于监控窗口变化 — 核心逻辑在主动调用中
        val pkg = event?.packageName?.toString() ?: return
        if (event.eventType == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED && pkg in LAUNCHER_PACKAGES) {
            detectedLauncherPkg = pkg
        }
    }

    override fun onInterrupt() {}

    // ──────────────────────────────────────────────
    // 公开接口
    // ──────────────────────────────────────────────

    /** 统一上报进度：同时更新 organizeProgress 字段并通知回调 */
    private fun reportProgress(progress: Int, message: String) {
        organizeProgress = progress.coerceIn(0, 100)
        organizeCallback?.onProgress(organizeProgress, message)
    }

    /** 开始整理桌面 */
    fun startOrganize() {
        if (isOrganizing) return

        serviceScope.launch {
            isOrganizing = true
            organizeProgress = 0

            try {
                // ① 强制返回桌面
                reportProgress(5, "正在返回桌面…")
                val onDesktop = goToHomeScreen()
                if (!onDesktop) {
                    organizeCallback?.onComplete(false, 0, "无法切换到桌面，请手动返回桌面后重试")
                    return@launch
                }
                DiagnosticLogger.info(TAG, "已确认在桌面: $detectedLauncherPkg")

                // ② 备份当前桌面
                reportProgress(10, "正在备份桌面…")
                currentBackup = backupDesktop()
                if (currentBackup != null) {
                    backupManager.saveBackup(currentBackup!!)
                }

                // ③ 扫描并解析桌面图标
                reportProgress(30, "正在分析桌面图标…")
                val desktopItems = scanDesktop()
                if (desktopItems.isEmpty()) {
                    // 自动触发深度诊断
                    DiagnosticLogger.info(TAG, "=== 自动深度诊断 ===")
                    val diagRoot = rootInActiveWindow
                    dumpNodeTree(diagRoot)
                    diagRoot?.recycle()
                    organizeCallback?.onComplete(false, 0, "未找到桌面图标 — 请查看诊断日志")
                    return@launch
                }

                // ④ 智能分类
                reportProgress(50, "正在智能分类…")
                val categorized = categorizeItems(desktopItems)

                // ⑤ 执行整理
                reportProgress(70, "正在整理桌面…")
                val folderCount = performOrganize(categorized)

                reportProgress(100, "整理完成")
                organizeCallback?.onComplete(true, folderCount, "整理完成，共创建 $folderCount 个文件夹")

            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "整理异常: ${e.message}")
                e.printStackTrace()
                organizeCallback?.onComplete(false, 0, "整理失败: ${e.message}")
            } finally {
                isOrganizing = false
            }
        }
    }

    /** 撤销整理 */
    fun undoOrganize() {
        serviceScope.launch {
            val backup = backupManager.loadBackup() ?: currentBackup
            if (backup == null) {
                organizeCallback?.onComplete(false, 0, "没有备份数据")
                return@launch
            }
            reportProgress(10, "正在返回桌面…")
            val onDesktop = goToHomeScreen()
            if (!onDesktop) {
                organizeCallback?.onComplete(false, 0, "无法切换到桌面，撤销失败")
                return@launch
            }
            reportProgress(40, "正在扫描已创建的文件夹…")
            val dissolved = dissolveFolders()
            reportProgress(100, "还原完成")
            organizeCallback?.onComplete(
                true, dissolved,
                if (dissolved > 0) "已尝试还原 $dissolved 个文件夹" else "未发现可还原的文件夹"
            )
        }
    }

    /** 运行纯诊断（不整理） */
    fun runDiagnostic() {
        if (isOrganizing) return
        serviceScope.launch {
            try {
                DiagnosticLogger.clear()
                DiagnosticLogger.info(TAG, "=== 开始诊断扫描 ===")
                delay(300)

                val root = rootInActiveWindow
                if (root == null) {
                    DiagnosticLogger.error(TAG, "rootInActiveWindow 为 null")
                    return@launch
                }

                DiagnosticLogger.info(TAG, "当前窗口包名: ${root.packageName}")
                DiagnosticLogger.info(TAG, "当前窗口类名: ${root.className}")
                DiagnosticLogger.info(TAG, "是否桌面: ${root.packageName in LAUNCHER_PACKAGES}")

                dumpNodeTree(root)
                val items = scanDesktop()

                DiagnosticLogger.info(TAG, "=== 诊断完成: 找到 ${items.size} 个APP ===")
                items.forEachIndexed { i, item ->
                    DiagnosticLogger.info(TAG, "  [${i+1}] ${item.name} → ${item.packageName ?: "?"}")
                }

                if (items.isEmpty() && root.packageName !in LAUNCHER_PACKAGES) {
                    DiagnosticLogger.warn(TAG, "当前不在桌面！请手动按 Home 返回桌面后重试")
                }
                root.recycle()
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "诊断异常: ${e.message}")
            }
        }
    }

    fun isServiceEnabled() = instance != null

    // ──────────────────────────────────────────────
    // ① 强制返回桌面
    // ──────────────────────────────────────────────

    /**
     * 执行 GLOBAL_ACTION_HOME 并等待桌面窗口出现
     * @return true 如果确认已在桌面
     */
    private suspend fun goToHomeScreen(): Boolean {
        // 先检查是否已经在桌面
        val currentPkg = currentWindowPackage()
        if (currentPkg in LAUNCHER_PACKAGES) {
            detectedLauncherPkg = currentPkg
            DiagnosticLogger.info(TAG, "已在桌面: $currentPkg")
            return true
        }
        DiagnosticLogger.info(TAG, "当前窗口: $currentPkg → 切换到桌面")

        // 执行 HOME
        performGlobalAction(GLOBAL_ACTION_HOME)
        delay(600) // 等动画

        // 轮询等待桌面渲染（最长 3 秒）
        repeat(10) { attempt ->
            delay(300)
            val pkg = currentWindowPackage()
            if (pkg.isEmpty()) return@repeat
            DiagnosticLogger.debug(TAG, "轮询#${attempt+1}: 窗口=$pkg")

            if (pkg in LAUNCHER_PACKAGES) {
                detectedLauncherPkg = pkg
                // 额外等待桌面内容渲染
                delay(500)
                DiagnosticLogger.info(TAG, "桌面已就绪: $pkg")
                return true
            }
        }

        // 最后一次尝试
        val lastPkg = currentWindowPackage()
        if (lastPkg in LAUNCHER_PACKAGES) {
            detectedLauncherPkg = lastPkg
            delay(500)
            return true
        }

        DiagnosticLogger.warn(TAG, "无法确认桌面窗口。当前: $lastPkg")
        DiagnosticLogger.warn(TAG, "已知桌面包名: ${LAUNCHER_PACKAGES.joinToString()}")
        return false
    }

    /** 获取当前活动窗口包名（自动回收节点，避免泄漏） */
    private fun currentWindowPackage(): String {
        val root = rootInActiveWindow ?: return ""
        val pkg = root.packageName?.toString() ?: ""
        root.recycle()
        return pkg
    }

    // ──────────────────────────────────────────────
    // ② 通用图标扫描
    // ──────────────────────────────────────────────

    private fun scanDesktop(): List<DesktopItem> {
        val root = rootInActiveWindow
        if (root == null) {
            DiagnosticLogger.error(TAG, "rootInActiveWindow 为 null")
            return emptyList()
        }

        val rootPkg = root.packageName?.toString() ?: "未知"
        DiagnosticLogger.info(TAG, "当前窗口包名: $rootPkg")
        DiagnosticLogger.info(TAG, "当前窗口类名: ${root.className}")
        DiagnosticLogger.info(TAG, "根节点子节点数: ${root.childCount}")

        val classCounts = mutableMapOf<String, Int>()
        val items = mutableListOf<DesktopItem>()
        var totalNodes = 0
        var skippedNoName = 0
        val potentialNodes = mutableListOf<String>()

        traverseNodes(root) { node ->
            totalNodes++
            val cls = node.className?.toString() ?: ""
            classCounts[cls] = (classCounts[cls] ?: 0) + 1

            val item = parseNodeToItem(node)
            if (item != null && item.type == DesktopItem.ItemType.APP) {
                items.add(item)
                DiagnosticLogger.scan(TAG, "✓ APP: ${item.name} | clickable=${node.isClickable} | class=${cls.substringAfterLast('.')}")
            } else if (isPotentialIcon(node)) {
                skippedNoName++
                val name = node.contentDescription?.toString() ?: node.text?.toString() ?: "(无)"
                potentialNodes.add("  name='$name' class=${cls.substringAfterLast('.')} clickable=${node.isClickable} childCount=${node.childCount}")
            }
            true
        }

        // 输出统计
        DiagnosticLogger.info(TAG, "总节点: $totalNodes | 识别APP: ${items.size} | 跳过: $skippedNoName")
        DiagnosticLogger.info(TAG, "节点类名分布 (Top 15):")
        classCounts.entries
            .sortedByDescending { it.value }
            .take(15)
            .forEach { (cls, count) ->
                DiagnosticLogger.debug(TAG, "  ${cls.substringAfterLast('.')} × $count")
            }

        if (items.isEmpty()) {
            DiagnosticLogger.warn(TAG, "未找到任何APP图标！可能原因:")
            DiagnosticLogger.warn(TAG, "  1. 当前窗口不是桌面 (包名: $rootPkg)")
            DiagnosticLogger.warn(TAG, "  2. Launcher 使用非标准视图结构")
            DiagnosticLogger.warn(TAG, "  3. 权限不足 — 请检查无障碍、悬浮窗权限")
            DiagnosticLogger.info(TAG, "被跳过的可疑节点 (${potentialNodes.size}):")
            potentialNodes.take(20).forEach { DiagnosticLogger.debug(TAG, it) }
        }

        root.recycle()
        return items
    }

    /** 预检：节点是否有可能是图标 */
    private fun isPotentialIcon(node: AccessibilityNodeInfo): Boolean {
        val name = node.contentDescription?.toString() ?: node.text?.toString()
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        return !name.isNullOrEmpty() &&
            bounds.width() in 40..800 &&
            bounds.height() in 40..800
    }

    /** 解析节点为桌面项 */
    private fun parseNodeToItem(node: AccessibilityNodeInfo): DesktopItem? {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)

        if (isWidget(node)) {
            return DesktopItem(type = DesktopItem.ItemType.WIDGET, bounds = bounds)
        }

        if (isFolder(node)) {
            return DesktopItem(
                type = DesktopItem.ItemType.FOLDER,
                bounds = bounds,
                name = node.contentDescription?.toString()
            )
        }

        val name = node.contentDescription?.toString() ?: node.text?.toString()
        if (name != null && isAppIconUniversal(node, name)) {
            return DesktopItem(
                type = DesktopItem.ItemType.APP,
                name = name,
                bounds = bounds,
                packageName = node.packageName?.toString()
            )
        }

        return null
    }

    /**
     * 通用图标识别 — 不依赖特定类名
     *
     * 特征组合（满足以下 3 条即视为图标）：
     * 1. 有名称（contentDescription 或 text）
     * 2. 尺寸合理（40-800dp）
     * 3. 自身 clickable 或 子节点含 ImageView+TextView 或 父节点 clickable
     */
    private fun isAppIconUniversal(node: AccessibilityNodeInfo, name: String): Boolean {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        val w = bounds.width()
        val h = bounds.height()

        if (w !in 40..800 || h !in 40..800) return false

        val className = node.className?.toString() ?: ""

        // 排除容器类
        if (className.endsWith("RecyclerView") || className.endsWith("ListView") ||
            className.endsWith("ScrollView") || className.endsWith("GridView") ||
            className.endsWith("ViewPager") || className.endsWith("PageIndicator") ||
            className.endsWith("DockBar") || className.endsWith("HotSeat") ||
            className.contains("Workspace") && node.childCount > 4) {
            return false
        }

        // ① 自身可点击 → 很可能是图标
        if (node.isClickable) return true

        // ② 搜索子节点：有 ImageView 或 Icon 类 → 图标特征强
        var hasImageView = false
        var hasTextView = false
        for (i in 0 until minOf(node.childCount, 10)) {
            val child = node.getChild(i) ?: continue
            val cc = child.className?.toString() ?: ""
            if (cc.endsWith("ImageView") || cc.endsWith("FastBitmapDrawable") || cc.contains("Icon")) {
                hasImageView = true
            }
            if (cc.endsWith("TextView") || child.text?.isNotEmpty() == true) {
                hasTextView = true
            }
            child.recycle()
        }
        if (hasImageView && (hasTextView || name.isNotEmpty())) return true

        // ③ 父节点可点击
        val parent = node.parent
        if (parent != null && parent.isClickable && node.isEnabled) {
            parent.recycle()
            return true
        }
        parent?.recycle()

        // ④ MIUI 特殊处理：contentDescription 不为空 + 在合理范围内
        if (name.isNotEmpty() && w in 60..400 && h in 60..400) {
            // 小米桌面的图标节点可能只是一个可聚焦的 ViewGroup
            if (node.childCount in 1..4 && node.isFocusable) return true
        }

        return false
    }

    private fun isWidget(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString() ?: return false
        return className.contains("AppWidget") ||
               className.contains("widget") ||
               className.contains("Widget")
    }

    private fun isFolder(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""
        val text = node.text?.toString() ?: ""
        return desc.contains("文件夹") || text.contains("文件夹") ||
               (className.contains("Folder") && node.childCount >= 2)
    }

    // ──────────────────────────────────────────────
    // ④ dumpNodeTree() 深度调试
    // ──────────────────────────────────────────────

    /**
     * 打印当前窗口完整无障碍节点树（前 3 层 + 所有叶子节点）
     * 这是排查「无法分析桌面图标」的最强工具
     */
    fun dumpNodeTree(node: AccessibilityNodeInfo?, maxDepth: Int = 4) {
        if (node == null) {
            DiagnosticLogger.error(TAG, "dumpNodeTree: node is null")
            return
        }

        DiagnosticLogger.info(TAG, "========== 节点树转储 (maxDepth=$maxDepth) ==========")
        val total = dumpNodeRecursive(node, 0, maxDepth, mutableSetOf())
        DiagnosticLogger.info(TAG, "========== 总计 $total 个节点 ==========")
    }

    private fun dumpNodeRecursive(
        node: AccessibilityNodeInfo,
        depth: Int,
        maxDepth: Int,
        visited: MutableSet<String>
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
            DiagnosticLogger.debug(TAG, "$indent$cls pkg=$pkg text='$text' desc='$desc' $bStr$flagStr")
        }

        var count = 1
        if (depth < maxDepth) {
            for (i in 0 until node.childCount) {
                val child = node.getChild(i) ?: continue
                count += dumpNodeRecursive(child, depth + 1, maxDepth, visited)
                child.recycle()
            }
        }
        return count
    }

    // ──────────────────────────────────────────────
    // 遍历节点
    // ──────────────────────────────────────────────

    private fun traverseNodes(
        node: AccessibilityNodeInfo,
        callback: (AccessibilityNodeInfo) -> Boolean
    ) {
        if (!callback(node)) return
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            traverseNodes(child, callback)
            child.recycle() // 释放子节点，避免长期扫描导致节点泄漏
        }
    }

    // ──────────────────────────────────────────────
    // 备份
    // ──────────────────────────────────────────────

    private fun backupDesktop(): DesktopBackup? {
        val root = rootInActiveWindow ?: return null
        val items = mutableListOf<DesktopItem>()
        traverseNodes(root) { node ->
            val item = parseNodeToItem(node)
            if (item != null) items.add(item)
            true
        }
        root.recycle()
        return DesktopBackup(timestamp = System.currentTimeMillis(), screen = 0, items = items)
    }

    // ──────────────────────────────────────────────
    // ⑤ 智能分类（包名映射 + UsageStatsManager）
    // ──────────────────────────────────────────────

    private fun categorizeItems(items: List<DesktopItem>): Map<String, List<DesktopItem>> {
        val result = mutableMapOf<String, MutableList<DesktopItem>>()

        // 获取使用频率数据 —— 7 天内前台时长低于阈值的应用归入「不常用」
        val usageStats = getAppUsageStats()
        DiagnosticLogger.info(TAG, "使用统计可用包数: ${usageStats.size}")

        for (item in items) {
            // 优先通过包名分类（更精准）
            val baseCategory = if (item.packageName != null) {
                categorizeByPackageName(item.packageName)
            } else {
                categoryMatcher.matchCategory(item.name)
            }

            // 结合使用频率：几乎不用的应用单独归入「不常用」
            val category = if (item.packageName != null && isRarelyUsed(item.packageName, usageStats)) {
                "不常用"
            } else {
                baseCategory
            }

            if (!result.containsKey(category)) {
                result[category] = mutableListOf()
            }
            result[category]?.add(item)
        }

        return result
    }

    /**
     * 判断应用是否「不常用」：
     * 7 天内前台时长 < 1 分钟（且能查到统计）视为不常用。
     * 查不到统计（系统应用/未启动过）不归为不常用，避免误伤。
     */
    private fun isRarelyUsed(packageName: String, usageStats: Map<String, Long>): Boolean {
        val foregroundMs = usageStats[packageName] ?: return false
        return foregroundMs < RARELY_USED_THRESHOLD_MS
    }

    /**
     * 基于包名精准分类（替代关键词匹配）
     */
    private fun categorizeByPackageName(packageName: String): String {
        val pkg = packageName.lowercase()

        // 社交
        if (pkg.contains("wechat") || pkg.contains("tencent.mm") ||
            pkg.contains("tencent.mobileqq") || pkg.contains("qq") && !pkg.contains("qqmusic") ||
            pkg.contains("sina.weibo") || pkg.contains("twitter") ||
            pkg.contains("facebook") || pkg.contains("instagram") ||
            pkg.contains("telegram") || pkg.contains("whatsapp") ||
            pkg.contains("messenger") || pkg.contains("snapchat") ||
            pkg.contains("dingtalk") || pkg.contains("alibaba.android") && pkg.contains("ding") ||
            pkg.contains("tiktok") || pkg.contains("douyin") ||
            pkg.contains("kuaishou") || pkg.contains("reddit")) {
            return "社交"
        }

        // 工具
        if (pkg.contains("calculator") || pkg.contains("calendar") ||
            pkg.contains("clock") || pkg.contains("alarm") ||
            pkg.contains("weather") || pkg.contains("compass") ||
            pkg.contains("file") && pkg.contains("manager") ||
            pkg.contains("clean") || pkg.contains("master") ||
            pkg.contains("security") || pkg.contains("antivirus") ||
            pkg.contains("vpn") || pkg.contains("wifi") ||
            pkg.contains("flashlight") || pkg.contains("torch") ||
            pkg.contains("scanner") || pkg.contains("translate") ||
            pkg.contains("note") || pkg.contains("notepad") ||
            pkg.contains("recorder") || pkg.contains("voice") ||
            pkg.contains("browser") || pkg.contains("chrome") ||
            pkg.contains("firefox") || pkg.contains("edge") ||
            pkg.contains("samsung") && pkg.contains("internet") ||
            pkg.contains("miui") && (pkg.contains("calculator") || pkg.contains("clock") || pkg.contains("compass")) ||
            pkg.contains("settings") || pkg.contains("setup")) {
            return "工具"
        }

        // 购物
        if (pkg.contains("taobao") || pkg.contains("tmall") ||
            pkg.contains("jingdong") || pkg.contains("pinduoduo") ||
            pkg.contains("alibaba") && pkg.contains("shop") ||
            pkg.contains("amazon") && pkg.contains("shop") ||
            pkg.contains("ebay") || pkg.contains("shopee") ||
            pkg.contains("meituan") || pkg.contains("eleme") ||
            pkg.contains("dianping") || pkg.contains("xianyu") ||
            pkg.contains("sun") && pkg.contains("buy") ||
            pkg.contains("mogujie") || pkg.contains("vipshop")) {
            return "购物"
        }

        // 娱乐（视频/音乐）
        if (pkg.contains("youtube") || pkg.contains("bilibili") ||
            pkg.contains("iqiyi") || pkg.contains("youku") ||
            pkg.contains("tencent.qqlive") || pkg.contains("tv") && pkg.contains("danmaku") ||
            pkg.contains("spotify") || pkg.contains("music") ||
            pkg.contains("netease.cloudmusic") || pkg.contains("qqmusic") ||
            pkg.contains("kugou") || pkg.contains("kuwo") ||
            pkg.contains("podcast") || pkg.contains("fm") ||
            pkg.contains("twitch") || pkg.contains("huya") ||
            pkg.contains("douyu") || pkg.contains("netflix") ||
            pkg.contains("disney")) {
            return "影音"
        }

        // 游戏
        if (pkg.contains("game") || pkg.contains("tencent.tmgp") ||
            pkg.contains("mihoyo") || pkg.contains("genshin") ||
            pkg.contains("honkai") || pkg.contains("pubg") ||
            pkg.contains("com.tencent.ig") || pkg.contains("king") && pkg.contains("glory") ||
            pkg.contains("supercell") || pkg.contains("netease") && pkg.contains("game") ||
            pkg.contains("lilith") || pkg.contains("blizzard")) {
            return "游戏"
        }

        // 摄影
        if (pkg.contains("camera") || pkg.contains("photo") ||
            pkg.contains("gallery") || pkg.contains("album") ||
            pkg.contains("picture") || pkg.contains("snapseed") ||
            pkg.contains("lightroom") || pkg.contains("picsart") ||
            pkg.contains("beauty") || pkg.contains("meitu") && !pkg.contains("meituan") ||
            pkg.contains("capcut") || pkg.contains("video") && pkg.contains("editor")) {
            return "摄影"
        }

        // 金融
        if (pkg.contains("bank") || pkg.contains("alipay") ||
            pkg.contains("pay") && !pkg.contains("payment") ||
            pkg.contains("stock") || pkg.contains("finance") ||
            pkg.contains("wallet") || pkg.contains("digital") ||
            pkg.contains("cmb") || pkg.contains("icbc") ||
            pkg.contains("ccb") || pkg.contains("boc")) {
            return "金融"
        }

        // 阅读
        if (pkg.contains("reader") || pkg.contains("book") ||
            pkg.contains("novel") || pkg.contains("kindle") ||
            pkg.contains("zhihu") || pkg.contains("jianshu") ||
            pkg.contains("news") && !pkg.contains("samsung") ||
            pkg.contains("toutiao") || pkg.contains("jinritoutiao")) {
            return "阅读"
        }

        // 健康
        if (pkg.contains("health") || pkg.contains("fitness") ||
            pkg.contains("sport") || pkg.contains("step") ||
            pkg.contains("workout") || pkg.contains("run") ||
            pkg.contains("heartrate") || pkg.contains("sleep") ||
            pkg.contains("pedometer")) {
            return "健康"
        }

        return "其他"
    }

    /** 获取最近 7 天应用使用频率 */
    private fun getAppUsageStats(): Map<String, Long> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) return emptyMap()

        return try {
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 7 * 24 * 60 * 60 * 1000L
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, beginTime, endTime
            )
            stats.associate { it.packageName to it.totalTimeInForeground }
        } catch (e: Exception) {
            DiagnosticLogger.warn(TAG, "无法获取使用统计: ${e.message}")
            emptyMap()
        }
    }

    // ──────────────────────────────────────────────
    // 整理执行
    // ──────────────────────────────────────────────

    private suspend fun performOrganize(categorized: Map<String, List<DesktopItem>>): Int {
        val categoriesToOrganize = categorized.filter { it.value.size >= 2 }
        val total = categoriesToOrganize.size
        var folderCount = 0
        for ((category, items) in categoriesToOrganize) {
            folderCount++
            // 进度从 70 线性推进到 95，避免超过 100
            val progress = if (total > 0) 70 + (folderCount * 25 / total) else 70
            reportProgress(progress, "正在整理 $category…")
            createFolderAndAddItems(items, category)
            delay(300)
        }
        return folderCount
    }

    private suspend fun createFolderAndAddItems(items: List<DesktopItem>, category: String) {
        if (items.size < 2) return
        // ① 拖第一个图标到第二个图标上，触发文件夹创建
        dragAndDrop(items[0].bounds, items[1].bounds)
        delay(600)

        // 文件夹位置近似为第二个图标的原位置（多数 Launcher 在被覆盖图标处生成文件夹）
        val folderBounds = items[1].bounds

        if (items.size > 2) {
            // ② 关键：创建文件夹后桌面会自动重排，剩余图标的坐标已变化。
            //    必须重新扫描获取最新坐标，否则后续拖拽会拖到错误的位置（旧坐标）。
            val freshItems = scanDesktop().associateBy { it.packageName ?: it.name ?: "" }
            DiagnosticLogger.info(TAG, "重扫获取 ${freshItems.size} 个图标的新坐标，继续拖入「$category」")
            for (i in 2 until items.size) {
                val target = items[i]
                val key = target.packageName ?: target.name ?: ""
                // 优先用最新坐标；找不到则回退到旧坐标（聊胜于无）
                val freshBounds = freshItems[key]?.bounds ?: target.bounds
                dragAndDrop(freshBounds, folderBounds)
                delay(400)
            }
        }
    }

    // ──────────────────────────────────────────────
    // 撤销 / 还原
    // ──────────────────────────────────────────────

    /** 扫描桌面上的文件夹节点 */
    private fun scanDesktopFolders(): List<DesktopItem> {
        val root = rootInActiveWindow ?: return emptyList()
        val folders = mutableListOf<DesktopItem>()
        traverseNodes(root) { node ->
            val item = parseNodeToItem(node)
            if (item != null && item.type == DesktopItem.ItemType.FOLDER) {
                folders.add(item)
            }
            true
        }
        root.recycle()
        return folders
    }

    /**
     * 撤销整理：长按每个文件夹后拖到屏幕顶部「移除」区域。
     * 多数 Launcher（含 MIUI）在长按后会显示「移除 / 解散」热区，
     * 把文件夹拖到那里即可解散文件夹并把图标还原到桌面。
     * @return 成功解散的文件夹数量
     */
    private suspend fun dissolveFolders(): Int {
        val folders = scanDesktopFolders()
        if (folders.isEmpty()) {
            DiagnosticLogger.info(TAG, "撤销：未发现文件夹")
            return 0
        }
        DiagnosticLogger.info(TAG, "撤销：发现 ${folders.size} 个文件夹，开始解散")

        val dm = resources.displayMetrics
        val removeX = (dm.widthPixels / 2).toFloat()
        val removeY = (dm.heightPixels / 8f).toFloat() // 屏幕顶部 1/8 处（移除热区）
        // 用零宽高 Rect 表示目标点，centerX/centerY 即为该点
        val removeRect = Rect(removeX.toInt(), removeY.toInt(), removeX.toInt(), removeY.toInt())

        var dissolved = 0
        for (folder in folders) {
            val bounds = folder.bounds ?: continue
            DiagnosticLogger.info(TAG, "撤销：解散文件夹 '${folder.name}' ${bounds}")
            dragAndDrop(bounds, removeRect)
            delay(600)
            dissolved++
        }
        // 解散后回到桌面刷新
        performGlobalAction(GLOBAL_ACTION_HOME)
        delay(400)
        DiagnosticLogger.info(TAG, "撤销：完成，共解散 $dissolved 个文件夹")
        return dissolved
    }

    private suspend fun dragAndDrop(fromBounds: Rect?, toBounds: Rect?) {
        if (fromBounds == null || toBounds == null) return

        val fromX = fromBounds.centerX().toFloat()
        val fromY = fromBounds.centerY().toFloat()
        val toX = toBounds.centerX().toFloat()
        val toY = toBounds.centerY().toFloat()

        // ⚠️ 关键：必须用「分阶段手势」(willContinue) 来实现长按 + 拖动。
        // 之前把两根 Stroke 放进同一个 GestureDescription —— 系统会把它当作
        // 多指同时触摸（multitouch），而不是单指长按后拖动，因此拖拽完全无效。
        //
        // 正确做法：
        //   ① 第一段手势：在起点长按保持（willContinue=true，表示后续还有续接段）
        //   ② 第二段手势：从起点拖到终点（willContinue=false，结束整条手势链）
        // 两段通过 dispatchGesture 顺序派发，系统会把它识别为一条连续的单指手势。

        // ① 长按阶段
        val holdPath = Path().apply {
            moveTo(fromX, fromY)
            lineTo(fromX, fromY) // 原地保持，触发长按
        }
        val holdStroke = GestureDescription.StrokeDescription(holdPath, 0, LONG_PRESS_MS, true)

        if (!dispatchGestureSync(GestureDescription.Builder().addStroke(holdStroke).build())) {
            DiagnosticLogger.warn(TAG, "长按手势被取消: ($fromX,$fromY)")
            return
        }

        // ② 拖动阶段（续接上一段）
        val dragPath = Path().apply {
            moveTo(fromX, fromY)
            lineTo(toX, toY)
        }
        val dragStroke = GestureDescription.StrokeDescription(dragPath, 0, DRAG_MS)

        val ok = dispatchGestureSync(GestureDescription.Builder().addStroke(dragStroke).build())
        if (!ok) {
            DiagnosticLogger.warn(TAG, "拖动手势被取消: ($fromX,$fromY)→($toX,$toY)")
        }
    }

    /** 同步派发一次手势，返回是否成功完成（true=完成，false=取消） */
    private suspend fun dispatchGestureSync(gesture: GestureDescription): Boolean {
        val result = CompletableDeferred<Boolean>()
        dispatchGesture(gesture, object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                result.complete(true)
            }
            override fun onCancelled(gestureDescription: GestureDescription?) {
                result.complete(false)
            }
        }, null)
        return result.await()
    }
}