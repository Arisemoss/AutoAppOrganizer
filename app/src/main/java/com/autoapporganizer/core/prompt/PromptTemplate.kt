package com.autoapporganizer.core.prompt

/**
 * A prompt template used at a specific stage of the visual organizing pipeline.
 *
 * @property id Stable template identifier.
 * @property stage One of `scan`, `plan`, `execute`.
 * @property name Human-readable name for debugging/selection UI.
 * @property content The prompt text; may contain placeholders such as `{screen_width}`,
 *                   `{screen_height}`, `{apps}`.
 * @property exampleShot Optional few-shot example appended to the prompt.
 * @property deviceHint Optional device/launcher hint that this template targets.
 */
data class PromptTemplate(
    val id: String,
    val stage: String,
    val name: String,
    val content: String,
    val exampleShot: String = "",
    val deviceHint: String = ""
)
