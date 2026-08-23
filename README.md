# 桌面整理 - Auto App Organizer

一款帮助你自动整理 Android 桌面的工具应用，支持传统无障碍模式和 AI 视觉模式。

## 功能特性

- 🔄 **一键自动分类**：自动扫描桌面上的应用图标，按照应用名称智能分类到文件夹
- 🤖 **AI 视觉分类**：集成 VLM 视觉语言模型（OpenAI/Gemini/GLM），通过截图分析智能分类
- 📱 **小组件保护**：识别并跳过所有小组件（Widget）、快捷方式，保持桌面布局
- 📊 **本地分类词库**：内置 200+ 常用应用分类词库，覆盖 16 个分类
- 🛡️ **备份与还原**：整理前自动备份桌面布局，支持一键撤销
- ⚡ **三种整理模式**：传统无障碍 / 视觉 AI / 混合增强（推荐）
- 🔒 **API Key 加密存储**：使用 EncryptedSharedPreferences 保护 VLM API Key
- 📈 **整理历史**：记录每次整理的详细结果，支持查看历史
- 🎯 **空间优化**：智能选择拖拽路径，减少整理时间
- 🔍 **诊断模式**：详细的节点树诊断，方便排查兼容性问题

## 技术实现

### 核心原理

使用 Android 无障碍服务（AccessibilityService）实现：
- 通过 AccessibilityNodeInfo 获取桌面视图节点树
- 分析节点特征，识别应用图标、小组件、文件夹
- 使用 GestureDescription 模拟拖拽操作创建文件夹
- 将同类应用图标拖拽到同一文件夹

### AI 视觉模式

集成 VLM 视觉语言模型进行智能分类：
- 截取桌面截图，发送给 VLM 分析
- AI 识别应用图标并返回分类建议
- 高置信度结果直接采用，低置信度回退到关键词匹配
- 支持 OpenAI GPT-4V、Google Gemini、智谱 GLM-4V

### 项目结构

```
app/
├── src/main/
│   ├── java/com/autoapporganizer/
│   │   ├── ui/                        # 界面层 (Jetpack Compose)
│   │   │   ├── MainActivity.kt        # 主界面
│   │   │   ├── screens/               # 各页面
│   │   │   │   ├── HomeScreen.kt      # 首页
│   │   │   │   ├── OrganizingScreen.kt# 整理进度
│   │   │   │   ├── ResultScreen.kt    # 整理结果
│   │   │   │   └── BackupScreen.kt    # 备份管理
│   │   │   └── components/            # 通用组件
│   │   ├── service/                   # 服务层
│   │   │   └── AutoAppOrganizerService.kt
│   │   ├── core/                      # 核心引擎
│   │   │   ├── action/                # 手势执行
│   │   │   ├── agent/                 # ReAct Agent 框架
│   │   │   ├── classification/        # AI 分类 + 融合
│   │   │   ├── feedback/              # 反馈收集 + 缓存
│   │   │   ├── layout/                # 空间优化
│   │   │   ├── model/                 # VLM 服务
│   │   │   ├── perception/            # 感知融合
│   │   │   ├── plan/                  # 视觉规划
│   │   │   ├── prompt/                # Prompt 模板
│   │   │   └── strategy/              # 策略模式
│   │   ├── model/                     # 数据模型
│   │   └── util/                      # 工具类
│   ├── res/
│   │   ├── layout/                    # 布局文件
│   │   ├── values/                    # 资源值
│   │   └── xml/                       # 服务配置
│   └── assets/
│       ├── categories.json            # 分类词库 (200+ 应用)
│       └── prompts/                   # AI Prompt 模板
├── src/test/                          # 单元测试
│   └── java/com/autoapporganizer/
│       ├── core/
│       │   ├── action/                # 手势测试
│       │   ├── agent/                 # Agent 测试
│       │   ├── classification/        # 分类测试
│       │   ├── feedback/              # 反馈测试
│       │   ├── layout/                # 布局优化测试
│       │   ├── perception/            # 感知测试
│       │   └── plan/                  # 规划测试
│       └── util/                      # 工具测试
└── docs/                              # 文档
```

## 使用说明

### 基本使用

1. **安装应用**：在 Android 设备上安装本应用
2. **启用服务**：在系统设置 → 无障碍 → 找到"桌面整理"并启用
3. **开始整理**：返回应用，点击圆形按钮开始整理
4. **完成！**：桌面应用图标已自动分类到文件夹

### AI 视觉模式

1. 进入设置 → 整理策略 → 选择"混合增强"或"视觉 AI"
2. 点击"模型配置"，输入 VLM API Key（支持 OpenAI/Gemini/GLM）
3. 返回首页，点击"视觉整理"按钮

### 诊断模式

如果整理不工作，可以使用诊断模式排查：
1. 点击首页的"诊断"按钮
2. 查看详细的节点树信息
3. 根据诊断信息调整设置

## 分类词库

内置 16 个分类，覆盖 200+ 常用应用：

| 分类 | 示例应用 |
|------|----------|
| 社交 | 微信、QQ、微博、钉钉、Telegram |
| 购物 | 淘宝、京东、拼多多、闲鱼、亚马逊 |
| 视频 | 抖音、快手、B站、爱奇艺、Netflix |
| 音乐 | QQ音乐、网易云音乐、Spotify、喜马拉雅 |
| 游戏 | 王者荣耀、原神、和平精英、Steam |
| 出行 | 高德地图、滴滴、携程、12306 |
| 阅读 | 微信读书、Kindle、知乎、小红书 |
| 金融 | 支付宝、银行、证券、数字货币 |
| 工具 | 计算器、天气、文件管理、密码管理 |
| 系统 | 设置、相机、电话、应用商店 |
| 学习 | 慕课、得到、Duolingo、LeetCode |
| 健康 | Keep、运动、医疗健康 |
| 摄影 | 相机、剪辑、修图 |
| 生活 | 外卖、快递、招聘、房产 |
| 亲子 | 宝宝巴士、儿童教育 |

## 开发环境要求

- Android Studio Hedgehog | 2023.1.1+
- JDK 17+
- Android SDK API 24+ (Android 7.0)
- Kotlin 1.9.0+

## 编译构建

```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease

# 运行单元测试
./gradlew testDebugUnitTest
```

## 架构设计

### 策略模式

应用支持三种整理策略，通过 `OrganizerFacade` 统一管理：

- **LegacyStrategy**：纯无障碍模式，不依赖网络
- **VisionStrategy**：纯 AI 视觉模式，需要 VLM API
- **HybridStrategy**：混合模式，AI 优先 + 无障碍兜底（推荐）

### ReAct Agent 框架

视觉模式使用 ReAct（Reason-Act）循环：

1. **Perceive**：扫描桌面（无障碍 + 可选 VLM）
2. **Reason**：决定下一步操作
3. **Act**：执行手势操作
4. **Observe**：观察结果，更新状态

### 感知融合

融合多源感知数据：
- AccessibilityChannel：无障碍节点树
- VisionChannel：VLM 视觉分析
- PerceptionFusion：合并去重，提升准确性

## 注意事项

⚠️ **重要提示**：
- 本应用需要无障碍服务权限才能工作
- 不同厂商的桌面 Launcher 实现可能有差异
- 整理前会自动备份，但建议重要布局先手动截图
- 本应用不收集任何个人数据，所有操作在本地完成
- AI 视觉模式需要网络连接和 VLM API Key
- API Key 使用加密存储，不会泄露

## 许可证

本项目仅供学习和个人使用。
