package com.arer.app.domain.usecase

import com.arer.app.domain.model.DailyEntryStatus
import org.junit.Assert.assertEquals
import org.junit.Test

class MdmDomainTest {

    private val calculateSummary = CalculateMonthlySummaryUseCase()

    @Test
    fun testMonthlyStudentSummationWithZero() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 38, false, null, null),
            DayUiModel(2L, 2, "TUE", DailyEntryStatus.NORMAL, 0, false, null, null),
            DayUiModel(3L, 3, "WED", DailyEntryStatus.NORMAL, 39, false, null, null)
        )

        val summary = calculateSummary(days)
        assertEquals(77, summary.studentsServed)
        assertEquals(3, summary.completedEntriesCount)
    }

    @Test
    fun testMissingVsZeroEntry() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, null, false, null, null), // missing
            DayUiModel(2L, 2, "TUE", DailyEntryStatus.NORMAL, 38, false, null, null)
        )

        val summary = calculateSummary(days)
        assertEquals(38, summary.studentsServed)
        assertEquals(1, summary.completedEntriesCount) // only 1 completed out of 2 mdm days
        assertEquals(2, summary.totalMdmDaysCount)
    }
}
