# APK 构建计划

## 一、摘要

当前项目是一个 Android 桌面整理应用（Kotlin + Gradle），系统环境存在 **Android SDK 未安装** 和 **JDK 版本(25)与 AGP(8.1.0)不兼容** 等阻碍构建的问题。本计划将按顺序解决这些阻塞问题，修复编译错误，最终完成 Debug APK 的本地构建。

---

## 二、当前状态分析

### 2.1 环境现状
| 项目 | 状态 | 说明 |
|------|------|------|
| JDK | ✅ 已安装 | OpenJDK 25.0.2，但 AGP 8.1.0 最高支持 JDK 17~19 |
| Android SDK | ❌ 未安装 | `$ANDROID_HOME` 未设置，无 SDK 目录 |
| Gradle Wrapper | ✅ 就绪 | Gradle 8.0，可正常执行 |
| local.properties | ❌ 不存在 | 缺少 Android SDK 路径配置 |

### 2.2 已知编译阻塞问题
1. **AndroidManifest.xml** - 使用 `tools:ignore` 属性但缺少 `xmlns:tools` 命名空间声明（从之前分支的继续开发计划文档已知）
2. **AGP 8.1.0 + JDK 25 不兼容** - 官方 AGP 8.1.x 最高支持 JDK 19，使用 JDK 25 会导致 Gradle 构建失败
3. **缺少 Android SDK** - 需要安装 cmdline-tools 并下载对应平台的 SDK

### 2.3 相关文件
| 文件 | 角色 |
|------|------|
| `/workspace/build.gradle.kts` | 顶层构建配置（AGP 8.1.0 + Kotlin 1.9.0） |
| `/workspace/app/build.gradle.kts` | App 模块构建配置（compileSdk=34, minSdk=24, targetSdk=34） |
| `/workspace/app/src/main/AndroidManifest.xml` | **需要修复** - `xmlns:tools` 缺失 |
| `/workspace/gradle/wrapper/gradle-wrapper.properties` | Gradle 8.0 分发配置 |

---

## 三、变更计划

### Step 1: 修复 AndroidManifest.xml 构建阻塞问题

**文件：** `/workspace/app/src/main/AndroidManifest.xml`

**原因：** 文件中第 8、11 行使用了 `tools:ignore` 属性，但根 `<manifest>` 标签缺少 `xmlns:tools` 声明，会导致编译报错 `error: attribute 'tools:ignore' not found`。

**变更：** 在 `<manifest>` 标签添加 `xmlns:tools="http://schemas.android.com/tools"`。

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">
```

### Step 2: 降级 JDK 到兼容版本

**原因：** AGP 8.1.0 不支持 JDK 25。需要使用 JDK 17（GitHub Actions 中 `setup-java@v4` 指定的也是 JDK 17）。

**方案：** 检查系统是否已有 JDK 17 可用，如果没有则通过 sdkman 或 mise 安装 JDK 17 并设置 `JAVA_HOME`。或者升级 AGP 版本到 8.5+ 以支持 JDK 21+。

**策略选择：** 优先尝试安装 JDK 17（与 CI/CD 环境一致，风险最低）。

### Step 3: 安装 Android SDK

**原因：** 系统没有 Android SDK，需要安装命令行工具并下载 platform 34 和 build-tools。

**步骤：**
1. 下载 Android SDK command-line tools
2. 安装 `platforms;android-34` 和 `build-tools;34.0.0`
3. 创建 `local.properties` 指向 SDK 路径

### Step 4: 构建 Debug APK

**命令：** `./gradlew assembleDebug`

**期望输出：** `app/build/outputs/apk/debug/app-debug.apk`

**如果构建失败：** 根据错误信息逐步修复（可能涉及资源文件缺失、API 变更等）。

### Step 5: （如用户需要）提交修复

**说明：** 使用 `git-commit` skill 提交构建相关的修复（AndroidManifest.xml 命名空间修复、local.properties 等）。**注意：仅当用户明确要求时才执行提交。**

### Step 6: （如用户需要）构建 Release APK

**说明：** Debug APK 构建成功后，如果用户还需要 Release APK，需要创建调试用的 keystore 并配置签名。

---

## 四、假设与决策

| 假设/决策 | 说明 |
|-----------|------|
| `frontend-design` skill 不适用 | `frontend-design` 用于 Web 前端设计，Android APK 构建属于原生 App 构建范畴，无需使用此 skill |
| `git-commit` skill 按需使用 | 仅在用户明确要求提交时才使用 `git-commit` skill |
| JDK 兼容性优先降级 | AGP 8.1.0 对 JDK 版本有严格限制，降级 JDK 比升级 AGP（可能引发连锁依赖变更）风险更低 |
| Debug 构建优先 | Debug APK 无需签名，可先验证编译是否通过；Release APK 需额外签名配置 |

---

## 五、验证步骤

1. **验证 JDK 版本：** `java -version` 确认为 JDK 17
2. **验证 Android SDK：** `ls $ANDROID_HOME/platforms/android-34` 确认 SDK 已安装
3. **编译 Debug APK：** `./gradlew assembleDebug` 退出码为 0
4. **确认 APK 产出：** `ls -la app/build/outputs/apk/debug/app-debug.apk`
5. **运行单元测试（可选）：** `./gradlew testDebugUnitTest`