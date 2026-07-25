# AutoAppOrganizer 视觉 AI 重构设计文档

## 1. 目标

将 AutoAppOrganizer 从「主要依赖无障碍节点树」的整理方式，升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强方案：

- 视觉模型通过截图理解桌面内容，输出图标位置、类别、下一步动作。
- 无障碍节点树作为低成本、高稳定的辅助感知，提供候选图标、包名、类别先验。
- 原有整理业务逻辑（分类、排序、文件夹管理）保留并作为 VLM 的约束与校验层。
- 支持在「传统模式 / 视觉模式 / 混合模式」之间切换，便于对比测试与渐进迭代。

## 2. 架构总览

```
┌─────────────────────────────────────────────────────────────────┐
│                         UI 层 (MainActivity)                      │
│                模式选择 · 进度展示 · 日志查看 · 配置入口            │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    OrganizerFacade（整理入口）                    │
│         根据 PrefsManager.organizeMode 分发到不同策略              │
│         legacy │ vision │ hybrid                                 │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐
│  LegacyStrategy │  │  VisionStrategy │  │    HybridStrategy   │
│  原有无障碍整理  │  │  纯视觉 AI 驱动  │  │  混合增强（默认）    │
└─────────────────┘  └─────────────────┘  └─────────────────────┘
          │                   │                   │
          └───────────────────┼───────────────────┘
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                      PerceptionFusion（感知融合）                 │
│         合并 VisionChannel + AccessibilityChannel 输出            │
│         生成带置信度的 ScreenElement 列表                         │
└─────────────────────────────────────────────────────────────────┘
                              │
          ┌───────────────────┼───────────────────┐
          ▼                   ▼                   ▼
┌─────────────────┐  ┌─────────────────┐  ┌─────────────────────┐
│ VisionChannel   │  │ Accessibility   │  │   CategoryMatcher   │
│ 截图 → VLM 解析  │  │ 节点树扫描       │  │  本地分类词库匹配    │
└─────────────────┘  └─────────────────┘  └─────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    VisionPlanner（视觉决策器）                    │
│    基于融合感知 + 整理目标，向 VLM 请求结构化动作计划               │
│    输出 ActionPlan（点击 / 长按 / 拖拽 / 等待 / Home / Back）     │
└─────────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────────┐
│                    ActionExecutor（动作执行）                     │
│              将 Action 翻译为无障碍手势并执行                      │
│              内置重试、超时、异常恢复                              │
└─────────────────────────────────────────────────────────────────┘
```

## 3. 组件设计

### 3.1 OrganizerFacade

- 作为整理流程的统一入口，替换 `AutoAppOrganizerService.startOrganize()` 中直接调用旧逻辑的方式。
- 根据 `PrefsManager.organizeMode` 选择策略：
  - `legacy`：走原有 `AutoAppOrganizerService` 的扫描 + 分类 + 拖拽。
  - `vision`：走纯视觉策略（完全由 VLM 决策）。
  - `hybrid`：走混合策略（默认）。
- 负责协调：返回桌面 → 备份 → 执行策略 → 记录历史。

### 3.2 策略接口 `OrganizeStrategy`

```kotlin
interface OrganizeStrategy {
    val name: String
    suspend fun organize(session: OrganizeSessionContext): StrategyResult
}

data class StrategyResult(
    val success: Boolean,
    val message: String,
    val foldersCreated: Int,
    val stepsExecuted: Int
)
```

### 3.3 PerceptionFusion

- 输入：`AccessibilityChannel.scanElements()` + `VisionChannel.detectIcons()`。
- 输出：统一的 `List<ScreenElement>`。
- 融合规则：
  1. 无障碍元素作为基础集合（低成本、包名/标签准确）。
  2. VLM 检测到的元素作为补充，仅当与无障碍集合无重叠（IOU < 0.5 或标签差异大）时才加入。
  3. 当无障碍节点树无法识别图标（如某些 OEM Launcher）时，以 VLM 结果为主。
  4. 每个元素保留 `source` 和 `confidence`，供决策器参考。

### 3.4 VisionPlanner

- 接收当前 `Bitmap`（截图）和 `ScreenElement` 列表。
- 根据当前任务阶段生成结构化 prompt：
  - 阶段 1：发现与分类（识别所有图标、按类别分组）。
  - 阶段 2：创建文件夹（选择锚点图标、决定第一个拖拽动作）。
  - 阶段 3：补充拖拽（将同类图标拖入已创建文件夹）。
- 要求 VLM 返回 JSON：`{ "thought": "", "actions": [{ "type": "...", "params": {} }], "nextPhase": "...", "expectedOutcome": "..." }`。
- `VisionPlanner` 将 JSON 解析为 `ActionPlan`（含一个或多个 `Action`）。
- 内置 `PromptTemplateRepository`，支持按阶段/机型切换提示词模板。

### 3.5 ActionExecutor

- 在现有 `GestureExecutor` 基础上扩展：
  - 增加 `executePlan(actions: List<Action>)` 批量执行。
  - 每个动作执行后截图校验（可选，由模式决定）。
  - 动作失败时重试 1 次，重试失败则标记错误并进入恢复流程（按 Home 等待）。
- 保留 `AccessibilityService` 作为手势执行后端；未来可扩展 ADB/Root 执行器。

## 4. 数据流

以混合模式为例：

1. `OrganizerFacade` 触发「返回桌面」并等待稳定。
2. 调用 `AccessibilityChannel.scanElements()` 获取节点树图标。
3. 调用 `VisionChannel.detectIcons()` 获取 VLM 图标。
4. `PerceptionFusion` 合并两组图标，得到带置信度的元素列表。
5. `CategoryMatcher` 对元素预分类，作为 VLM 的候选类别提示。
6. `VisionPlanner` 根据截图 + 元素列表 + 当前阶段，请求 VLM 输出下一步动作。
7. `ActionExecutor` 执行动作。
8. 动作后等待 UI 稳定，必要时重新截图并进入下一阶段。
9. 循环直到完成或达到最大步数/错误数。

## 5. 混合增强策略细节

### 5.1 何时以 VLM 为主

- 无障碍节点树返回空或图标数量明显偏少时。
- 需要精确定位文件夹创建后的新位置时。
- 图标排列不规则、节点树无法给出准确边界时。
- 遇到未见过的 Launcher 或系统版本时。

### 5.2 何时以无障碍节点树为主

- 节点树能稳定返回图标且包名可用时（分类更准）。
- 需要快速失败/低成本场景（VLM 未配置或网络差）。
- 文件夹解散等精确坐标操作，节点树足以完成时。

### 5.3 决策冲突解决

- 坐标：VLM 检测结果优先，但用节点树边界进行合理性校验（如坐标越界则剔除）。
- 类别：节点树 + `CategoryMatcher` 优先；VLM 仅在其与节点树分歧且置信度高时覆盖。
- 动作：VLM 输出动作计划，但需通过本地规则校验安全性（如不能点击状态栏、不能拖到屏幕外）。

## 6. 视觉 AI 接口

保持并扩展现有 `VisionModelService`：

```kotlin
interface VisionModelService {
    val isAvailable: Boolean
    suspend fun analyze(bitmap: Bitmap, prompt: String): VisionResult
}
```

新增：

```kotlin
interface VisionPlannerService {
    suspend fun plan(bitmap: Bitmap, elements: List<ScreenElement>, phase: OrganizePhase): ActionPlan
}

data class ActionPlan(
    val thought: String,
    val actions: List<Action>,
    val nextPhase: OrganizePhase,
    val expectedOutcome: String
)
```

实现：

- `CloudVlmService`：复用现有 OpenAI / Gemini / GLM 支持。
- `LocalVlmService`（预留接口）：通过 llama.cpp / MNN 等本地推理框架加载 VLM，待后续实现。

## 7. 操作执行层

### 7.1 动作类型

扩展现有 `Action`：

```kotlin
sealed class Action {
    data class Click(val x: Float, val y: Float) : Action()
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()
    data class Drag(...) : Action()
    data class Swipe(val fromX: Float, val fromY: Float, val toX: Float, val toY: Float) : Action()
    data class Type(val text: String) : Action()
    data class Wait(val ms: Long = 500L) : Action()
    object Home : Action()
    object Back : Action()
    object Complete : Action()
}
```

### 7.2 执行与重试

- 单次动作超时 5 秒。
- 失败重试 1 次，重试前等待 300ms 并重新截图（可选）。
- 连续失败 3 次则暂停并请求用户确认，或降级到传统模式。

## 8. 整理功能迁移

- 保留 `CategoryMatcher`、`categories.json`、使用频率统计等核心业务。
- 将分类结果作为 VLM prompt 中的「建议分类」输入，而非直接执行。
- VLM 可接受、调整或忽略建议分类，最终输出整理计划。
- 原有 `performOrganize()`、`createFolderAndAddItems()` 等逻辑抽入 `LegacyStrategy`，作为兜底。

## 9. 配置机制

在 `PrefsManager` 中新增：

```kotlin
var organizeMode: String // "hybrid" | "vision" | "legacy"
var visionVerifyAfterAction: Boolean // 动作后是否截图校验
var visionMaxSteps: Int
var visionMaxErrors: Int
var visionModelTemperature: Float
var visionPromptTemplate: String // 提示词模板标识
```

UI：

- `SettingsActivity` 增加「整理模式」单选。
- `VlmConfigActivity` 增加「视觉决策参数」（步数上限、重试次数、提示词模板选择）。

## 10. 提示词模板与少样本示例

新增 `assets/prompts/` 目录：

```
assets/prompts/
├── icon_discovery.json      # 发现桌面所有图标
├── icon_categorization.json # 按类别分组
├── folder_creation.json     # 创建文件夹动作
├── folder_population.json   # 将图标拖入文件夹
└── recovery.json            # 异常恢复
```

每个模板包含：

- `system`：角色与约束。
- `user_template`：占位符模板。
- `few_shots`：1-2 组输入输出示例。

示例（icon_discovery）：

```json
{
  "system": "You are a UI understanding assistant. Analyze the Android home screen screenshot and return structured JSON only.",
  "user_template": "Identify all app icons. Return JSON array: [{\"label\":\"\",\"x\":0,\"y\":0,\"width\":0,\"height\":0,\"confidence\":0.0}]",
  "few_shots": []
}
```

## 11. 错误处理

- VLM 返回不可解析 JSON：记录日志，重试一次，重试失败则使用无障碍感知兜底。
- VLM 输出危险坐标（越界、在状态栏/导航栏）：拒绝执行并记录错误。
- 手势执行失败：重试一次，仍失败则按 Home 回到桌面重新感知。
- 连续错误达到阈值：停止并提示用户。

## 12. 日志与可观测性

- 每个阶段输出：截图尺寸、感知元素数、VLM 原始响应、执行动作、结果。
- 保留并扩展 `DiagnosticLogger`。
- 新增「导出本次整理日志」功能（可选）。

## 13. 测试策略

- 单元测试：
  - `PerceptionFusion` 合并规则。
  - `VisionPlanner` JSON 解析与动作生成。
  - `ActionExecutor` 重试逻辑。
- 集成测试（需设备或模拟器）：
  - 截图 → VLM → 手势执行完整流程。
- 回归测试：
  - `LegacyStrategy` 与原有整理流程行为一致。

## 14. 实施顺序

1. 重构 `AutoAppOrganizerService`，引入 `OrganizerFacade` 与 `OrganizeStrategy` 接口。
2. 实现 `PerceptionFusion` 与扩展的 `ScreenElement`。
3. 扩展 `VisionModelService`，新增 `VisionPlannerService` 与 `ActionPlan`。
4. 实现 `HybridStrategy` 和 `VisionStrategy`。
5. 扩展 `Action` 与 `ActionExecutor` 的重试/校验逻辑。
6. 新增配置项与 UI。
7. 添加提示词模板资产与少样本示例。
8. 补充单元测试与日志。
