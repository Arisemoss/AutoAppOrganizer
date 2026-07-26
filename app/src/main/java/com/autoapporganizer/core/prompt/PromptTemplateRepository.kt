package com.autoapporganizer.core.prompt

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Loads prompt templates from `assets/prompts/` and resolves the best template
 * for a given pipeline stage and optional device hint.
 */
class PromptTemplateRepository(context: Context) {

    private val templates: List<PromptTemplate> by lazy { loadAll(context) }

    /** Return all loaded templates. */
    fun all(): List<PromptTemplate> = templates

    /** Return templates for a specific stage. */
    fun forStage(stage: String): List<PromptTemplate> =
        templates.filter { it.stage.equals(stage, ignoreCase = true) }

    /**
     * Pick the best template for [stage].
     *
     * If [deviceHint] is provided and a template targets that device, prefer it.
     * Otherwise fall back to the generic template for that stage.
     *
     * @return the selected template, or a safe default if none is configured.
     */
    fun resolve(stage: String, deviceHint: String = ""): PromptTemplate {
        val candidates = forStage(stage)
        if (candidates.isEmpty()) return defaultFor(stage)

        if (deviceHint.isNotBlank()) {
            val exact = candidates.firstOrNull { it.deviceHint.equals(deviceHint, ignoreCase = true) }
            if (exact != null) return exact
        }
        return candidates.firstOrNull { it.deviceHint.isBlank() }
            ?: candidates.first()
    }

    /** Render [template] by replacing simple `{key}` placeholders with [values]. */
    fun render(template: PromptTemplate, values: Map<String, String>): String {
        var text = template.content
        values.forEach { (k, v) -> text = text.replace("{$k}", v) }
        if (template.exampleShot.isNotBlank()) {
            text += "\n\n--- Example ---\n${template.exampleShot}"
        }
        return text
    }

    private fun loadAll(context: Context): List<PromptTemplate> {
        return try {
            val files = context.assets.list(PROMPTS_DIR) ?: emptyArray()
            val gson = Gson()
            val listType = object : TypeToken<List<PromptTemplate>>() {}.type
            files.filter { it.endsWith(".json") }
                .flatMap { file ->
                    context.assets.open("$PROMPTS_DIR/$file").bufferedReader().use { reader ->
                        gson.fromJson<List<PromptTemplate>>(reader, listType) ?: emptyList()
                    }
                }
        } catch (e: Exception) {
            emptyList()
        }
    }

    private fun defaultFor(stage: String): PromptTemplate = when (stage.lowercase()) {
        "scan" -> PromptTemplate(
            id = "default_scan",
            stage = "scan",
            name = "Default Scan",
            content = "List all visible app icons on this Android home screen. For each icon, give label and bounding box in JSON."
        )
        "plan" -> PromptTemplate(
            id = "default_plan",
            stage = "plan",
            name = "Default Plan",
            content = "You are an Android UI automation assistant. Given a screenshot, decide the next action to organize apps into folders. Return JSON with 'thought' and 'actions'."
        )
        "execute" -> PromptTemplate(
            id = "default_execute",
            stage = "execute",
            name = "Default Execute",
            content = "Verify the previous action result and decide whether to continue or finish. Return JSON with 'thought' and 'actions'."
        )
        else -> PromptTemplate(
            id = "default",
            stage = stage,
            name = "Default",
            content = ""
        )
    }

    companion object {
        private const val PROMPTS_DIR = "prompts"
    }
}
