package com.autoapporganizer.util

import android.content.Context
import com.autoapporganizer.model.OrganizeSession
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

/**
 * 整理历史管理器 —— 持久化记录每次整理会话。
 * 存储为 JSON 文件（最近 50 条），供历史页与统计概览读取。
 */
class HistoryManager(private val context: Context) {

    private val gson = Gson()
    private val file: File by lazy { File(context.filesDir, "organize_history.json") }

    private val type = object : TypeToken<MutableList<OrganizeSession>>() {}.type

    /** 读取全部历史，按时间倒序（最新在前） */
    fun loadAll(): List<OrganizeSession> {
        if (!file.exists()) return emptyList()
        return try {
            val raw = file.readText()
            if (raw.isBlank()) emptyList()
            else gson.fromJson<MutableList<OrganizeSession>>(raw, type)?.sortedByDescending { it.timestamp }
                ?: emptyList()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    /** 追加一条会话，自动裁剪到最近 50 条 */
    fun append(session: OrganizeSession) {
        val list = loadAll().toMutableList()
        list.add(0, session)
        if (list.size > MAX_RECORDS) list.subList(MAX_RECORDS, list.size).clear()
        save(list)
    }

    /** 删除指定时间戳的记录 */
    fun delete(timestamp: Long) {
        val list = loadAll().filterNot { it.timestamp == timestamp }.toMutableList()
        save(list)
    }

    /** 清空全部历史 */
    fun clear() {
        if (file.exists()) file.delete()
    }

    /** 最近一次整理记录（用于概览卡片） */
    fun latest(): OrganizeSession? = loadAll().firstOrNull()

    /** 累计整理次数 */
    fun totalSessions(): Int = loadAll().size

    private fun save(list: List<OrganizeSession>) {
        try {
            file.writeText(gson.toJson(list))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MAX_RECORDS = 50
    }
}
