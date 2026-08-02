package com.autoapporganizer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.app.usage.UsageStatsManager
import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.autoapporganizer.model.DesktopBackup
import com.autoapporganizer.model.DesktopItem
import com.autoapporganizer.model.OrganizeSession
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import com.autoapporganizer.util.HistoryManager
import com.autoapporganizer.core.action.GestureExecutor
import com.autoapporganizer.core.agent.AgentRunner
import com.autoapporganizer.core.feedback.FeedbackCollector
import com.autoapporganizer.core.model.CloudVlmService
import com.autoapporganizer.core.model.VisionModelService
import com.autoapporganizer.core.model.VlmServiceFactory
import com.autoapporganizer.core.perception.AccessibilityChannelImpl
import com.autoapporganizer.core.perception.VisionChannelImpl
import com.autoapporganizer.task.organize.DesktopOrganizeTask
import com.autoapporganizer.core.OrganizerFacade
import com.autoapporganizer.core.strategy.LegacyOrganizer
import com.autoapporganizer.core.strategy.OrganizeSessionContext
import com.autoapporganizer.core.strategy.StrategyResult
import com.autoapporganizer.core.strategy.VisionOrganizer
import com.autoapporganizer.util.NodeDumper
import com.autoapporganizer.util.PrefsManager
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
class AutoAppOrganizerService : AccessibilityService(), LegacyOrganizer, VisionOrganizer {

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

        // NOTE: GESTURE_TIMEOUT_MS used to live here for the local dispatchGestureSync().
        // It was removed when dragAndDrop was consolidated into GestureExecutor (#7), which
        // carries its own GESTURE_TIMEOUT_MS. If you need to tune the gesture timeout,
        // see GestureExecutor.GESTURE_TIMEOUT_MS.

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
    private lateinit var historyManager: HistoryManager
    private lateinit var prefs: PrefsManager

    /**
     * Shared gesture executor for both the legacy organize path ([dragAndDrop]) and the
     * Agent framework ([AgentRunner] constructs its own, but the legacy path now also uses
     * this single implementation — see #7). Holding it as a field avoids re-creating it
     * per organize call.
     */
    private lateinit var gestureExecutor: GestureExecutor

    private var currentBackup: DesktopBackup? = null
    private lateinit var facade: OrganizerFacade

    override fun onCreate() {
        super.onCreate()
        instance = this
        categoryMatcher = CategoryMatcher(this)
        backupManager = BackupManager(this)
        historyManager = HistoryManager(this)
        prefs = PrefsManager(this)
        usageStatsManager = getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
        gestureExecutor = GestureExecutor(this)
        facade = OrganizerFacade(prefs, this, this)
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

    /** 开始整理桌面（兼容入口，实际通过 [OrganizerFacade] 分发到当前策略） */
    fun startOrganize() {
        if (isOrganizing) return

        serviceScope.launch {
            isOrganizing = true
            organizeProgress = 0
            try {
                val result = facade.organize(
                    OrganizeSessionContext(
                        prefs = prefs,
                        onProgress = { p, m -> reportProgress(p, m) }
                    )
                )
                reportProgress(100, result.message)
                organizeCallback?.onComplete(result.success, result.foldersCreated, result.message)
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

    /**
     * 传统无障碍桌面整理实现 —— [LegacyOrganizer] 接口。
     */
    override suspend fun organizeDesktop(): StrategyResult {
        organizeProgress = 0

        // ① 强制返回桌面（受设置控制）
        if (prefs.autoReturnHome) {
            reportProgress(5, "正在返回桌面…")
            val onDesktop = goToHomeScreen()
            if (!onDesktop) {
                return StrategyResult(false, "无法切换到桌面，请手动返回桌面后重试", 0, 0)
            }
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
            return StrategyResult(false, "未找到桌面图标 — 请查看诊断日志", 0, 0)
        }

        // ④ 智能分类
        reportProgress(50, "正在智能分类…")
        val categorized = categorizeItems(desktopItems)

        // ⑤ 执行整理
        reportProgress(70, "正在整理桌面…")
        val folderCount = performOrganize(categorized)

        // ⑥ 记录历史会话
        val session = OrganizeSession(
            timestamp = System.currentTimeMillis(),
            folderCount = folderCount,
            appCount = desktopItems.size,
            categories = categorized.mapValues { it.value.size },
            launcher = detectedLauncherPkg
        )
        historyManager.append(session)

        return StrategyResult(
            success = folderCount > 0,
            message = "整理完成，共创建 $folderCount 个文件夹",
            foldersCreated = folderCount,
            appsOrganized = desktopItems.size
        )
    }

    /** 撤销整理 */
    fun undoOrganize() {
        // 与整理流程共享 isOrganizing 互斥锁，避免整理中触发撤销或撤销中再次撤销，
        // 否则两个协程会同时派发手势，互相干扰。
        if (isOrganizing) {
            organizeCallback?.onComplete(false, 0, "正在执行操作，请稍候")
            return
        }
        serviceScope.launch {
            isOrganizing = true
            organizeProgress = 0
            try {
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
                val result = dissolveFolders()
                reportProgress(100, "还原完成")
                // 区分三种情况，避免「发现文件夹却解散失败」时显示误导性「未发现」
                val msg = when {
                    result.dissolved > 0 -> "已尝试还原 ${result.dissolved} 个文件夹"
                    result.expected > 0 -> "发现 ${result.expected} 个文件夹但未能解散（该机型移除热区位置可能不同，请手动长按文件夹拖至「移除」）"
                    else -> "未发现可还原的文件夹"
                }
                organizeCallback?.onComplete(true, result.dissolved, msg)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "撤销异常: ${e.message}")
                organizeCallback?.onComplete(false, 0, "撤销失败: ${e.message}")
            } finally {
                isOrganizing = false
            }
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
                root.recycle()

                // scanDesktop 与 dumpNodeTree 各自独立调用 rootInActiveWindow 获取全新快照，
                // 避免一方 recycle 子节点后另一方复用同一缓存对象导致 getChild 失效。
                val items = scanDesktop()
                val dumpRoot = rootInActiveWindow
                dumpNodeTree(dumpRoot)
                dumpRoot?.recycle()

                DiagnosticLogger.info(TAG, "=== 诊断完成: 找到 ${items.size} 个APP ===")
                items.forEachIndexed { i, item ->
                    DiagnosticLogger.info(TAG, "  [${i+1}] ${item.name} → ${item.packageName ?: "?"}")
                }

                if (items.isEmpty()) {
                    DiagnosticLogger.warn(TAG, "未发现图标 — 若不在桌面请手动按 Home 返回桌面后重试")
                }
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "诊断异常: ${e.message}")
            }
        }
    }

    // ──────────────────────────────────────────────
    // P1 视觉 Agent 基座：视觉整理入口
    // ──────────────────────────────────────────────

    /**
     * 视觉整理入口 —— 直接调用 [VisionOrganizer.organizeByVision]。
     */
    fun startVisionOrganize() {
        if (isOrganizing) {
            organizeCallback?.onComplete(false, 0, "正在执行操作，请稍候")
            return
        }
        serviceScope.launch {
            isOrganizing = true
            organizeProgress = 0
            try {
                val result = organizeByVision()
                reportProgress(100, result.message)
                organizeCallback?.onComplete(result.success, result.foldersCreated, result.message)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "视觉整理异常: ${e.message}")
                e.printStackTrace()
                organizeCallback?.onComplete(false, 0, "视觉整理失败: ${e.message}")
            } finally {
                isOrganizing = false
            }
        }
    }

    /**
     * 视觉整理实现 —— [VisionOrganizer] 接口。
     */
    override suspend fun organizeByVision(): StrategyResult {
        organizeProgress = 0
        DiagnosticLogger.info(TAG, "=== 视觉整理启动 ===")

        val vlm = VlmServiceFactory.create(prefs)
        DiagnosticLogger.info(TAG, "VLM: provider=${prefs.vlmProvider} available=${vlm.isAvailable}")

        val perceptionChannel = AccessibilityChannelImpl(this@AutoAppOrganizerService)
        val visionChannel = VisionChannelImpl(perceptionChannel, vlm)
        val runner = AgentRunner(gestureExecutor, perceptionChannel, visionChannel)
        val task = DesktopOrganizeTask(perceptionChannel, visionChannel, this@AutoAppOrganizerService, prefs, vlm)

        // 整理前返回桌面
        if (prefs.autoReturnHome) {
            reportProgress(5, "正在返回桌面…")
            val onDesktop = goToHomeScreen()
            if (!onDesktop) {
                return StrategyResult(false, "无法切换到桌面，请手动返回桌面后重试", 0, 0)
            }
        }

        val result = runner.run(task) { progress, msg ->
            reportProgress(progress, msg)
        }

        val folders = task.getFoldersCreated()
        // 附加反馈摘要到结果消息
        val feedbackSummary = task.getFeedbackCollector().getSummary()
        val enhancedMessage = buildString {
            append(result.message)
            if (feedbackSummary.aiClassifiedCount > 0) {
                append(" | AI 分类 ${feedbackSummary.aiClassifiedCount} 个应用")
                if (feedbackSummary.lowConfidenceCount > 0) {
                    append("，${feedbackSummary.lowConfidenceCount} 个低置信度")
                }
            }
        }
        return StrategyResult(
            success = result.success,
            message = enhancedMessage,
            foldersCreated = folders,
            appsOrganized = result.stepsExecuted
        )
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

    private fun scanDesktop(verbose: Boolean = true): List<DesktopItem> {
        val root = rootInActiveWindow
        if (root == null) {
            DiagnosticLogger.error(TAG, "rootInActiveWindow 为 null")
            return emptyList()
        }

        val rootPkg = root.packageName?.toString() ?: "未知"
        if (verbose) {
            DiagnosticLogger.info(TAG, "当前窗口包名: $rootPkg")
            DiagnosticLogger.info(TAG, "当前窗口类名: ${root.className}")
            DiagnosticLogger.info(TAG, "根节点子节点数: ${root.childCount}")
        }

        val classCounts = if (verbose) mutableMapOf<String, Int>() else null
        val items = mutableListOf<DesktopItem>()
        var totalNodes = 0
        var skippedNoName = 0
        val potentialNodes = if (verbose) mutableListOf<String>() else null

        traverseNodes(root) { node ->
            totalNodes++
            val cls = node.className?.toString() ?: ""
            classCounts?.let { it[cls] = (it[cls] ?: 0) + 1 }

            val item = parseNodeToItem(node)
            if (item != null && item.type == DesktopItem.ItemType.APP) {
                items.add(item)
                if (verbose) {
                    DiagnosticLogger.scan(TAG, "✓ APP: ${item.name} | clickable=${node.isClickable} | class=${cls.substringAfterLast('.')}")
                }
            } else if (potentialNodes != null && isPotentialIcon(node)) {
                skippedNoName++
                val name = node.contentDescription?.toString() ?: node.text?.toString() ?: "(无)"
                potentialNodes.add("  name='$name' class=${cls.substringAfterLast('.')} clickable=${node.isClickable} childCount=${node.childCount}")
            }
            true
        }

        if (verbose) {
            DiagnosticLogger.info(TAG, "总节点: $totalNodes | 识别APP: ${items.size} | 跳过: $skippedNoName")
            DiagnosticLogger.info(TAG, "节点类名分布 (Top 15):")
            classCounts!!.entries
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
                DiagnosticLogger.info(TAG, "被跳过的可疑节点 (${potentialNodes!!.size}):")
                potentialNodes!!.take(20).forEach { DiagnosticLogger.debug(TAG, it) }
            }
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
    //
    // 实现已移到 util/NodeDumper.kt（#6）。这里保留薄转发方法以维持二进制兼容：
    // dumpNodeTree 是 public，外部诊断调用方（如 MainActivity 的诊断按钮）仍在用。
    // 直接删除会破坏 API；改为转发让调用方无感知。

    /**
     * 打印当前窗口完整无障碍节点树（前 3 层 + 所有叶子节点）。
     *
     * 实现已委托给 [com.autoapporganizer.util.NodeDumper.dump]，签名保持不变以兼容
     * 现有调用方。
     */
    fun dumpNodeTree(node: AccessibilityNodeInfo?, maxDepth: Int = 4) {
        NodeDumper.dump(node, maxDepth, tag = TAG)
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
        // TODO(P2): 多屏桌面支持 —— 当前仅备份默认屏 (screen=0)。
        //   多数 Launcher 的多页桌面通过 workspace 子节点而非多 AccessibilityWindow 体现，
        //   若要按页保留独立备份，需在 traverseNodes 中识别 Workspace/CellLayout 分页边界，
        //   并对每页分别生成 DesktopBackup。当前实现在多页桌面上会混合所有页的图标，
        //   撤销还原时无法准确放回原页。
        return DesktopBackup(timestamp = System.currentTimeMillis(), screen = 0, items = items)
    }

    // ──────────────────────────────────────────────
    // ⑤ 智能分类（CategoryMatcher + UsageStatsManager）
    // ──────────────────────────────────────────────
    //
    // 历史上有两套分类逻辑：
    //   1) CategoryMatcher (util/CategoryMatcher.kt) —— 从 assets/categories.json
    //      加载关键词表，按 Map 顺序 contains 匹配，应用名 + 包名统一处理。
    //   2) categorizeByPackageName() —— 250+ 行 inline if-else，专门处理包名。
    // 两者语义重叠且互相不同步（同应用在不同入口可能分到不同类）。
    // 现已删除 categorizeByPackageName，并把它的包名关键词合并进 categories.json。
    // 分类顺序在 JSON 中精心编排：更具体的分类（如「音乐」含 qqmusic）必须排在
    // 更宽泛的（如「社交」含 qq）之前，否则 qqmusic 会被误分到「社交」。
    // CategoryMatcher 用 Gson 解析为 LinkedHashMap，迭代顺序 == JSON 声明顺序。

    private fun categorizeItems(items: List<DesktopItem>): Map<String, List<DesktopItem>> {
        val result = mutableMapOf<String, MutableList<DesktopItem>>()

        // 获取使用频率数据 —— 7 天内前台时长低于阈值的应用归入「不常用」
        val usageStats = getAppUsageStats()
        DiagnosticLogger.info(TAG, "使用统计可用包数: ${usageStats.size}")

        for (item in items) {
            // 应用名和包名走同一张关键词表：CategoryMatcher 做 lowercase + contains，
            // 包名（如 com.tencent.mm）和应用名（如「微信」）都能命中。
            // 包名通常更精准，优先用包名；无包名时退回应用名。
            val key = item.packageName ?: item.name
            val baseCategory = categoryMatcher.matchCategory(key)

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
        // 阈值由设置控制（分钟 → 毫秒）
        val thresholdMs = prefs.rarelyUsedMinutes * 60_000L
        // 阈值为 0 表示禁用「不常用」分类
        if (thresholdMs <= 0) return false
        return foregroundMs < thresholdMs
    }

    /** 获取最近 7 天应用使用频率 */
    private fun getAppUsageStats(): Map<String, Long> {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP_MR1) return emptyMap()

        return try {
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 7 * 24 * 60 * 60 * 1000L
            // queryUsageStats 文档未保证非 null 返回（权限缺失/区间无效时可能返回 null），
            // 直接 .associate 会抛 NPE，这里用安全调用兜底。
            val stats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY, beginTime, endTime
            )
            stats?.associate { it.packageName to it.totalTimeInForeground } ?: emptyMap()
        } catch (e: Exception) {
            DiagnosticLogger.warn(TAG, "无法获取使用统计: ${e.message}")
            emptyMap()
        }
    }

    // ──────────────────────────────────────────────
    // 整理执行
    // ──────────────────────────────────────────────

    private suspend fun performOrganize(categorized: Map<String, List<DesktopItem>>): Int {
        val minSize = prefs.minFolderSize
        val categoriesToOrganize = categorized.filter { it.value.size >= minSize }
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
        val firstBounds = items[0].bounds ?: return
        val secondBounds = items[1].bounds ?: return
        // ① 拖第一个图标到第二个图标上，触发文件夹创建
        dragAndDrop(firstBounds, secondBounds)
        delay(600)

        // ② 关键：文件夹创建后，某些 Launcher 会把新文件夹重新对齐到网格，而非停留在
        //    被覆盖图标的精确位置。旧实现把 folderBounds 固定为 items[1].bounds，
        //    后续拖入会拖到已失效的旧坐标，命中空位。
        //    这里重扫桌面，取离「第二个图标原位置」最近的文件夹节点作为后续拖入目标。
        val folderBounds = scanDesktopFolders()
            .minByOrNull { f ->
                val b = f.bounds ?: return@minByOrNull Int.MAX_VALUE
                val dx = b.centerX() - secondBounds.centerX()
                val dy = b.centerY() - secondBounds.centerY()
                dx * dx + dy * dy
            }?.bounds ?: secondBounds
        DiagnosticLogger.info(
            TAG, "「$category」文件夹目标坐标: $folderBounds（原第二图标: $secondBounds" +
                if (folderBounds != secondBounds) "，已被网格重排）" else "）"
        )

        if (items.size > 2) {
            DiagnosticLogger.info(TAG, "开始将剩余 ${items.size - 2} 个图标拖入「$category」文件夹")
            for (i in 2 until items.size) {
                val target = items[i]
                val key = target.packageName ?: target.name ?: ""
                // 关键：每拖入一个图标，桌面都会自动重排，剩余图标的坐标随之变化。
                // 旧实现只在循环前重扫一次，导致第 3 个之后的图标被拖到失效的旧坐标上，
                // 进而拖空或拖错位置。这里改为每轮重新扫描获取最新坐标。
                //
                // verbose=false：循环内调用频繁（每图标一次），跳过 classCounts 统计 /
                // potentialNodes 收集 / 详细日志，把扫描耗时压到最低（见 #3）。
                // 诊断信息只在循环外的首次 scanDesktop() 调用里产生，足够排查问题。
                val freshItems = scanDesktop(verbose = false).associateBy { it.packageName ?: it.name ?: "" }
                // 优先用最新坐标；找不到（图标已被前一轮误拖入文件夹等）则跳过，避免拖空。
                val freshBounds = freshItems[key]?.bounds ?: continue
                DiagnosticLogger.debug(TAG, "拖入「$category」: ${target.name} → $freshBounds")
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

    /** 撤销解散结果：expected=扫描到的文件夹数，dissolved=成功解散数 */
    private data class DissolveResult(val expected: Int, val dissolved: Int)

    /**
     * 不同 Launcher 长按后的「移除/解散」热区位置不同（均位于屏幕顶部，但 Y 偏移各异）。
     * 旧实现硬编码 heightPixels/8 是 MIUI 的值，在其它机型上会拖到错误位置导致解散失败。
     * 这里按检测到的桌面包名/设备品牌返回对应的移除目标点。
     * @return 零宽高 Rect，centerX/centerY 即移除热区坐标点
     */
    private fun computeRemoveHotZone(): Rect {
        val dm = resources.displayMetrics
        val pkg = detectedLauncherPkg?.lowercase() ?: ""
        val mfr = Build.MANUFACTURER.lowercase()

        // Y 因子：移除热区距屏幕顶部的比例（越小越靠顶）
        val yFactor = when {
            pkg.contains("miui.home") -> 1f / 8f                                      // MIUI/HyperOS
            pkg.contains("nexuslauncher") || pkg.contains("launcher3") ||
                pkg.endsWith("google.android.apps.nexuslauncher") -> 1f / 10f          // Pixel/AOSP Launcher3
            pkg.contains("sec.android") || mfr.contains("samsung") -> 1f / 7f         // 三星 OneUI
            pkg.contains("huawei") || mfr.contains("huawei") ||
                mfr.contains("honor") -> 1f / 8f                                      // 华为/荣耀
            pkg.contains("coloros") || pkg.contains("oppo.launcher") ||
                mfr.contains("oppo") || mfr.contains("realme") -> 1f / 7f             // OPPO/Realme
            pkg.contains("vivo") || pkg.contains("bbk") || pkg.contains("funtouch") ||
                mfr.contains("vivo") -> 1f / 7f                                       // vivo
            pkg.contains("oneplus") || mfr.contains("oneplus") -> 1f / 8f             // OnePlus
            else -> 1f / 8f                                                            // 默认（兼容未知 Launcher）
        }
        val cx = dm.widthPixels / 2
        val cy = (dm.heightPixels * yFactor).toInt()
        DiagnosticLogger.info(TAG, "移除热区(机型=${detectedLauncherPkg ?: "?"}/$mfr): ($cx,$cy) yFactor=$yFactor")
        return Rect(cx, cy, cx, cy)
    }

    /**
     * 撤销整理：长按每个文件夹后拖到屏幕顶部「移除」区域。
     * 多数 Launcher（含 MIUI）在长按后会显示「移除 / 解散」热区，
     * 把文件夹拖到那里即可解散文件夹并把图标还原到桌面。
     * @return DissolveResult(expected=扫描到的文件夹数, dissolved=成功解散数)
     */
    private suspend fun dissolveFolders(): DissolveResult {
        val removeRect = computeRemoveHotZone()

        val expected = scanDesktopFolders().size
        if (expected == 0) {
            DiagnosticLogger.info(TAG, "撤销：未发现文件夹")
            return DissolveResult(0, 0)
        }
        DiagnosticLogger.info(TAG, "撤销：发现 $expected 个文件夹，开始解散")

        var dissolved = 0
        var lastSignature: String? = null
        // 关键：解散一个文件夹后，其内部图标会散落回桌面，导致其它文件夹坐标变化，
        // 旧实现一次性捕获所有文件夹坐标后逐个拖拽，第 2 个起就用了失效坐标。
        // 这里改为每轮重新扫描，取首个文件夹的最新坐标进行解散。
        while (dissolved < expected) {
            val folders = scanDesktopFolders()
            if (folders.isEmpty()) break
            val folder = folders.first()
            val bounds = folder.bounds ?: break
            val sig = "${folder.name}@$bounds"
            // 若连续两轮扫到同一个文件夹（名称+坐标均未变），说明上一次拖拽没能解散它
            // —— 多半是该 Launcher 的「移除热区」位置不同。避免无效死循环，直接跳出。
            if (sig == lastSignature) {
                DiagnosticLogger.warn(TAG, "撤销：文件夹未被解散（拖拽无效，可能移除热区位置不符），停止: $sig")
                break
            }
            lastSignature = sig
            DiagnosticLogger.info(TAG, "撤销：解散文件夹 '${folder.name}' $bounds (第 ${dissolved + 1}/$expected)")
            dragAndDrop(bounds, removeRect)
            delay(600)
            dissolved++
        }
        // 解散后回到桌面刷新
        performGlobalAction(GLOBAL_ACTION_HOME)
        delay(400)
        DiagnosticLogger.info(TAG, "撤销：完成，共解散 $dissolved/$expected 个文件夹")
        return DissolveResult(expected, dissolved)
    }

    private suspend fun dragAndDrop(fromBounds: Rect?, toBounds: Rect?) {
        if (fromBounds == null || toBounds == null) return

        val fromX = fromBounds.centerX().toFloat()
        val fromY = fromBounds.centerY().toFloat()
        val toX = toBounds.centerX().toFloat()
        val toY = toBounds.centerY().toFloat()

        // Delegated to the shared [GestureExecutor], which contains a single, version-aware
        // implementation of the long-press + drag gesture (including the API 24-25 tiny-circle
        // fallback). Previously this method duplicated that logic inline; see #7.
        val ok = gestureExecutor.performDrag(
            fromX, fromY, toX, toY,
            holdMs = LONG_PRESS_MS,
            dragMs = DRAG_MS
        )
        if (!ok) {
            DiagnosticLogger.warn(TAG, "拖拽手势被取消: ($fromX,$fromY)→($toX,$toY)")
        }
    }
}