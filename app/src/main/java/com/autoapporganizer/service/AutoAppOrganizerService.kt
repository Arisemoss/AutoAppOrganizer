package com.autoapporganizer.service

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.AccessibilityServiceInfo
import android.accessibilityservice.GestureDescription
import android.graphics.Path
import android.graphics.Rect
import android.view.accessibility.AccessibilityEvent
import android.view.accessibility.AccessibilityNodeInfo
import com.autoapporganizer.model.DesktopBackup
import com.autoapporganizer.model.DesktopItem
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.CategoryMatcher
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.*

/**
 * 桌面整理无障碍服务
 */
class AutoAppOrganizerService : AccessibilityService() {
    
    interface OrganizeCallback {
        fun onProgress(progress: Int, message: String)
        fun onComplete(success: Boolean, folderCount: Int, message: String)
    }
    
    companion object {
        private const val TAG = "AutoOrganizerService"
        var instance: AutoAppOrganizerService? = null
            private set
        
        var isOrganizing = false
            private set
        
        var organizeProgress: Int = 0
            private set
        
        var organizeCallback: OrganizeCallback? = null
    }
    
    private val serviceScope = MainScope()
    private lateinit var categoryMatcher: CategoryMatcher
    private lateinit var backupManager: BackupManager
    
    private var currentBackup: DesktopBackup? = null
    
    override fun onCreate() {
        super.onCreate()
        instance = this
        categoryMatcher = CategoryMatcher(this)
        backupManager = BackupManager(this)
    }
    
    override fun onServiceConnected() {
        super.onServiceConnected()
        instance = this
        
        DiagnosticLogger.clear()
        DiagnosticLogger.info(TAG, "服务已连接")
        DiagnosticLogger.info(TAG, "设备: ${android.os.Build.MANUFACTURER} ${android.os.Build.MODEL}")
        DiagnosticLogger.info(TAG, "Android: ${android.os.Build.VERSION.RELEASE} (SDK ${android.os.Build.VERSION.SDK_INT})")
        
        // 配置服务信息，在代码中补充配置
        val info = serviceInfo ?: AccessibilityServiceInfo()
        info.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC
        // Only listen for window events — we trigger organization actively, not passively
        info.eventTypes = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED or
                AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED
        info.flags = AccessibilityServiceInfo.FLAG_INCLUDE_NOT_IMPORTANT_VIEWS or
                AccessibilityServiceInfo.FLAG_REQUEST_TOUCH_EXPLORATION_MODE or
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
        // 处理无障碍事件，目前主要通过主动调用触发整理
    }
    
    override fun onInterrupt() {
        // 服务中断
    }
    
    /**
     * 开始整理桌面
     */
    fun startOrganize() {
        if (isOrganizing) return
        
        serviceScope.launch {
            isOrganizing = true
            organizeProgress = 0
            
            try {
                // 延迟一点时间让用户返回桌面
                delay(500)
                
                // 1. 备份当前桌面
                organizeCallback?.onProgress(10, "正在备份桌面...")
                currentBackup = backupDesktop()
                if (currentBackup != null) {
                    backupManager.saveBackup(currentBackup!!)
                }
                
                // 2. 扫描并解析桌面图标
                organizeCallback?.onProgress(30, "正在分析桌面图标...")
                val desktopItems = scanDesktop()
                if (desktopItems.isEmpty()) {
                    organizeCallback?.onComplete(false, 0, "未找到桌面图标")
                    return@launch
                }
                
                // 3. 分类图标
                organizeCallback?.onProgress(50, "正在分类图标...")
                val categorized = categorizeItems(desktopItems)
                
                // 4. 执行整理
                organizeCallback?.onProgress(70, "正在整理桌面...")
                val folderCount = performOrganize(categorized)
                
                organizeCallback?.onProgress(100, "整理完成")
                organizeCallback?.onComplete(true, folderCount, "整理完成")
                
            } catch (e: Exception) {
                e.printStackTrace()
                organizeCallback?.onComplete(false, 0, "整理失败: ${e.message}")
            } finally {
                isOrganizing = false
            }
        }
    }
    
    /**
     * 撤销整理
     */
    fun undoOrganize() {
        serviceScope.launch {
            val backup = backupManager.loadBackup() ?: currentBackup
            if (backup != null) {
                organizeCallback?.onProgress(50, "正在还原桌面...")
                // 实际的还原操作比较复杂，这里主要提供备份数据
                // 完整的还原需要复杂的拖拽操作
                organizeCallback?.onComplete(true, 0, "已准备好还原数据")
            } else {
                organizeCallback?.onComplete(false, 0, "没有备份数据")
            }
        }
    }
    
    /**
     * 备份桌面
     */
    private fun backupDesktop(): DesktopBackup? {
        val root = rootInActiveWindow ?: return null
        val items = mutableListOf<DesktopItem>()
        
        traverseNodes(root) { node ->
            val item = parseNodeToItem(node)
            if (item != null) {
                items.add(item)
            }
            true
        }
        
        return DesktopBackup(
            timestamp = System.currentTimeMillis(),
            screen = 0,
            items = items
        )
    }
    
    /**
     * 扫描桌面 — 带诊断日志
     */
    private fun scanDesktop(): List<DesktopItem> {
        val root = rootInActiveWindow
        if (root == null) {
            DiagnosticLogger.error(TAG, "rootInActiveWindow 为 null — 可能不在桌面或无障碍未授权")
            return emptyList()
        }
        
        // 记录当前窗口信息
        val rootPkg = root.packageName?.toString() ?: "未知"
        val rootCls = root.className?.toString() ?: "未知"
        DiagnosticLogger.info(TAG, "当前窗口包名: $rootPkg")
        DiagnosticLogger.info(TAG, "当前窗口类名: $rootCls")
        DiagnosticLogger.info(TAG, "根节点子节点数: ${root.childCount}")
        
        // 收集类名统计
        val classCounts = mutableMapOf<String, Int>()
        val items = mutableListOf<DesktopItem>()
        var totalNodes = 0
        var skippedNoName = 0
        
        traverseNodes(root) { node ->
            totalNodes++
            val cls = node.className?.toString() ?: ""
            classCounts[cls] = (classCounts[cls] ?: 0) + 1
            
            val item = parseNodeToItem(node)
            if (item != null && item.type == DesktopItem.ItemType.APP) {
                items.add(item)
                DiagnosticLogger.scan(TAG, "✓ APP: ${item.name} | bounds=(${item.bounds.centerX()},${item.bounds.centerY()}) | pkg=${item.packageName ?: "?"}")
            } else if (item == null && isPotentialIcon(node)) {
                skippedNoName++
                val name = node.contentDescription?.toString() ?: node.text?.toString() ?: "(无)"
                val clsShort = cls.substringAfterLast('.')
                DiagnosticLogger.scan(TAG, "✗ 跳过: name='$name' | class=$clsShort | clickable=${node.isClickable}")
            }
            true
        }
        
        // 输出类名分布统计（最有价值的诊断信息）
        DiagnosticLogger.info(TAG, "=== 扫描结果 ===")
        DiagnosticLogger.info(TAG, "总节点: $totalNodes | 识别APP: ${items.size} | 跳过无名称: $skippedNoName")
        DiagnosticLogger.info(TAG, "节点类名分布 (Top 15):")
        classCounts.entries
            .sortedByDescending { it.value }
            .take(15)
            .forEach { (cls, count) ->
                DiagnosticLogger.debug(TAG, "  ${cls.substringAfterLast('.')} × $count")
            }
        
        if (items.isEmpty()) {
            DiagnosticLogger.warn(TAG, "未找到任何APP图标！可能原因:")
            DiagnosticLogger.warn(TAG, "  1. Launcher 使用非标准视图类名 (见上方类名分布)")
            DiagnosticLogger.warn(TAG, "  2. 无障碍服务未完全授权")
            DiagnosticLogger.warn(TAG, "  3. 当前窗口不是桌面 (包名: $rootPkg)")
        }
        
        return items
    }
    
    /** 预检：节点是否有可能是图标（有文本描述/内容描述且可点击） */
    private fun isPotentialIcon(node: AccessibilityNodeInfo): Boolean {
        val name = node.contentDescription?.toString() ?: node.text?.toString()
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        // 过滤太小或太大的节点
        val w = bounds.width()
        val h = bounds.height()
        return !name.isNullOrEmpty() && w in 40..600 && h in 40..600
    }
    
    /**
     * 解析节点为桌面项
     */
    private fun parseNodeToItem(node: AccessibilityNodeInfo): DesktopItem? {
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        
        // 判断是否是小组件
        if (isWidget(node)) {
            return DesktopItem(
                type = DesktopItem.ItemType.WIDGET,
                bounds = bounds
            )
        }
        
        // 判断是否是文件夹
        if (isFolder(node)) {
            return DesktopItem(
                type = DesktopItem.ItemType.FOLDER,
                bounds = bounds,
                name = node.contentDescription?.toString()
            )
        }
        
        // 判断是否是应用图标
        val name = node.contentDescription?.toString() ?: node.text?.toString()
        if (name != null && isAppIcon(node)) {
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
     * 判断是否是小组件
     */
    private fun isWidget(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString() ?: return false
        return className.contains("AppWidget") || 
               className.contains("widget") ||
               className.contains("Widget")
    }
    
    /**
     * 判断是否是文件夹
     */
    private fun isFolder(node: AccessibilityNodeInfo): Boolean {
        val className = node.className?.toString() ?: ""
        val desc = node.contentDescription?.toString() ?: ""
        val text = node.text?.toString() ?: ""
        return desc.contains("文件夹") ||
               text.contains("文件夹") ||
               (className.contains("Folder") && node.childCount >= 2)
    }
    
    /**
     * 判断是否是应用图标 — 宽松匹配，适配更多 Launcher
     *
     * 不同 Launcher 的图标节点类名差异巨大：
     *   - MIUI:   android.widget.RelativeLayout / TextView
     *   - OneUI:  com.android.launcher3.BubbleTextView
     *   - Pixel:  android.widget.TextView
     *   - Nova:   com.teslacoilsw.launcher.FastBitmapDrawable
     *   - 华为:    com.huawei.android.launcher.ItemIcon
     *
     * 策略：名称非空 + (可点击 或 内容描述非空) + 尺寸合理
     */
    private fun isAppIcon(node: AccessibilityNodeInfo): Boolean {
        val name = node.contentDescription?.toString() ?: node.text?.toString()
        if (name.isNullOrEmpty()) return false
        
        val bounds = Rect()
        node.getBoundsInScreen(bounds)
        val w = bounds.width()
        val h = bounds.height()
        
        // 图标尺寸通常在 60~400dp 之间
        if (w !in 40..600 || h !in 40..600) return false
        
        // 排除已知非图标节点
        val className = node.className?.toString() ?: ""
        if (className.contains("RecyclerView") || className.contains("ListView") ||
            className.contains("ScrollView") || className.contains("GridView") ||
            className.contains("ViewGroup") && !node.isClickable) {
            return false
        }
        
        // 有内容描述或有文本 + 可点击 → 很可能是图标
        // 部分 Launcher 的图标不直接 clickable，但父节点是
        if (node.isClickable) return true
        
        // 如果自己不 clickable 但父节点 clickable 且带 Action，也算
        val parent = node.parent
        if (parent != null && parent.isClickable && 
            node.isEnabled && name.length >= 1) {
            return true
        }
        
        return false
    }
    
    /**
     * 遍历节点
     */
    private fun traverseNodes(
        node: AccessibilityNodeInfo,
        callback: (AccessibilityNodeInfo) -> Boolean
    ) {
        if (!callback(node)) return
        
        for (i in 0 until node.childCount) {
            val child = node.getChild(i) ?: continue
            traverseNodes(child, callback)
        }
    }
    
    /**
     * 执行完整诊断 — 不整理，只扫描并输出日志
     */
    fun runDiagnostic() {
        if (isOrganizing) return
        serviceScope.launch {
            try {
                DiagnosticLogger.clear()
                DiagnosticLogger.info(TAG, "=== 开始诊断扫描 ===")
                delay(300)
                val items = scanDesktop()
                DiagnosticLogger.info(TAG, "=== 诊断完成: 找到 ${items.size} 个APP图标 ===")
                items.forEachIndexed { i, item ->
                    DiagnosticLogger.info(TAG, "  [${i+1}] ${item.name} → ${item.packageName ?: "?"}")
                }
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "诊断异常: ${e.message}")
            }
        }
    }
    
    /**
     * 分类图标
     */
    private fun categorizeItems(items: List<DesktopItem>): Map<String, List<DesktopItem>> {
        val result = mutableMapOf<String, MutableList<DesktopItem>>()
        
        for (item in items) {
            val category = categoryMatcher.matchCategory(item.name)
            if (!result.containsKey(category)) {
                result[category] = mutableListOf()
            }
            result[category]?.add(item)
        }
        
        return result
    }
    
    /**
     * 执行整理
     */
    private suspend fun performOrganize(categorized: Map<String, List<DesktopItem>>): Int {
        var folderCount = 0
        
        for ((category, items) in categorized) {
            // 只有2个或更多图标才创建文件夹
            if (items.size >= 2) {
                folderCount++
                organizeCallback?.onProgress(70 + folderCount * 5, "正在整理${category}...")
                
                // 模拟创建文件夹并拖拽图标
                // 实际的实现需要根据不同的Launcher做适配
                createFolderAndAddItems(items, category)
                
                delay(300)
            }
        }
        
        return folderCount
    }
    
    /**
     * 创建文件夹并添加项目
     */
    private suspend fun createFolderAndAddItems(items: List<DesktopItem>, category: String) {
        if (items.size < 2) return
        
        // Take the first two icons to create a folder
        val first = items[0]
        val second = items[1]
        
        // Drag first onto second — the folder will live at second's position
        dragAndDrop(first.bounds, second.bounds)
        delay(500)
        
        // After merging, second.bounds may be stale. Keep using the original
        // target location for subsequent drags so they land in the same folder.
        val folderBounds = second.bounds
        
        for (i in 2 until items.size) {
            val item = items[i]
            dragAndDrop(item.bounds, folderBounds)
            delay(300)
        }
    }
    
    /**
     * 模拟拖拽 - 长按并移动
     */
    private suspend fun dragAndDrop(fromBounds: Rect?, toBounds: Rect?) {
        if (fromBounds == null || toBounds == null) return
        
        val fromX = fromBounds.centerX().toFloat()
        val fromY = fromBounds.centerY().toFloat()
        val toX = toBounds.centerX().toFloat()
        val toY = toBounds.centerY().toFloat()
        
        // Stroke 1: stationary long-press at starting point (500ms)
        val longPressPath = Path()
        longPressPath.moveTo(fromX, fromY)
        longPressPath.lineTo(fromX, fromY)  // stays in place
        
        // Stroke 2: drag from start to target (500ms)
        val dragPath = Path()
        dragPath.moveTo(fromX, fromY)
        dragPath.lineTo(toX, toY)
        
        val gestureBuilder = GestureDescription.Builder()
        val longPressStroke = GestureDescription.StrokeDescription(longPressPath, 0, 500L)
        gestureBuilder.addStroke(longPressStroke)
        val dragStroke = GestureDescription.StrokeDescription(dragPath, 500L, 500L)
        gestureBuilder.addStroke(dragStroke)
        
        val gestureResult = CompletableDeferred<Boolean>()
        dispatchGesture(gestureBuilder.build(), object : GestureResultCallback() {
            override fun onCompleted(gestureDescription: GestureDescription?) {
                gestureResult.complete(true)
            }
            
            override fun onCancelled(gestureDescription: GestureDescription?) {
                gestureResult.complete(false)
            }
        }, null)
        
        gestureResult.await()
    }
    
    /**
     * 检查服务是否启用
     */
    fun isServiceEnabled(): Boolean {
        return instance != null
    }
}