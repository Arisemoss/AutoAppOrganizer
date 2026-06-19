# 桌面整理 - 完善开发计划

> 目标：将项目从基础框架打磨成可发布的完整成品

---

## 一、项目现状分析

### ✅ 已完成
| 模块 | 说明 |
|------|------|
| 主界面 UI | Premium Slate-Blue 设计，含 Hero Card、进度条、诊断日志 |
| 无障碍服务框架 | `AutoAppOrganizerService` 基础类，手势支持 |
| 桌面图标扫描 | `AccessibilityNodeInfo` 遍历 + 通用图标识别 |
| 智能分类 | 包名硬匹配 + 关键词 `categories.json` 双路径 |
| 拖拽操作 | `GestureDescription` 长按 + 拖拽实现 |
| 备份还原数据模型 | `DesktopBackup` + `BackupManager` JSON 持久化 |
| 诊断日志系统 | `DiagnosticLogger` StateFlow 实时更新 |
| 权限引导 | 无障碍 + 悬浮窗 + 使用统计三权限检查 |
| CI/CD | GitHub Actions 自动构建 Debug/Release APK |

### ❌ 待完善（按优先级）

#### 🔴 P0 - 阻塞性问题
1. **AndroidManifest.xml** 缺少 `xmlns:tools` 命名空间声明 → 编译报错
2. **accessibility_service_config.xml** 的 `packageNames` 限制 → 服务只监听指定包名，影响通用桌面检测
3. **undoOrganize() 为空实现** → 备份后无法真正还原桌面
4. **`_summary.value` 从未被设置** → `DiagnosticLogger.dumpAll()` 中 summary 始终为空

#### 🟠 P1 - 核心功能缺失
5. **文件夹创建后未命名** → 拖入图标创建文件夹后应该注入分类名称（如"社交""游戏"）
6. **`BackupManager` API 不一致** → `saveBackup()`/`loadBackup()` 是 suspend，但 `hasBackup()` 不是；服务调用时未用协程作用域
7. **自适应图标使用系统图标** → `ic_launcher.xml` foreground 使用 `@android:drawable/ic_menu_sort_by_size`，应使用自定义矢量图
8. **深色主题颜色引用不匹配** → `themes-night.xml` 引用 `@color/background_dark`，但 `colors-night.xml` 定义的是 `@color/background`

#### 🟡 P2 - 用户体验
9. **缺少首次引导教程 (Onboarding)** → 新用户不知道需要开启哪些权限、如何操作
10. **缺少设置界面** → 无法自定义分类策略、文件夹命名规则等
11. **Hero Card 图标非自定义** → 使用系统排序图标，缺乏品牌辨识度
12. **缺少动画效果** → 整理按钮点击/整理完成无过渡动画
13. **`btnUndo` 与 `layoutProgress` 嵌套结构混乱** → undo 按钮放在 progress 区域内，逻辑不清晰

#### 🟢 P3 - 工程完善
14. **缺少单元测试** → 分类匹配、备份序列化无测试覆盖
15. **ProGuard 规则需要完善** → 需保留 Kotlin 协程、Gson 反射相关类
16. **分类词库 `categories.json` 不完善** → 与包名分类有重叠，部分热门应用未收录
17. **缺少多屏幕/多分辨率测试资源** → mipmap 目录不全
18. **Gradle/AGP 版本较老** → AGP 8.1.0 + Kotlin 1.9.0，建议升级

---

## 二、详细变更计划

### 🔴 阶段 1：修复阻塞性问题

#### 1.1 修复 AndroidManifest.xml 命名空间

> 📁 文件：`app/src/main/AndroidManifest.xml`

**问题：** `tools:ignore` 属性需要 `xmlns:tools` 声明

**变更：** 在根 `<manifest>` 标签添加 `xmlns:tools="http://schemas.android.com/tools"`

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
```

#### 1.2 移除 accessibility_service_config.xml 的 packageNames 限制

> 📁 文件：`app/src/main/res/xml/accessibility_service_config.xml`

**问题：** `android:packageNames="..."` 限制了服务只接收特定包名的事件。

**变更：** 删除 `android:packageNames` 属性，让服务监听所有窗口。同时在代码中用 `LAUNCHER_PACKAGES` 做业务层过滤即可。

#### 1.3 实现真正的 undoOrganize 还原逻辑

> 📁 文件：`app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`

**问题：** 当前 `undoOrganize()` 只返回一个文本消息，没有真正执行还原操作。

**变更：**
- 重新扫描桌面，识别所有文件夹节点
- 从文件夹中逐个长按拖出图标到空白区域
- 每个还原动作之间有 300ms 间隔，等待桌面响应
- 完成后清除备份标记，告知用户"已还原 X 个图标"
- 新增 `restoreFromBackup(backup: DesktopBackup)` 私有 suspend 函数

#### 1.4 修复 DiagnosticLogger._summary 初始化

> 📁 文件：`app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`
>
> 📁 文件：`app/src/main/java/com/autoapporganizer/util/DiagnosticLogger.kt`

**变更：**
- 在 `DiagnosticLogger` 中新增 `fun setLauncherInfo(pkg: String, version: String)` 方法，设置 `_summary.value`
- 在 `goToHomeScreen()` 检测到桌面包名后调用此方法

---

### 🟠 阶段 2：完善核心功能

#### 2.1 文件夹命名（创建文件夹后设置名称）

> 📁 文件：`app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`

**问题：** 当前拖入图标创建文件夹后，文件夹名称仍为系统默认（可能是空白或"文件夹1"）

**变更：**
- 在 `createFolderAndAddItems()` 中增加命名步骤
- 创建文件夹完成后 `delay(400)` 等待 UI 渲染
- 检测可编辑的文件夹名称节点（通常是 EditText / TextView contentDescription 变化）
- 通过 accessibility gesture 点击文件夹内部的名称编辑区域，注入分类名称
- 如果命名失败（节点未找到），记录警告但不中断流程

**伪代码：**
```kotlin
private suspend fun nameFolder(categoryName: String, folderBounds: Rect) {
    delay(400)
    // 点击打开文件夹 → 等待渲染 → 点击标题栏编辑 → 输入文字 → 确认
    val centerX = folderBounds.centerX().toFloat()
    val centerY = folderBounds.centerY().toFloat()
    performTap(centerX, centerY)  // 打开文件夹
    delay(500)
    // 尝试定位标题编辑区域（不同 launcher 位置不同）
    // 方案：模拟点击位置上方标题区 + 粘贴文本
}
```

#### 2.2 统一 BackupManager API

> 📁 文件：`app/src/main/java/com/autoapporganizer/util/BackupManager.kt`

**变更：**
- `hasBackup()` 保持非 suspend（只是文件存在检查，轻量）
- `saveBackup()` 和 `loadBackup()` 保持 suspend（IO 操作）
- 新增 `fun getBackupInfo(): BackupInfo?`（非 suspend，只读元数据时间戳）

#### 2.3 重绘自定义自适应启动图标

> 📁 文件：`app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml`
>
> 📁 新增：`app/src/main/res/drawable/ic_launcher_foreground.xml`
>
> 📁 新增：`app/src/main/res/drawable/ic_launcher_background.xml`
>
> 📁 新增：`app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml`

**变更：**
- 创建 `ic_launcher_foreground.xml`：自定义 SVG，图标内容为"九宫格+文件夹"主题
- 创建 `ic_launcher_background.xml`：使用 slate-blue 渐变色块作为背景
- 更新 `ic_launcher.xml` 引用上述两个 drawable
- 新增 `ic_launcher_round.xml`（与 ic_launcher 相同，但用于圆形图标场景）

**矢量图设计：** 3x3 网格图标，中间一格高亮（代表正在整理分类）

#### 2.4 修复深色主题颜色引用

> 📁 文件：`app/src/main/res/values-night/colors.xml`

**问题：** `themes-night.xml` 引用 `@color/background_dark`，但 `colors-night.xml` 只有 `@color/background`

**变更：**
- 在 `colors-night.xml` 中添加 `background_dark`、`surface_dark` 等完整定义
- 确保布局 XML 中所有 `@color/...` 引用在两套主题中都有对应值

---

### 🟡 阶段 3：用户体验增强

#### 3.1 新增 Onboarding 首次引导界面

> 📁 新增：`app/src/main/java/com/autoapporganizer/ui/OnboardingActivity.kt`
>
> 📁 新增：`app/src/main/res/layout/activity_onboarding.xml`
>
> 📁 修改：`app/src/main/AndroidManifest.xml`（将 OnboardingActivity 设为 LAUNCHER，首次启动后跳转到 MainActivity）

**设计：**
- **Step 1**：欢迎页 + 应用介绍（"一键让桌面清爽"）
- **Step 2**：开启无障碍服务（带跳转按钮 + 视频/截图指引）
- **Step 3**：开启悬浮窗权限（Android 15+ 必需）
- **Step 4**：可选开启使用统计权限（智能分类增强）
- **Step 5**：完成，点击"开始整理"进入主界面

**持久化：**
- `SharedPreferences` 存储 `KEY_ONBOARDING_COMPLETED`，完成后不再显示
- `MainActivity` 检测到已完成 onboarding 时不再跳转

#### 3.2 新增设置界面

> 📁 新增：`app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt`
>
> 📁 新增：`app/src/main/res/layout/activity_settings.xml`
>
> 📁 新增：`app/src/main/java/com/autoapporganizer/util/AppPreferences.kt`

**设置项：**
- 文件夹命名策略：中文分类名 / 英文 / 仅图标 / 自定义
- 最小分类数量：少于 N 个图标的分类不创建文件夹（默认 2）
- 自动忽略系统应用：开关
- 保留 Dock 栏应用：开关（不整理底部常驻图标）
- 深色主题：跟随系统 / 强制浅色 / 强制深色
- 关于页面：版本号、作者信息、检查更新入口
- 清除诊断日志

#### 3.3 Hero Card 自定义图标

> 📁 新增：`app/src/main/res/drawable/ic_organize_hero.xml`

**变更：**
- 创建自定义 SVG：桌面整理主题（文件夹+图标组合）
- 更新 `activity_main.xml` 中 Hero Card 的 ImageView 引用

#### 3.4 添加过渡动画

> 📁 修改：`app/src/main/java/com/autoapporganizer/ui/MainActivity.kt`
>
> 📁 新增：`app/src/main/res/animator/` 目录动画文件

**动画：**
- Hero Card 点击时缩放 + 阴影变化 (`animate().scaleX().scaleY().alpha()`)
- 整理完成后 Snackbar 出现动画（使用默认即有）
- 进度条出现/隐藏的淡入淡出

#### 3.5 优化按钮层级结构

> 📁 修改：`app/src/main/res/layout/activity_main.xml`

**问题：** `btnUndo` 放在 `layoutProgress` 内部，但 undo 应该是整理完成后的独立操作。

**变更：**
- 将 `btnUndo` 从 `layoutProgress` 中移出，放到 Hero Card 下方的独立区域
- `layoutProgress` 只包含进度条和当前状态文字

---

### 🟢 阶段 4：工程完善

#### 4.1 新增单元测试

> 📁 新增：`app/src/test/java/com/autoapporganizer/util/CategoryMatcherTest.kt`
>
> 📁 新增：`app/src/test/java/com/autoapporganizer/util/BackupManagerTest.kt`

**测试覆盖：**
- `CategoryMatcher.matchCategory()` - 验证各分类关键词匹配
- `CategoryMatcher.matchCategory()` - 验证未知应用返回"其他"
- `DesktopBackup` JSON 序列化/反序列化（通过 Gson）

#### 4.2 完善 ProGuard 规则

> 📁 修改：`app/proguard-rules.pro`

**添加规则：**
- 保留 Kotlin 协程相关类
- 保留 Gson 反射模型类
- 保留 AppCompat 相关资源引用

```proguard
# Kotlin Coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}

# Gson models
-keep class com.autoapporganizer.model.** { *; }
-keep class com.google.gson.** { *; }

# Accessibility service
-keep class com.autoapporganizer.service.** { *; }

# AndroidX
-keep class androidx.** { *; }
-keep interface androidx.** { *; }
```

#### 4.3 扩充分类词库

> 📁 修改：`app/src/main/assets/categories.json`

**扩充：**
- 增加"影音"分类（把视频+音乐合并或新增）
- 增加"摄影"分类（相机、修图、相册等）
- 增加"金融"分类细化（股票、理财）
- 同步更新 `AutoAppOrganizerService.kt` 中硬编码的包名分类列表

#### 4.4 补充 mipmap 资源（可选）

> 📁 新增：`app/src/main/res/mipmap-hdpi/ic_launcher.png`
>
> 📁 新增：`app/src/main/res/mipmap-hdpi/ic_launcher_round.png`
>
> ... (mdpi, xhdpi, xxhdpi, xxxhdpi)

---

### 🔵 阶段 5：构建系统升级

#### 5.1 升级 Gradle/AGP 版本（可选，谨慎操作）

> 📁 修改：`build.gradle.kts` (root)
>
> 📁 修改：`app/build.gradle.kts`
>
> 📁 修改：`gradle/wrapper/gradle-wrapper.properties`

**版本调整：**
- AGP: `8.1.0` → `8.2.2`
- Kotlin: `1.9.0` → `1.9.22`
- Gradle Wrapper: `8.0` → `8.5`
- compileSdk: `34` (保持)
- targetSdk: `34` (保持)
- minSdk: `24` (保持)

---

## 三、文件变更清单汇总

| # | 文件 | 类型 | 变更摘要 |
|---|------|------|---------|
| 1 | `app/src/main/AndroidManifest.xml` | 修改 | 添加 `xmlns:tools` 命名空间 |
| 2 | `app/src/main/res/xml/accessibility_service_config.xml` | 修改 | 移除 `android:packageNames` 限制 |
| 3 | `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 修改 | 实现 `undoOrganize()` + 文件夹命名 + 设置 summary |
| 4 | `app/src/main/java/com/autoapporganizer/util/DiagnosticLogger.kt` | 修改 | 新增 `setLauncherInfo()` 方法 |
| 5 | `app/src/main/java/com/autoapporganizer/util/BackupManager.kt` | 修改 | 统一 API + 新增 `getBackupInfo()` |
| 6 | `app/src/main/res/values-night/colors.xml` | 修改 | 补全 `background_dark` 等定义 |
| 7 | `app/src/main/res/mipmap-anydpi-v26/ic_launcher.xml` | 修改 | 使用自定义 foreground/background |
| 8 | `app/src/main/res/mipmap-anydpi-v26/ic_launcher_round.xml` | 新增 | 圆形自适应图标 |
| 9 | `app/src/main/res/drawable/ic_launcher_foreground.xml` | 新增 | 自定义前景矢量图 |
| 10 | `app/src/main/res/drawable/ic_launcher_background.xml` | 新增 | 自定义背景色/图形 |
| 11 | `app/src/main/res/drawable/ic_organize_hero.xml` | 新增 | Hero Card 自定义 SVG |
| 12 | `app/src/main/res/layout/activity_main.xml` | 修改 | 调整 btnUndo 层级位置 + 更新图标引用 |
| 13 | `app/src/main/java/com/autoapporganizer/ui/OnboardingActivity.kt` | 新增 | 首次引导界面 |
| 14 | `app/src/main/res/layout/activity_onboarding.xml` | 新增 | 引导页布局 |
| 15 | `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增 | 设置界面 |
| 16 | `app/src/main/res/layout/activity_settings.xml` | 新增 | 设置页布局 |
| 17 | `app/src/main/java/com/autoapporganizer/util/AppPreferences.kt` | 新增 | SharedPreferences 配置管理 |
| 18 | `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 修改 | 添加 Onboarding 跳转 + 设置入口 + 动画 |
| 19 | `app/src/main/res/values/strings.xml` | 修改 | 新增 onboarding、settings 相关文字 |
| 20 | `app/proguard-rules.pro` | 修改 | 添加 Kotlin 协程 + Gson 保留规则 |
| 21 | `app/src/main/assets/categories.json` | 修改 | 扩充分类词库 |
| 22 | `app/src/test/java/com/autoapporganizer/util/CategoryMatcherTest.kt` | 新增 | 分类匹配单元测试 |
| 23 | `app/src/test/java/com/autoapporganizer/util/BackupManagerTest.kt` | 新增 | 备份管理单元测试 |

---

## 四、实施顺序

1. **P0 阻塞修复** → 确保项目可编译运行
2. **P1 核心功能** → undo 还原 + 文件夹命名 + 图标
3. **P2 体验增强** → Onboarding + Settings + 动画
4. **P3 工程完善** → 测试 + ProGuard + 词库
5. **P5 (可选)** → Gradle 升级

---

## 五、验证步骤

### 5.1 编译验证
```bash
cd /workspace
./gradlew clean assembleDebug
./gradlew assembleRelease
./gradlew testDebugUnitTest
```

### 5.2 运行时验证 Checklist
- [ ] 首次安装 → 显示 Onboarding → 引导开启权限 → 进入主界面
- [ ] 点击"一键自动分类" → 正常扫描桌面 → 创建带名称的文件夹 → 显示完成提示
- [ ] 点击"撤销整理" → 图标从文件夹拖出 → 恢复原始位置 → 提示还原完成
- [ ] 深色主题切换正常
- [ ] 诊断日志功能正常（查看/复制/清空）
- [ ] 设置界面可打开并保存配置
- [ ] 重启应用后设置和 onboarding 状态被正确记住
- [ ] 在不同品牌 Launcher (MIUI、原生、One UI) 上均能扫描到图标
