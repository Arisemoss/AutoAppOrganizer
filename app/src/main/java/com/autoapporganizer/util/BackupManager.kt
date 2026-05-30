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
 * 备份管理工具类
 */
class BackupManager(private val context: Context) {
    
    private val gson = Gson()
    private val backupFile: File by lazy {
        File(context.filesDir, "desktop_backup.json")
    }
    
    /**
     * 保存备份
     */
    suspend fun saveBackup(backup: DesktopBackup): Boolean = withContext(Dispatchers.IO) {
        return@withContext try {
            FileWriter(backupFile).use { writer ->
                gson.toJson(backup, writer)
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
    
    /**
     * 读取备份
     */
    suspend fun loadBackup(): DesktopBackup? = withContext(Dispatchers.IO) {
        return@withContext try {
            if (backupFile.exists()) {
                FileReader(backupFile).use { reader ->
                    gson.fromJson(reader, DesktopBackup::class.java)
                }
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
    
    /**
     * 检查是否有备份
     */
    fun hasBackup(): Boolean {
        return backupFile.exists()
    }
    
    /**
     * 删除备份
     */
    fun deleteBackup() {
        if (backupFile.exists()) {
            backupFile.delete()
        }
    }
}
