package com.autoapporganizer.core.feedback

import com.autoapporganizer.core.classification.ClassificationResponse
import com.autoapporganizer.core.classification.CategoryResult
import com.autoapporganizer.core.classification.ClassifiedApp
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [FeedbackCollector].
 */
class FeedbackCollectorTest {

    private lateinit var collector: FeedbackCollector

    @Before
    fun setup() {
        collector = FeedbackCollector()
    }

    @Test
    fun `collect tracks AI classified count`() {
        val response = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    ClassifiedApp("微信", "社交", 0.95f, null),
                    ClassifiedApp("QQ", "社交", 0.92f, null)
                ))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        collector.collect(response, 5)

        val summary = collector.getSummary()
        assertEquals(5, summary.totalIcons)
        assertEquals(2, summary.aiClassifiedCount)
    }

    @Test
    fun `collect tracks low confidence items`() {
        val response = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    ClassifiedApp("微信", "社交", 0.95f, null),
                    ClassifiedApp("未知App", "社交", 0.3f, null) // low confidence
                ))
            ),
            uncertain = listOf(
                ClassifiedApp("不确定App", "不确定", 0.4f, null)
            ),
            thought = "test"
        )

        collector.collect(response, 4)

        val lowConfidence = collector.getLowConfidenceItems()
        assertEquals(2, lowConfidence.size) // 1 low + 1 uncertain
    }

    @Test
    fun `recordCorrection removes from low confidence list`() {
        val response = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    ClassifiedApp("微信", "社交", 0.3f, null) // low confidence
                ))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        collector.collect(response, 1)
        assertEquals(1, collector.getLowConfidenceItems().size)

        collector.recordCorrection("微信", "社交", "即时通讯")
        assertEquals(0, collector.getLowConfidenceItems().size)
    }

    @Test
    fun `reset clears all data`() {
        val response = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    ClassifiedApp("微信", "社交", 0.95f, null)
                ))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        collector.collect(response, 1)
        collector.reset()

        val summary = collector.getSummary()
        assertEquals(0, summary.totalIcons)
        assertEquals(0, summary.aiClassifiedCount)
        assertTrue(collector.getLowConfidenceItems().isEmpty())
    }

    @Test
    fun `summary calculates AI coverage rate`() {
        val response = ClassificationResponse(
            categories = listOf(
                CategoryResult("社交", listOf(
                    ClassifiedApp("微信", "社交", 0.95f, null),
                    ClassifiedApp("QQ", "社交", 0.92f, null)
                ))
            ),
            uncertain = emptyList(),
            thought = "test"
        )

        collector.collect(response, 10)

        val summary = collector.getSummary()
        assertEquals(0.2f, summary.aiCoverageRate, 0.01f) // 2/10
    }

    @Test
    fun `empty response produces zero counts`() {
        val response = ClassificationResponse(
            categories = emptyList(),
            uncertain = emptyList(),
            thought = "test"
        )

        collector.collect(response, 5)

        val summary = collector.getSummary()
        assertEquals(5, summary.totalIcons)
        assertEquals(0, summary.aiClassifiedCount)
        assertEquals(0, summary.lowConfidenceCount)
    }
}
