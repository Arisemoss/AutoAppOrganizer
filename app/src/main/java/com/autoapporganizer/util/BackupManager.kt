package com.autoapporganizer.util

import android.content.Context
import com.autoapporganizer.model.DesktopBackup
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileReader
import java.io.FileWriter

/**
 * 备份管理工具类 —— 持久化桌面布局快照，支持保存、读取、删除。
 *
 * 线程安全：所有文件操作通过 [lock] 串行化，避免并发读写导致数据损坏。
 */
class BackupManager(private val context: Context) {

    companion object {
        private const val TAG = "BackupManager"
    }

    private val gson = Gson()
    private val backupFile: File by lazy {
        File(context.filesDir, "desktop_backup.json")
    }

    /** Lock for serializing file access across coroutines/threads */
    private val lock = Any()

    /**
     * 保存备份
     */
    suspend fun saveBackup(backup: DesktopBackup): Boolean = withContext(Dispatchers.IO) {
        synchronized(lock) {
            try {
                // 写入临时文件再重命名，避免写入中断导致文件损坏
                val tmpFile = File(backupFile.parent, "${backupFile.name}.tmp")
                FileWriter(tmpFile).use { writer ->
                    gson.toJson(backup, writer)
                }
                val ok = tmpFile.renameTo(backupFile)
                if (!ok) {
                    DiagnosticLogger.warn(TAG, "Failed to rename temp file, writing directly")
                    FileWriter(backupFile).use { writer ->
                        gson.toJson(backup, writer)
                    }
                }
                DiagnosticLogger.info(TAG, "Backup saved: ${backup.items.size} items, screen=${backup.screen}")
                true
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "saveBackup failed: ${e.message}")
                false
            }
        }
    }

    /**
     * 读取备份
     */
    suspend fun loadBackup(): DesktopBackup? = withContext(Dispatchers.IO) {
        synchronized(lock) {
            try {
                if (!backupFile.exists()) {
                    DiagnosticLogger.debug(TAG, "No backup file found")
                    return@synchronized null
                }
                val backup = FileReader(backupFile).use { reader ->
                    gson.fromJson(reader, DesktopBackup::class.java)
                }
                DiagnosticLogger.info(TAG, "Backup loaded: ${backup?.items?.size ?: 0} items")
                backup
            } catch (e: Exception) {
                DiagnosticLogger.error(TAG, "loadBackup failed: ${e.message}")
                null
            }
        }
    }

    /**
     * 检查是否有备份
     */
    fun hasBackup(): Boolean {
        return backupFile.exists() && backupFile.length() > 0
    }

    /**
     * 获取备份文件大小（字节）
     */
    fun getBackupSize(): Long {
        return if (backupFile.exists()) backupFile.length() else 0
    }

    /**
     * 删除备份
     */
    fun deleteBackup() {
        synchronized(lock) {
            if (backupFile.exists()) {
                val deleted = backupFile.delete()
                DiagnosticLogger.info(TAG, "Backup deleted: $deleted")
            }
        }
    }
}
