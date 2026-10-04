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

    /** Lock for serializing file access across coroutines/threads */
    private val lock = Any()

    /** 读取全部历史，按时间倒序（最新在前） */
    fun loadAll(): List<OrganizeSession> = synchronized(lock) {
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
    fun append(session: OrganizeSession) = synchronized(lock) {
        val list = loadAllInternal().toMutableList()
        list.add(0, session)
        if (list.size > MAX_RECORDS) list.subList(MAX_RECORDS, list.size).clear()
        save(list)
    }

    /** 删除指定时间戳的记录 */
    fun delete(timestamp: Long) = synchronized(lock) {
        val list = loadAllInternal().filterNot { it.timestamp == timestamp }.toMutableList()
        save(list)
    }

    /** Internal load without lock (called from within synchronized blocks) */
    private fun loadAllInternal(): List<OrganizeSession> {
        if (!file.exists()) return emptyList()
        return try {
            val raw = file.readText()
            if (raw.isBlank()) emptyList()
            else gson.fromJson<MutableList<OrganizeSession>>(raw, type)
                ?: emptyList()
        } catch (e: Exception) {
            // 损坏的历史文件不能被下一次 append 静默清空：先留档再返回空，
            // append 会以空列表重写主文件，损坏数据仅存于 .corrupt 副本中。
            try {
                file.copyTo(File(file.parent, "${file.name}.corrupt"), overwrite = true)
            } catch (ignored: Exception) {
            }
            e.printStackTrace()
            emptyList()
        }
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
            // 与 BackupManager 相同的 tmp+rename 原子写；rename 失败退化为直接写。
            val tmp = File(file.parent, "${file.name}.tmp")
            tmp.writeText(gson.toJson(list))
            if (!tmp.renameTo(file)) {
                file.writeText(gson.toJson(list))
                tmp.delete()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private const val MAX_RECORDS = 50
    }
}
