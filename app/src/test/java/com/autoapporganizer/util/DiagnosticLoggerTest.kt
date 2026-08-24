package com.autoapporganizer.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [DiagnosticLogger].
 */
class DiagnosticLoggerTest {

    @Before
    fun setup() {
        DiagnosticLogger.clear()
    }

    @Test
    fun `log adds entries`() {
        DiagnosticLogger.info("Test", "Info message")
        DiagnosticLogger.warn("Test", "Warn message")
        DiagnosticLogger.error("Test", "Error message")

        assertEquals(3, DiagnosticLogger.size())
    }

    @Test
    fun `clear removes all entries`() {
        DiagnosticLogger.info("Test", "Message")
        DiagnosticLogger.clear()
        assertEquals(0, DiagnosticLogger.size())
    }

    @Test
    fun `getEntriesByLevel filters correctly`() {
        DiagnosticLogger.info("Test", "Info")
        DiagnosticLogger.warn("Test", "Warn")
        DiagnosticLogger.error("Test", "Error")
        DiagnosticLogger.debug("Test", "Debug")

        assertEquals(1, DiagnosticLogger.getEntriesByLevel(DiagnosticLogger.LogEntry.Level.INFO).size)
        assertEquals(1, DiagnosticLogger.getEntriesByLevel(DiagnosticLogger.LogEntry.Level.WARN).size)
        assertEquals(1, DiagnosticLogger.getEntriesByLevel(DiagnosticLogger.LogEntry.Level.ERROR).size)
        assertEquals(1, DiagnosticLogger.getEntriesByLevel(DiagnosticLogger.LogEntry.Level.DEBUG).size)
    }

    @Test
    fun `getRecent returns last N entries`() {
        for (i in 1..10) {
            DiagnosticLogger.info("Test", "Message $i")
        }

        val recent = DiagnosticLogger.getRecent(3)
        assertEquals(3, recent.size)
        assertEquals("Message 8", recent[0].message)
        assertEquals("Message 9", recent[1].message)
        assertEquals("Message 10", recent[2].message)
    }

    @Test
    fun `getErrorCount counts errors`() {
        DiagnosticLogger.info("Test", "Info")
        DiagnosticLogger.error("Test", "Error 1")
        DiagnosticLogger.error("Test", "Error 2")

        assertEquals(2, DiagnosticLogger.getErrorCount())
    }

    @Test
    fun `getWarnCount counts warnings`() {
        DiagnosticLogger.info("Test", "Info")
        DiagnosticLogger.warn("Test", "Warn 1")
        DiagnosticLogger.warn("Test", "Warn 2")
        DiagnosticLogger.warn("Test", "Warn 3")

        assertEquals(3, DiagnosticLogger.getWarnCount())
    }

    @Test
    fun `hasErrors returns true when errors exist`() {
        assertFalse(DiagnosticLogger.hasErrors())

        DiagnosticLogger.error("Test", "Error")
        assertTrue(DiagnosticLogger.hasErrors())
    }

    @Test
    fun `dumpAll includes device info`() {
        DiagnosticLogger.info("Test", "Message")
        val dump = DiagnosticLogger.dumpAll()

        assertTrue(dump.contains("AutoAppOrganizer 诊断日志"))
        assertTrue(dump.contains("Message"))
    }

    @Test
    fun `max entries is respected`() {
        for (i in 1..600) {
            DiagnosticLogger.info("Test", "Message $i")
        }

        assertTrue(DiagnosticLogger.size() <= 500)
    }

    @Test
    fun `setSummary updates summary`() {
        DiagnosticLogger.setSummary("Test Summary")
        assertEquals("Test Summary", DiagnosticLogger.summary.value)
    }

    @Test
    fun `log entry formatted string is correct`() {
        val entry = DiagnosticLogger.LogEntry(
            timestamp = 1700000000000L,
            level = DiagnosticLogger.LogEntry.Level.INFO,
            tag = "TestTag",
            message = "Test message"
        )

        val formatted = entry.formatted
        assertTrue(formatted.contains("I"))
        assertTrue(formatted.contains("TestTag"))
        assertTrue(formatted.contains("Test message"))
    }
}
