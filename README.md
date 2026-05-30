# 桌面整理 - Auto App Organizer

一款帮助你自动整理 Android 桌面的工具应用。

## 功能特性

- 🔄 **一键自动分类**：自动扫描桌面上的应用图标，按照应用名称智能分类到文件夹
- 📱 **小组件保护**：识别并跳过所有小组件（Widget）、快捷方式，保持桌面布局
- 📊 **本地分类词库**：内置常用应用分类词库，支持模糊匹配
- 🛡️ **备份与还原**：整理前自动备份桌面布局，支持一键撤销
- ⚡ **快速简洁**：操作异步执行，UI 简洁明了

## 技术实现

### 核心原理

使用 Android 无障碍服务（AccessibilityService）实现：
- 通过 AccessibilityNodeInfo 获取桌面视图节点树
- 分析节点特征，识别应用图标、小组件、文件夹
- 使用 GestureDescription 模拟拖拽操作创建文件夹
- 将同类应用图标拖拽到同一文件夹

### 项目结构

```
app/
├── src/main/
│   ├── java/com/autoapporganizer/
│   │   ├── ui/                    # 界面层
│   │   │   └── MainActivity.kt    # 主界面
│   │   ├── service/               # 服务层
│   │   │   └── AutoAppOrganizerService.kt  # 无障碍服务
│   │   ├── model/                 # 数据模型
│   │   │   ├── DesktopItem.kt     # 桌面项
│   │   │   └── DesktopBackup.kt   # 备份数据
│   │   └── util/                  # 工具类
│   │       ├── CategoryMatcher.kt # 分类匹配
│   │       └── BackupManager.kt   # 备份管理
│   ├── res/
│   │   ├── layout/                # 布局文件
│   │   ├── values/                # 资源值
│   │   └── xml/                   # 服务配置
│   └── assets/
│       └── categories.json        # 分类词库
```

## 使用说明

1. **安装应用**：在 Android 设备上安装本应用
2. **启用服务**：在系统设置 -> 无障碍 -> 找到"桌面整理"并启用
3. **开始整理**：返回应用，点击圆形按钮开始整理
4. **完成！**：桌面应用图标已自动分类到文件夹

## 分类词库

内置分类包括：
- 社交（微信、QQ、微博等）
- 购物（淘宝、京东、拼多多等）
- 视频（抖音、快手、B站等）
- 音乐（QQ音乐、网易云音乐等）
- 工具（计算器、指南针等）
- 阅读（微信读书、Kindle等）
- 出行（高德地图、滴滴等）
- 系统（设置、相机等）
- 游戏（王者荣耀、原神等）
- 学习（慕课、网易云课堂等）
- 金融（支付宝、银行等）
- 健康（微信运动、Keep等）

## 开发环境要求

- Android Studio Hedgehog | 2023.1.1+
- JDK 8+
- Android SDK API 24+ (Android 7.0)
- Kotlin 1.9.0+

## 编译构建

```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease
```

## 注意事项

⚠️ **重要提示**：
- 本应用需要无障碍服务权限才能工作
- 不同厂商的桌面 Launcher 实现可能有差异
- 整理前会自动备份，但建议重要布局先手动截图
- 本应用不收集任何个人数据，所有操作在本地完成

## 许可证

本项目仅供学习和个人使用。
