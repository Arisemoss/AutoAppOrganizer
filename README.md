# 桌面整理 · AutoAppOrganizer

[![CI](https://github.com/Arisemoss/AutoAppOrganizer/actions/workflows/ci.yml/badge.svg)](https://github.com/Arisemoss/AutoAppOrganizer/actions/workflows/ci.yml)
[![Release](https://img.shields.io/github/v/release/Arisemoss/AutoAppOrganizer)](https://github.com/Arisemoss/AutoAppOrganizer/releases)
[![License: MIT](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

一款自动整理 Android 桌面的工具：扫描桌面图标、智能分类、自动建夹归组。支持**传统无障碍**与**视觉 AI（VLM）**双引擎，整理前自动备份，一键还原。

> 当前版本 **v1.13**（versionCode 5） · minSdk 24 / targetSdk 35 · Kotlin + Jetpack Compose

---

## 功能特性

- **三种整理模式**：传统无障碍（关键词词库）/ 视觉 AI（截图交给 VLM 语义分类）/ 混合增强（默认，视觉失败自动回退传统）
- **智能分类**：内置 540+ 关键词词库（15 分类，含中文名与包名片段）；VLM 支持 OpenAI / Gemini / 智谱 GLM / Ollama 本地模型；7 天分类缓存加速二次整理
- **备份与还原**：三种模式整理前都会自动备份桌面布局；一键解散文件夹还原（按厂商适配「移除热区」位置）
- **空间优化**：质心锚点对建夹，每步拖拽前按 label+IoU 在最新扫描中重定位图标与文件夹（对抗 Launcher 网格重排）
- **随时取消**：整理进度页一键取消，协程级安全中断，已建文件夹保留
- **整理历史**：最近 50 条会话记录（分类统计/文件夹数），可单条删除
- **诊断模式**：无障碍节点树深度 dump，排查各厂商 Launcher 兼容性
- **API Key 安全**：EncryptedSharedPreferences 加密存储，旧版明文自动迁移

## 工作原理

```
无障碍节点树 + 截图
  → 感知融合（无障碍元素 ⊕ VLM 检测，空间匹配去重）
  → 分类（缓存 → AI 语义 → 关键词词库 → 小类并入"其他"）
  → ReAct 循环（感知 → 规划 → 手势 → 观测，状态机 scan/drag/next/done）
  → GestureExecutor 派发长按+拖拽手势 → Launcher 建夹归组
```

- **传统模式**：纯节点树识别（尺寸窗 + clickable + label 启发式），包名经 PackageManager 反查后走词库
- **视觉模式**：截图（API 30+，HardwareBuffer → Bitmap）压缩后交 VLM，返回图标坐标与分类建议，与节点树融合
- **混合模式**：视觉成功即采用（含部分成功）；失败且未建夹才回退传统

## UI（Aurora Glass 设计系统）

深空底色 + 极光渐变（紫→青）+ 玻璃卡片（半透明面板 + 细描边）。

- **间距**：4dp 基准刻度（`theme/Spacing.kt`：4/8/12/16/20/24/32）
- **自适应**：三档断点（<600dp 手机单列 / 600–840dp 居中限宽 / >840dp 双栏，上限 1100dp）
- **五屏**：首页（Hero+统计+入口）/ 整理进度（进度环+轨道动画+**取消**）/ 结果 / 备份与历史 / 权限指南
- **组件库**：`components/AuroraComponents.kt`（GlassCard / AuroraButton / ProgressRing / StatusBadge / StatChip 等）
- 状态经 `rememberSaveable` 持久化，旋转不丢；服务回调生命周期安全注册

## 项目结构

```
app/src/main/java/com/autoapporganizer/
├── core/
│   ├── action/          # 手势执行（GestureExecutor：长按+拖拽单笔划、边缘内缩）
│   ├── agent/           # ReAct 循环（AgentRunner）与任务状态机（AgentTask/TaskState）
│   ├── classification/  # VLM 语义分类器 + 关键词融合
│   ├── feedback/        # 分类缓存（7 天 TTL）与反馈统计
│   ├── layout/          # 空间优化（质心锚点、拖拽序列）
│   ├── model/           # VLM 服务（OpenAI/Gemini/GLM/本地）+ 图片压缩编码器
│   ├── perception/      # 无障碍通道、视觉通道、感知融合
│   └── strategy/        # 整理策略（Legacy/Vision/Hybrid）
├── service/             # 无障碍服务入口与整理编排
├── task/organize/       # 桌面整理任务状态机
├── ui/                  # Compose 五屏 + 组件 + 主题（Aurora Glass）
├── model/               # 数据模型（DesktopItem/DesktopBackup/OrganizeSession）
└── util/                # 备份/历史/词库/诊断/偏好
app/src/test/            # 14 个测试类、108 个用例
```

## 快速开始

1. 安装 [最新 Release](https://github.com/Arisemoss/AutoAppOrganizer/releases) 的 APK
2. 首页 →「权限指南」，依次开启：
   - **无障碍服务**（必需，读取图标 + 派发手势）
   - **悬浮窗**（Android 15 与小米设备必需）
   - **使用统计**（可选，启用「不常用」分类）
3. 视觉 AI 模式需在设置中配置 VLM（Provider + API Key）
4. 回到桌面 → 开始整理。整理过程请停留在桌面；进度页可随时取消
5. 不满意？「备份与历史」→ 还原桌面布局

## 构建与测试

要求：**JDK 17**、**Android SDK 35**。

```bash
# Debug APK
./gradlew :app:assembleDebug

# 单元测试（当前 108 例：99 通过，9 个既有断言漂移见下）
./gradlew :app:testDebugUnitTest

# Release APK（未签名）
./gradlew :app:assembleRelease
```

> ⚠️ **两个 Windows 构建坑（血泪教训）**
> 1. 项目路径必须**纯 ASCII**——AGP 对中文路径的路径检查可跳过（`android.overridePathCheck=true`），但单元测试 worker 的 classpath 编码仍会炸（全部 ClassNotFound）。
> 2. `local.properties` 的 `sdk.dir` 必须写**正斜杠**（`D:/sdk/android-sdk`）——单反斜杠会被 properties 转义规则吃掉，报"文件名、目录名或卷标语法不正确"。

## CI/CD

| 工作流 | 触发 | 内容 |
|---|---|---|
| `ci.yml`（CI） | push main / PR / tag | 单测 + debug/release APK 构建，报告与 APK 工件上传 |
| `release.yml`（Release） | tag `v*` | 单测 + 构建 + 自动创建 GitHub Release 并附 APK |
| `deploy-pages.yml` | push（docs/**） | 文档页部署 |

发版流程：合并到 main → `git tag v1.x && git push origin v1.x` → Release 自动生成（附 APK）。也支持 `gh release create` 手动发。

## 测试现状与已知限制

**测试**：108 用例中 99 通过。9 个失败均为仓库历史遗留的断言/实现漂移（修复前 HEAD 连编译都无法通过，测试从未真正运行过）：

- `ClassificationFusionTest` ×5：测试假设单元素分类存活，实现遵循 AI prompt 契约（"单应用归其他"）——需把断言改为「其他」
- `SpatialClustererTest` ×2：`optimizeDragSequence` 步数断言（3→2，与 DragOptimizer 契约一致）；`findAnchorPair` 语义（实现返回"质心最近+离锚点最近"，测试期望"质心最近两个"）
- `CategoryMatcherTest` ×2：测试用手写内联词库，缺 `抖音/aweme`、`淘宝/taobao` 等词（生产词库 `assets/categories.json` 已补）

**已知限制**：

- Launcher 兼容性依赖 `accessibility_service_config.xml` 的包名白名单（内置 14 个主流桌面），第三方 Launcher 可能收不到事件
- VLM 返回的坐标被直接信任（无缩放校验），个别模型输出归一化坐标时会整体错位
- 整理过程无前台服务/通知兜底，熄屏或被杀后中断无恢复
- `allowBackup="true"` 会使加密 prefs 进云备份，恢复设备上可能触发明文回退路径
- 「不常用」分类依赖用户手动授予使用统计权限，目前仅指南页引导

## 许可证

[MIT](LICENSE) © 2025 Arisemoss
