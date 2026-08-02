package com.autoapporganizer.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.ui.components.LiquidSnackbarHost
import com.autoapporganizer.ui.components.SnackType
import com.autoapporganizer.ui.screens.AccessibilityGuideScreen
import com.autoapporganizer.ui.screens.BackupScreen
import com.autoapporganizer.ui.screens.HomeScreen
import com.autoapporganizer.ui.screens.OrganizingScreen
import com.autoapporganizer.ui.screens.ResultScreen
import com.autoapporganizer.ui.theme.AppCategory
import com.autoapporganizer.ui.theme.AutoAppOrganizerTheme
import com.autoapporganizer.util.BackupManager
import com.autoapporganizer.util.HistoryManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 主控制台 —— Compose 宿主。
 *
 * 持有导航状态与整理流程状态，通过 [AutoAppOrganizerService.organizeCallback]
 * 接收进度/完成回调（统一切回主线程），驱动 Organizing / Result 页面流转。
 * 保留对原有 [SettingsActivity] 的跳转，以及视觉整理 / 诊断入口。
 */
class MainActivity : ComponentActivity() {

    private lateinit var historyManager: HistoryManager
    private lateinit var backupManager: BackupManager
    private val mainScope = MainScope()

    // ── 导航与整理状态 ──
    private var screen by mutableStateOf(Screen.Home)
    private var opMode by mutableStateOf(OpMode.IDLE)
    private var organizeProgress by mutableFloatStateOf(0f)
    private var organizeMessage by mutableStateOf("")
    private var organizeResult by mutableStateOf<OrganizeResult?>(null)
    private var previewCategories by mutableStateOf<List<Pair<AppCategory, Int>>>(emptyList())

    // ── Home 概览状态 ──
    private var ready by mutableStateOf(false)
    private var pendingAppCount by mutableIntStateOf(0)
    private var lastOrganizeLabel by mutableStateOf("尚未")
    private var backupLabel by mutableStateOf("未开启")

    // ── 备份页状态 ──
    private var backups by mutableStateOf<List<BackupEntry>>(emptyList())
    private var autoBackup by mutableStateOf(true)

    // ── Snackbar 状态 ──
    private var snackMessage by mutableStateOf("")
    private var snackType by mutableStateOf(SnackType.INFO)

    private enum class OpMode { IDLE, ORGANIZE, VISION, UNDO }

    private val organizeCallback = object : AutoAppOrganizerService.OrganizeCallback {
        override fun onProgress(progress: Int, message: String) {
            // 回调可能从后台线程触发，统一切回主线程更新 Compose 状态
            mainScope.launch {
                withContext(Dispatchers.Main.immediate) {
                    organizeProgress = (progress / 100f).coerceIn(0f, 1f)
                    organizeMessage = message
                }
            }
        }

        override fun onComplete(success: Boolean, folderCount: Int, message: String) {
            mainScope.launch {
                // 磁盘 I/O 切到 IO 线程
                val latest = withContext(Dispatchers.IO) { historyManager.latest() }
                val result = OrganizeResult(
                    success = success,
                    folderCount = folderCount,
                    appCount = latest?.appCount ?: 0,
                    categories = latest?.categories ?: emptyMap(),
                    message = message
                )
                organizeResult = result
                val wasOrganize = opMode == OpMode.ORGANIZE || opMode == OpMode.VISION
                opMode = OpMode.IDLE
                refreshAll()
                if (success && wasOrganize && folderCount >= 0 && latest != null) {
                    screen = Screen.Result
                } else {
                    screen = Screen.Home
                    showSnack(message, if (success) SnackType.SUCCESS else SnackType.ERROR)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        historyManager = HistoryManager(this)
        backupManager = BackupManager(this)

        AutoAppOrganizerService.organizeCallback = organizeCallback
        refreshAll()

        setContent {
            AutoAppOrganizerTheme {
                AppRoot()
                // 全局 Snackbar
                LiquidSnackbarHost(
                    message = snackMessage,
                    type = snackType,
                    onDismiss = { snackMessage = "" }
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        AutoAppOrganizerService.organizeCallback = organizeCallback
        refreshAll()
    }

    override fun onDestroy() {
        super.onDestroy()
        AutoAppOrganizerService.organizeCallback = null
    }

    @Composable
    private fun AppRoot() {
        // 非主页时返回键回到主页；Organizing 页也允许返回（取消整理）
        BackHandler(enabled = screen != Screen.Home) {
            screen = Screen.Home
        }

        when (screen) {
            Screen.Home -> HomeScreen(
                pendingAppCount = pendingAppCount,
                lastOrganizeLabel = lastOrganizeLabel,
                backupLabel = backupLabel,
                ready = ready,
                onOrganize = ::startOrganize,
                onOpenBackup = { screen = Screen.Backup; refreshBackups() },
                onOpenSettings = { startActivity(Intent(this, SettingsActivity::class.java)) },
                onVisionOrganize = ::startVisionOrganize,
                onDiagnose = ::runDiagnostic
            )

            Screen.Organizing -> OrganizingScreen(
                progress = organizeProgress,
                statusMessage = organizeMessage.ifBlank { "正在分析桌面…" },
                categories = previewCategories
            )

            Screen.Result -> ResultScreen(
                result = organizeResult ?: OrganizeResult(false, 0, 0, emptyMap(), ""),
                onComplete = { screen = Screen.Home },
                onUndo = ::undoOrganize
            )

            Screen.Backup -> BackupScreen(
                backups = backups,
                autoBackup = autoBackup,
                onAutoBackupChange = { autoBackup = it },
                onRestore = { undoOrganize() },
                onDelete = { entry ->
                    mainScope.launch {
                        withContext(Dispatchers.IO) { historyManager.delete(entry.timestamp) }
                        refreshBackups()
                        showSnack("已删除该记录", SnackType.INFO)
                    }
                },
                onBack = { screen = Screen.Home }
            )

            Screen.Accessibility -> AccessibilityGuideScreen(
                onBack = { screen = Screen.Home },
                onOpenSettings = { openAccessibilitySettings() },
                onComplete = { screen = Screen.Home }
            )
        }
    }

    // ──────────────────────────────────────────────
    // 状态刷新
    // ──────────────────────────────────────────────

    private fun refreshAll() {
        mainScope.launch {
            val (serviceOn, overlay, latest, hasBackup, allBackups) = withContext(Dispatchers.IO) {
                val svc = AutoAppOrganizerService.instance != null
                val ov = hasOverlayPermission()
                val lat = historyManager.latest()
                val hb = backupManager.hasBackup()
                val all = historyManager.loadAll()
                FiveTuple(svc, ov, lat, hb, all)
            }
            ready = serviceOn && overlay
            pendingAppCount = latest?.appCount ?: 0
            lastOrganizeLabel = latest?.let { relativeLabel(it.timestamp) } ?: "尚未"
            backupLabel = if (hasBackup) "已就绪" else "未开启"
            val now = System.currentTimeMillis()
            backups = allBackups.map { s ->
                BackupEntry(
                    timestamp = s.timestamp,
                    folderCount = s.folderCount,
                    appCount = s.appCount,
                    fresh = (now - s.timestamp) < 7L * 24 * 60 * 60 * 1000
                )
            }
        }
    }

    private fun refreshBackups() {
        mainScope.launch {
            val now = System.currentTimeMillis()
            val all = withContext(Dispatchers.IO) { historyManager.loadAll() }
            backups = all.map { s ->
                BackupEntry(
                    timestamp = s.timestamp,
                    folderCount = s.folderCount,
                    appCount = s.appCount,
                    fresh = (now - s.timestamp) < 7L * 24 * 60 * 60 * 1000
                )
            }
        }
    }

    private fun relativeLabel(ts: Long): String {
        val days = ((System.currentTimeMillis() - ts) / (24 * 60 * 60 * 1000)).toInt()
        return when {
            days <= 0 -> "今天"
            days == 1 -> "昨天"
            days < 30 -> "${days}天前"
            else -> "${days / 30}月前"
        }
    }

    // ──────────────────────────────────────────────
    // Snackbar
    // ──────────────────────────────────────────────

    private fun showSnack(message: String, type: SnackType = SnackType.INFO) {
        snackMessage = message
        snackType = type
    }

    // ──────────────────────────────────────────────
    // 权限
    // ──────────────────────────────────────────────

    private fun hasOverlayPermission(): Boolean =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) Settings.canDrawOverlays(this) else true

    private fun openAccessibilitySettings() {
        startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
    }

    // ──────────────────────────────────────────────
    // 操作
    // ──────────────────────────────────────────────

    private fun startOrganize() {
        val service = AutoAppOrganizerService.instance
        when {
            service == null -> {
                screen = Screen.Accessibility
            }
            !hasOverlayPermission() -> {
                showSnack("请先开启悬浮窗权限", SnackType.ERROR)
                startActivity(
                    Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                )
            }
            else -> {
                opMode = OpMode.ORGANIZE
                organizeProgress = 0f
                organizeMessage = "正在分析桌面…"
                organizeResult = null
                loadPreviewCategories()
                screen = Screen.Organizing
                service.startOrganize()
            }
        }
    }

    private fun startVisionOrganize() {
        val service = AutoAppOrganizerService.instance
        if (service == null) {
            screen = Screen.Accessibility
            return
        }
        opMode = OpMode.VISION
        organizeProgress = 0f
        organizeMessage = "视觉整理中…"
        organizeResult = null
        loadPreviewCategories()
        screen = Screen.Organizing
        service.startVisionOrganize()
    }

    private fun runDiagnostic() {
        val service = AutoAppOrganizerService.instance
        if (service == null) {
            showSnack("请先开启无障碍服务", SnackType.ERROR)
            return
        }
        service.runDiagnostic()
        showSnack("诊断中，请稍候", SnackType.INFO)
    }

    private fun undoOrganize() {
        val service = AutoAppOrganizerService.instance
        if (service == null) {
            showSnack("服务未连接", SnackType.ERROR)
            return
        }
        opMode = OpMode.UNDO
        organizeProgress = 0f
        organizeMessage = "正在还原桌面…"
        screen = Screen.Organizing
        service.undoOrganize()
    }

    private fun loadPreviewCategories() {
        mainScope.launch {
            val latest = withContext(Dispatchers.IO) { historyManager.latest() }
            previewCategories = latest?.sortedCategories
                ?.map { AppCategory.fromLabel(it.key) to it.value }
                ?: emptyList()
        }
    }

    /** 五元组辅助类，用于一次性从 IO 线程返回多个值。 */
    private data class FiveTuple(
        val serviceOn: Boolean,
        val overlay: Boolean,
        val latest: com.autoapporganizer.model.OrganizeSession?,
        val hasBackup: Boolean,
        val allBackups: List<com.autoapporganizer.model.OrganizeSession>
    )
}
