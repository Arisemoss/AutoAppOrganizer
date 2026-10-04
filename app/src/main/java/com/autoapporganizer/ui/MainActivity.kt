package com.autoapporganizer.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.autoapporganizer.service.AutoAppOrganizerService
import com.autoapporganizer.ui.components.WindowSize
import com.autoapporganizer.ui.components.rememberWindowSize
import com.autoapporganizer.ui.screens.AccessibilityGuideScreen
import com.autoapporganizer.ui.screens.BackupScreen
import com.autoapporganizer.ui.screens.HomeScreen
import com.autoapporganizer.ui.screens.OrganizingScreen
import com.autoapporganizer.ui.screens.ResultScreen
import com.autoapporganizer.ui.theme.AutoAppOrganizerTheme
import kotlinx.coroutines.launch

/**
 * 主界面 —— 单 Activity + Compose。
 *
 * 状态全部 [rememberSaveable]（旋转不丢），服务回调用 [DisposableEffect]
 * 生命周期安全注册（离开页面注销，回来重挂并同步进行中状态）。
 * 屏间切换用 AnimatedContent（淡入 + 轻微上滑），取消整理直连服务协程取消。
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AutoAppOrganizerTheme {
                OrganizerApp()
            }
        }
    }
}

@Composable
@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
fun OrganizerApp() {
    val context = LocalContext.current
    val windowSize = rememberWindowSize()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // ── 导航与业务状态（rememberSaveable：配置更改不丢） ──────────
    var screen by rememberSaveable { mutableStateOf(Screen.Home) }
    var organizing by rememberSaveable { mutableStateOf(false) }
    var progress by rememberSaveable { mutableIntStateOf(0) }
    var progressMessage by rememberSaveable { mutableStateOf("") }
    var resultSuccess by rememberSaveable { mutableStateOf(false) }
    var resultFolders by rememberSaveable { mutableIntStateOf(0) }
    var resultMessage by rememberSaveable { mutableStateOf("") }
    var pendingSnackbar by rememberSaveable { mutableStateOf<String?>(null) }

    // ── 服务回调：生命周期安全注册 ────────────────────────────────
    DisposableEffect(Unit) {
        val callback = object : AutoAppOrganizerService.OrganizeCallback {
            override fun onProgress(p: Int, message: String) {
                organizing = true
                progress = p
                progressMessage = message
            }

            override fun onComplete(success: Boolean, folderCount: Int, message: String) {
                organizing = false
                resultSuccess = success
                resultFolders = folderCount
                resultMessage = message
                // 从整理页到达 → 进结果页；否则（如后台完成）只弹提示。
                if (screen == Screen.Organizing) {
                    screen = Screen.Result
                } else {
                    pendingSnackbar = message
                }
            }
        }
        AutoAppOrganizerService.organizeCallback = callback
        // 恢复进行中的整理（进程重建 / 从通知返回）。
        if (AutoAppOrganizerService.isOrganizing) {
            organizing = true
            progress = AutoAppOrganizerService.organizeProgress
            screen = Screen.Organizing
        }
        onDispose {
            if (AutoAppOrganizerService.organizeCallback === callback) {
                AutoAppOrganizerService.organizeCallback = null
            }
        }
    }

    // 非整理页到达的完成事件 → snackbar
    LaunchedEffect(pendingSnackbar) {
        pendingSnackbar?.let { message ->
            snackbarHostState.showSnackbar(message)
            pendingSnackbar = null
        }
    }

    fun startOrganize() {
        if (AutoAppOrganizerService.instance == null) {
            screen = Screen.Accessibility
            return
        }
        AutoAppOrganizerService.instance?.startOrganize()
        screen = Screen.Organizing
    }

    fun startVisionOrganize() {
        if (AutoAppOrganizerService.instance == null) {
            screen = Screen.Accessibility
            return
        }
        AutoAppOrganizerService.instance?.startVisionOrganize()
        screen = Screen.Organizing
    }

    val onBack: () -> Unit = { screen = Screen.Home }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AnimatedContent(
                targetState = screen,
                transitionSpec = {
                    (fadeIn(tween(240)) + slideInVertically(tween(240)) { it / 14 }) togetherWith
                        fadeOut(tween(160))
                },
                label = "screenTransition"
            ) { current ->
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        if (current != Screen.Home) {
                            TopAppBar(
                                title = {
                                    Text(
                                        text = when (current) {
                                            Screen.Organizing -> "正在整理"
                                            Screen.Result -> "整理结果"
                                            Screen.Backup -> "备份与历史"
                                            Screen.Accessibility -> "权限指南"
                                            Screen.Home -> "桌面整理"
                                        }
                                    )
                                },
                                navigationIcon = {
                                    IconButton(onClick = onBack) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                            contentDescription = "返回"
                                        )
                                    }
                                },
                                colors = TopAppBarDefaults.topAppBarColors(
                                    containerColor = MaterialTheme.colorScheme.background,
                                    titleContentColor = MaterialTheme.colorScheme.onBackground
                                )
                            )
                        }
                        when (current) {
                            Screen.Home -> HomeScreen(
                                windowSize = windowSize,
                                onStartOrganize = { startOrganize() },
                                onStartVisionOrganize = { startVisionOrganize() },
                                onOpenBackup = { screen = Screen.Backup },
                                onOpenGuide = { screen = Screen.Accessibility },
                                onOpenSettings = {
                                    context.startActivity(Intent(context, SettingsActivity::class.java))
                                }
                            )
                            Screen.Organizing -> OrganizingScreen(
                                windowSize = windowSize,
                                progress = progress,
                                message = progressMessage,
                                onCancel = {
                                    AutoAppOrganizerService.instance?.cancelOrganize()
                                }
                            )
                            Screen.Result -> ResultScreen(
                                windowSize = windowSize,
                                success = resultSuccess,
                                folderCount = resultFolders,
                                message = resultMessage,
                                onUndo = {
                                    AutoAppOrganizerService.instance?.undoOrganize()
                                    screen = Screen.Organizing
                                },
                                onHome = { screen = Screen.Home }
                            )
                            Screen.Backup -> BackupScreen(
                                windowSize = windowSize,
                                onUndoOrganize = {
                                    AutoAppOrganizerService.instance?.undoOrganize()
                                    screen = Screen.Organizing
                                }
                            )
                            Screen.Accessibility -> AccessibilityGuideScreen(
                                windowSize = windowSize
                            )
                        }
                    }
                }
            }
        }
    }
}
