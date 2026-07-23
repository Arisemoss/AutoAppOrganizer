# APK 构建计划文档

## 1. 概述

本文档旨在梳理当前项目构建 APK 过程中遇到的阻塞性问题，并制定分步解决计划，最终成功输出 Debug APK 文件。

| 项目 | 内容 |
|------|------|
| 目标产物 | Debug APK (app-debug.apk) |
| 构建工具 | Gradle + Android Gradle Plugin (AGP) |
| 当前状态 | 构建失败（环境 + 配置问题） |
| 优先级 | P0（阻塞性） |

---

## 2. 环境分析

### 2.1 环境现状表

| 组件 | 当前版本 | 要求版本 | 兼容性 | 备注 |
|------|---------|---------|--------|------|
| JDK | 25 | ≤ 17 | ❌ 不兼容 | AGP 8.1.0 最高支持 JDK 17 |
| Android SDK | 未安装 | 需 compileSdk 级别 | ❌ 缺失 | 需要安装 SDK 33+ |
| Gradle | 8.7 | 与 AGP 8.1.0 匹配 | ✅ 兼容 | 无需变更 |
| AGP | 8.1.0 | 与 Gradle 8.7 匹配 | ✅ 兼容 | 保持当前版本 |
| local.properties | 不存在 | 必须存在 | ❌ 缺失 | 需指向 Android SDK 路径 |

### 2.2 关键问题

1. **JDK 版本不兼容**：AGP 8.1.0 官方要求 JDK ≤ 17，当前环境为 JDK 25，构建时会出现 `Unsupported class file major version` 错误。
2. **Android SDK 缺失**：系统中未安装 Android SDK，Gradle 无法找到 `android.jar` 等必要文件。
3. **缺少 local.properties**：Gradle 通过该文件定位 SDK 路径，缺失会导致 `SDK location not found` 错误。
4. **AndroidManifest.xml 命名空间问题**：旧版 `xmlns:android` 声明方式可能需要更新。

---

## 3. 变更计划

### 3.1 修复 AndroidManifest.xml 命名空间

```xml
<!-- 当前（可能存在兼容性警告） -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.example.app">

<!-- 目标（推荐写法） -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- package 移至 build.gradle 的 namespace 字段 -->
```

在 `build.gradle` 的 `android` 块中添加：

```groovy
android {
    namespace 'com.example.app'
    // ...
}
```

### 3.2 降级 JDK 到 17

```bash
# 检查当前 JDK 版本
java -version

# 安装 JDK 17（使用 SDKMAN 或系统包管理器）
# 方案 A：使用 SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 17.0.9-tem

# 方案 B：使用 apt（Ubuntu/Debian）
sudo apt update
sudo apt install openjdk-17-jdk -y

# 切换默认 JDK
sudo update-alternatives --config java

# 验证
java -version
# 输出应为：openjdk version "17.0.9" 或类似
```

```bash
# 设置 JAVA_HOME 环境变量（持久化）
echo 'export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64' >> ~/.bashrc
echo 'export PATH=$JAVA_HOME/bin:$PATH' >> ~/.bashrc
source ~/.bashrc
```

### 3.3 安装 Android SDK

```bash
# 安装 Android SDK 命令行工具
cd /opt
sudo wget https://dl.google.com/android/repository/commandlinetools-linux-9477386_latest.zip
sudo unzip commandlinetools-linux-9477386_latest.zip -d android-sdk
sudo rm commandlinetools-linux-9477386_latest.zip

# 安装必要的 SDK 组件
export ANDROID_SDK_ROOT=/opt/android-sdk
yes | $ANDROID_SDK_ROOT/cmdline-tools/bin/sdkmanager --sdk_root=$ANDROID_SDK_ROOT "platforms;android-33" "build-tools;33.0.2" "platform-tools"
```

### 3.4 创建 local.properties

```properties
# 文件位置：项目根目录下 local.properties
sdk.dir=/opt/android-sdk
```

### 3.5 构建 Debug APK

```bash
# 清理并构建
./gradlew clean assembleDebug

# 构建产物位置
# app/build/outputs/apk/debug/app-debug.apk
```

---

## 4. 验证步骤

| 步骤 | 操作 | 预期结果 | 验证方法 |
|------|------|---------|---------|
| 1 | `java -version` | 输出 JDK 17 | 确认版本号中包含 "17" |
| 2 | `echo $ANDROID_HOME` | 输出 SDK 路径 | 路径非空且指向 SDK 目录 |
| 3 | `ls $ANDROID_HOME/platforms/` | 列出已安装平台 | 存在 android-33 目录 |
| 4 | `./gradlew --version` | 显示 Gradle 8.7 | 确认 Gradle 版本 |
| 5 | `./gradlew assembleDebug` | BUILD SUCCESSFUL | 无编译错误，产物生成 |
| 6 | `ls -lh app/build/outputs/apk/debug/` | 显示 app-debug.apk | 文件存在且大小正常 |

### 4.1 回滚方案

如果降级 JDK 后出现问题，恢复方案如下：

```bash
# 切换回 JDK 25
sudo update-alternatives --config java
# 选择对应 JDK 25 的选项
```

---

## 5. 风险与注意事项

1. **JDK 降级影响**：降级到 JDK 17 可能影响其他依赖 JDK 25 特性的项目，建议使用 SDKMAN 管理多版本 JDK。
2. **SDK 安装权限**：Android SDK 安装到系统目录可能需要 `sudo` 权限，也可安装到用户目录下。
3. **Gradle 缓存**：环境变更后建议执行 `./gradlew clean` 清除缓存，避免残留问题。
4. **AGP 版本兼容性**：AGP 8.1.0 对应的 Gradle 版本范围为 8.0-8.7，当前使用 8.7 在兼容范围内。

---

## 6. 参考资源

- [Android Gradle Plugin 版本兼容性表](https://developer.android.com/studio/releases/gradle-plugin#compatibility)
- [Android SDK 命令行工具安装指南](https://developer.android.com/studio/command-line/sdkmanager)
- [JDK 版本与 AGP 兼容性要求](https://developer.android.com/build/releases/gradle-plugin#jdk)