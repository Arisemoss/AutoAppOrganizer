# 视觉 AI 桌面整理重构# 视觉 AI 桌面整理重构实现计划

> **For agentic workers# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapp# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` |# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 Organ# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapp# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporgan# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapp# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` |# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` |# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/p# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑）# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ]# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val duration# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val from# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) :# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) :# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.to# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${duration# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -># 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapp# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 Action# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
-# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapp# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [Gesture# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS =# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute:# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.to# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.toX, action.toY,
                holdMs# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.toX, action.toY,
                holdMs = action.durationMs / 2,
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.toX, action.toY,
                holdMs = action.durationMs / 2,
                dragMs = action.durationMs / 2
# 视觉 AI 桌面整理重构实现计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将 AutoAppOrganizer 升级为「视觉 AI 主导 + 无障碍感知兜底」的混合增强整理方案，同时保留原有传统模式并支持配置切换。

**Architecture:** 引入 `OrganizerFacade` 统一分发三种 `OrganizeStrategy`（legacy / vision / hybrid）。混合模式通过 `PerceptionFusion` 合并无障碍节点树与 VLM 视觉检测，再由 `VisionPlanner` 向 VLM 请求结构化动作计划，最终由扩展后的 `ActionExecutor` 执行手势并支持重试与降级。

**Tech Stack:** Kotlin, Android AccessibilityService, Coroutines, Gson, Material Components, JUnit4

---

## 文件结构

| 文件 | 职责 |
|------|------|
| `app/src/main/java/com/autoapporganizer/core/action/Action.kt` | 扩展 Action 密封类，新增 Swipe、Type、Complete 等 |
| `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt` | 替换 GestureExecutor，统一执行 Action 并支持重试 |
| `app/src/main/java/com/autoapporganizer/core/plan/ActionPlan.kt` | ActionPlan 与 OrganizePhase 数据类 |
| `app/src/main/java/com/autoapporganizer/core/plan/VisionPlanner.kt` | VisionPlannerService 接口与 CloudVlmPlanner 实现 |
| `app/src/main/java/com/autoapporganizer/core/plan/PromptTemplate.kt` | 提示词模板数据类与 PromptTemplateRepository |
| `app/src/main/java/com/autoapporganizer/core/perception/ScreenElement.kt` | 扩展 ScreenElement 支持 source/confidence/secondaryLabels |
| `app/src/main/java/com/autoapporganizer/core/perception/PerceptionFusion.kt` | 合并无障碍与视觉检测结果 |
| `app/src/main/java/com/autoapporganizer/core/strategy/OrganizeStrategy.kt` | 策略接口与 StrategyResult |
| `app/src/main/java/com/autoapporganizer/core/strategy/LegacyStrategy.kt` | 封装原有无障碍整理流程 |
| `app/src/main/java/com/autoapporganizer/core/strategy/HybridStrategy.kt` | 混合增强策略 |
| `app/src/main/java/com/autoapporganizer/core/strategy/VisionStrategy.kt` | 纯视觉策略 |
| `app/src/main/java/com/autoapporganizer/core/session/OrganizeSessionContext.kt` | 整理会话上下文 |
| `app/src/main/java/com/autoapporganizer/core/OrganizerFacade.kt` | 统一整理入口与模式分发 |
| `app/src/main/java/com/autoapporganizer/util/PrefsManager.kt` | 新增模式与视觉参数配置 |
| `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt` | 接入 OrganizerFacade |
| `app/src/main/java/com/autoapporganizer/ui/MainActivity.kt` | 模式切换入口 |
| `app/src/main/java/com/autoapporganizer/ui/SettingsActivity.kt` | 新增整理模式设置 |
| `app/src/main/java/com/autoapporganizer/ui/VlmConfigActivity.kt` | 新增视觉决策参数 |
| `app/src/main/res/layout/activity_settings.xml` | 新增模式选择 RadioGroup |
| `app/src/main/res/layout/activity_vlm_config.xml` | 新增视觉参数控件 |
| `app/src/main/assets/prompts/icon_discovery.json` | 图标发现提示词模板 |
| `app/src/main/assets/prompts/folder_creation.json` | 文件夹创建提示词模板 |
| `app/src/main/assets/prompts/folder_population.json` | 图标拖入文件夹提示词模板 |
| `app/src/main/assets/prompts/recovery.json` | 异常恢复提示词模板 |
| `app/src/test/java/com/autoapporganizer/core/perception/PerceptionFusionTest.kt` | 感知融合单元测试 |
| `app/src/test/java/com/autoapporganizer/core/plan/VisionPlannerTest.kt` | VLM 响应解析测试 |
| `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt` | 动作执行重试逻辑测试（通过 Robolectric 或纯逻辑） |

---

## Task 1: 扩展 Action 密封类

**Files:**
- Modify: `app/src/main/java/com/autoapporganizer/core/action/Action.kt`
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt`（新建）

- [ ] **Step 1: 编写失败测试**

```kotlin
package com.autoapporganizer.core.action

import org.junit.Assert.assertEquals
import org.junit.Test

class ActionTest {
    @Test
    fun swipe_describe_isCorrect() {
        val action = Action.Swipe(10f, 20f, 30f, 40f)
        assertEquals("Swipe(10,20→30,40)", action.describe())
    }

    @Test
    fun type_describe_isCorrect() {
        val action = Action.Type("hello")
        assertEquals("Type(hello)", action.describe())
    }
}
```

- [ ] **Step 2: 运行测试确认失败**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: 编译失败，`Action.Swipe` 和 `Action.Type` 不存在。

- [ ] **Step 3: 修改 Action.kt 添加 Swipe、Type、Complete 并更新 describe**

```kotlin
package com.autoapporganizer.core.action

/**
 * A closed set of low-level actions the agent can ask the [ActionExecutor] to perform.
 */
sealed class Action {

    /** Tap at ([x], [y]). */
    data class Click(val x: Float, val y: Float) : Action()

    /** Press and hold at ([x], [y]) for [durationMs] milliseconds. */
    data class LongPress(val x: Float, val y: Float, val durationMs: Long = 600L) : Action()

    /** Drag from (fromX, fromY) to (toX, toY) over [durationMs] milliseconds. */
    data class Drag(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 800L
    ) : Action()

    /** Swipe from (fromX, fromY) to (toX, toY). */
    data class Swipe(
        val fromX: Float,
        val fromY: Float,
        val toX: Float,
        val toY: Float,
        val durationMs: Long = 300L
    ) : Action()

    /** Type [text] into the focused input field. */
    data class Type(val text: String) : Action()

    /** Do nothing for [ms] milliseconds. */
    data class Wait(val ms: Long = 500L) : Action()

    /** Press the HOME button. */
    object Home : Action()

    /** Press the BACK button. */
    object Back : Action()

    /** Sentinel marking the end of an action sequence. */
    object Complete : Action()

    fun describe(): String = when (this) {
        is Click -> "Click(${x.toInt()},${y.toInt()})"
        is LongPress -> "LongPress(${x.toInt()},${y.toInt()},${durationMs}ms)"
        is Drag -> "Drag(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Swipe -> "Swipe(${fromX.toInt()},${fromY.toInt()}→${toX.toInt()},${toY.toInt()})"
        is Type -> "Type($text)"
        is Wait -> "Wait(${ms}ms)"
        Home -> "Home"
        Back -> "Back"
        Complete -> "Complete"
    }
}
```

- [ ] **Step 4: 运行测试确认通过**

Run: `./gradlew testDebugUnitTest --tests "com.autoapporganizer.core.action.ActionTest"`
Expected: PASS

- [ ] **Step 5: 提交**

```bash
git add app/src/main/java/com/autoapporganizer/core/action/Action.kt app/src/test/java/com/autoapporganizer/core/action/ActionTest.kt
git commit -m "feat: extend Action sealed class with Swipe, Type and Complete"
```

---

## Task 2: 创建 ActionExecutor 替换 GestureExecutor

**Files:**
- Create: `app/src/main/java/com/autoapporganizer/core/action/ActionExecutor.kt`
- Delete: `app/src/main/java/com/autoapporganizer/core/action/GestureExecutor.kt`（后续步骤替换引用）
- Modify: `app/src/main/java/com/autoapporganizer/service/AutoAppOrganizerService.kt`（后续步骤）
- Test: `app/src/test/java/com/autoapporganizer/core/action/ActionExecutorTest.kt`（新建，纯逻辑测试重试）

- [ ] **Step 1: 创建 ActionExecutor.kt**

```kotlin
package com.autoapporganizer.core.action

import android.accessibilityservice.AccessibilityService
import android.accessibilityservice.GestureDescription
import android.graphics.Bitmap
import android.graphics.Path
import android.os.Build
import android.view.Display
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.autoapporganizer.util.DiagnosticLogger
import kotlinx.coroutines.delay
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import kotlin.coroutines.resume
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.sin

/**
 * Translates high-level [Action]s into platform accessibility gestures and global actions.
 *
 * This class subsumes the older [GestureExecutor]: it supports the same long-press-drag
 * gesture while also handling [Action.Swipe], [Action.Type], batch plan execution and
 * retry/verification hooks.
 */
class ActionExecutor(private val service: AccessibilityService) {

    companion object {
        private const val TAG = "ActionExecutor"
        private const val GESTURE_TIMEOUT_MS = 5000L
        private const val CLICK_DURATION_MS = 100L
        private const val GLOBAL_ACTION_SETTLE_MS = 500L
        private const val SCREENSHOT_TIMEOUT_MS = 3000L
        private const val MAX_RETRIES = 1
    }

    /**
     * Execute a single [action]. Returns true on success.
     */
    suspend fun execute(action: Action): Boolean {
        DiagnosticLogger.debug(TAG, "execute: ${action.describe()}")
        return when (action) {
            is Action.Click -> performClick(action.x, action.y)
            is Action.LongPress -> performLongPress(action.x, action.y, action.durationMs)
            is Action.Drag -> performDrag(
                action.fromX, action.fromY, action.toX, action.toY,
                holdMs = action.durationMs / 2,
                dragMs = action.durationMs / 2
            )
            is Action.Swipe -> performSwipe(action)
            is Action.Type ->