package com.autoapporganizer.util

import android.os.Build
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * 诊断日志单例 — 收集无障碍服务运行时诊断信息，
 * 便于排查"无法分析桌面图标"等问题。
 *
 * 特性：
 * - 最多保留 500 条日志（自动裁剪旧条目）
 * - 支持按级别过滤
 * - 支持导出完整日志文本
 * - 线程安全（StateFlow）
 */
object DiagnosticLogger {

    data class LogEntry(
        val timestamp: Long = System.currentTimeMillis(),
        val level: Level,
        val tag: String,
        val message: String
    ) {
        val formatted: String
            get() {
                val sdf = SimpleDateFormat("HH:mm:ss.SSS", Locale.getDefault())
                val time = sdf.format(Date(timestamp))
                val lvl = when (level) {
                    Level.INFO -> "I"
                    Level.WARN -> "W"
                    Level.ERROR -> "E"
                    Level.DEBUG -> "D"
                    Level.SCAN -> "S"
                }
                return "[$time $lvl/$tag] $message"
            }

        enum class Level { INFO, WARN, ERROR, DEBUG, SCAN }
    }

    private const val MAX_ENTRIES = 500

    private val _entries = MutableStateFlow<List<LogEntry>>(emptyList())
    val entries: StateFlow<List<LogEntry>> = _entries.asStateFlow()

    /** 设备/系统汇总信息 */
    private val _summary = MutableStateFlow("")
    val summary: StateFlow<String> = _summary.asStateFlow()

    fun info(tag: String, msg: String) = log(LogEntry.Level.INFO, tag, msg)
    fun warn(tag: String, msg: String) = log(LogEntry.Level.WARN, tag, msg)
    fun error(tag: String, msg: String) = log(LogEntry.Level.ERROR, tag, msg)
    fun debug(tag: String, msg: String) = log(LogEntry.Level.DEBUG, tag, msg)
    /** 扫描日志 — 用于输出节点扫描的详细信息 */
    fun scan(tag: String, msg: String) = log(LogEntry.Level.SCAN, tag, msg)

    private fun log(level: LogEntry.Level, tag: String, msg: String) {
        val entry = LogEntry(level = level, tag = tag, message = msg)
        _entries.value = (_entries.value + entry).takeLast(MAX_ENTRIES)
    }

    fun clear() {
        _entries.value = emptyList()
    }

    /** 设置设备/系统汇总信息 */
    fun setSummary(summary: String) {
        _summary.value = summary
    }

    /** 获取指定级别的日志 */
    fun getEntriesByLevel(level: LogEntry.Level): List<LogEntry> {
        return _entries.value.filter { it.level == level }
    }

    /** 获取最近 N 条日志 */
    fun getRecent(count: Int): List<LogEntry> {
        return _entries.value.takeLast(count)
    }

    /** 获取错误和警告数量 */
    fun getErrorCount(): Int = _entries.value.count { it.level == LogEntry.Level.ERROR }
    fun getWarnCount(): Int = _entries.value.count { it.level == LogEntry.Level.WARN }

    /** 检查是否有错误 */
    fun hasErrors(): Boolean = getErrorCount() > 0

    /** 获取日志总条数 */
    fun size(): Int = _entries.value.size

    /** 获取所有日志文本（用于复制） */
    fun dumpAll(): String {
        val sb = StringBuilder()
        sb.appendLine("=== AutoAppOrganizer 诊断日志 ===")
        sb.appendLine("设备: ${Build.MANUFACTURER} ${Build.MODEL}")
        sb.appendLine("Android: ${Build.VERSION.RELEASE} (SDK ${Build.VERSION.SDK_INT})")
        sb.appendLine("品牌: ${Build.BRAND}")
        sb.appendLine("Launcher: ${_summary.value}")
        sb.appendLine("日志条数: ${_entries.value.size}")
        sb.appendLine("错误数: ${getErrorCount()}, 警告数: ${getWarnCount()}")
        sb.appendLine("=================================")
        _entries.value.forEach { sb.appendLine(it.formatted) }
        return sb.toString()
    }

    /** 导出指定时间范围内的日志 */
    fun dumpSince(sinceTimestamp: Long): String {
        val filtered = _entries.value.filter { it.timestamp >= sinceTimestamp }
        val sb = StringBuilder()
        sb.appendLine("=== 日志 (since ${SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(sinceTimestamp))}) ===")
        filtered.forEach { sb.appendLine(it.formatted) }
        return sb.toString()
    }
}
