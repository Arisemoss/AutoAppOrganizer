# 📱 桌面整理 APK 构建指南

## 🚀 方案一：使用 Android Studio 构建（最简单）

### 前提条件
- 安装 Android Studio Hedgehog (2023.1.1) 或更高版本
- JDK 17+ (通常 Android Studio 自带)

### 构建步骤

#### 1. 准备项目
```bash
# 将整个 /workspace 文件夹复制到你的电脑上
# 例如：复制到 ~/AndroidStudioProjects/AutoAppOrganizer
```

#### 2. 打开项目
- 启动 Android Studio
- 选择 **File > Open**
- 选择你刚才复制的 AutoAppOrganizer 文件夹
- 等待 Gradle 同步完成（首次可能需要几分钟）

#### 3. 构建 Debug APK
- 菜单：**Build > Build Bundle(s) / APK(s) > Build APK(s)**
- 等待构建完成
- 点击通知中的 **locate** 即可找到APK！

APK 位置通常在：
```
app/build/outputs/apk/debug/app-debug.apk
```

#### 4. (可选) 构建 Release APK
- 菜单：**Build > Generate Signed Bundle / APK**
- 选择 **APK**
- 创建或选择密钥库
- 选择 **release** 构建变体

---

## 🔧 方案二：使用命令行构建

### 前置条件
- 安装 Android Studio
- 配置好 Android SDK 环境变量

### 构建命令
```bash
cd /path/to/AutoAppOrganizer

# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease
```

APK 将输出到：
- Debug: `app/build/outputs/apk/debug/`
- Release: `app/build/outputs/apk/release/`

---

## 📋 项目结构检查

在构建前，请确认以下文件存在：

```
/workspace/
├── app/
│   ├── src/main/
│   │   ├── AndroidManifest.xml
│   │   ├── assets/categories.json
│   │   ├── java/com/autoapporganizer/
│   │   │   ├── ui/MainActivity.kt
│   │   │   ├── service/AutoAppOrganizerService.kt
│   │   │   ├── model/
│   │   │   └── util/
│   │   └── res/
│   │       ├── layout/activity_main.xml
│   │       ├── values/
│   │       ├── xml/accessibility_service_config.xml
│   │       └── mipmap-anydpi-v26/
│   ├── build.gradle.kts
│   └── proguard-rules.pro
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── gradlew
└── gradle/wrapper/gradle-wrapper.properties
```

---

## ⚠️ 常见问题

### Q: Gradle 同步失败怎么办？
A:
1. 检查网络连接（首次需要下载依赖）
2. 尝试 **File > Invalidate Caches / Restart**
3. 检查 `local.properties` 中的 SDK 路径是否正确

### Q: 找不到 SDK 路径？
A:
1. 创建 `local.properties` 文件
2. 添加内容（替换为你的 SDK 路径）：
   ```
   sdk.dir=/Users/你的用户名/Library/Android/sdk
   ```
   或者 Windows:
   ```
   sdk.dir=C\:\\Users\\你的用户名\\AppData\\Local\\Android\\Sdk
   ```

### Q: Java 版本不匹配？
A:
- 在 Android Studio 中打开 **File > Settings > Build, Execution, Deployment > Build Tools > Gradle**
- 设置 Gradle JDK 为 17 或更高版本

---

## 🎯 安装到设备

构建完成后，将 APK 安装到 Android 设备：

```bash
# 方法一：使用 adb
adb install app/build/outputs/apk/debug/app-debug.apk

# 方法二：直接复制 APK 到设备安装
```

---

## 📱 应用使用说明

1. 安装应用后打开
2. 按照提示开启无障碍服务
3. 返回桌面整理应用
4. 点击大按钮开始整理！

---

## 📝 注意事项

⚠️ **重要提醒：**
- 该应用使用无障碍服务权限
- 不同厂商的 Launcher 可能需要适配
- 建议先在备用设备或模拟器上测试
- 整理前系统会自动备份，但仍建议手动备份重要数据

---

## 💡 提示

如果你想快速测试，可以使用 Android Studio 的模拟器：

1. 打开 **Device Manager**（右上角图标）
2. 创建或启动一个模拟器
3. 点击 Android Studio 中的 **Run** 按钮（绿色三角形）
4. 应用会自动安装并启动到模拟器！
