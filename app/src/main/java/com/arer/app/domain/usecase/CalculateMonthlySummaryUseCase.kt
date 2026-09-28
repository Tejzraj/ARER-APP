package com.arer.app.domain.usecase

import com.arer.app.domain.model.DailyEntryStatus
import javax.inject.Inject

data class MonthlySummary(
    val workingDays: Int,
    val mdmDays: Int,
    val studentsServed: Int,
    val completedEntriesCount: Int,
    val totalMdmDaysCount: Int
)

class CalculateMonthlySummaryUseCase @Inject constructor() {

    operator fun invoke(days: List<DayUiModel>): MonthlySummary {
        var workingDays = 0
        var mdmDays = 0
        var studentsServed = 0
        var completedCount = 0
        var totalMdmDays = 0

        for (day in days) {
            val isWorking = when (day.status) {
                DailyEntryStatus.NORMAL -> true
                DailyEntryStatus.OVERRIDDEN -> day.isOverridden && (day.studentCount != null && day.studentCount > 0 || day.status == DailyEntryStatus.OVERRIDDEN)
                DailyEntryStatus.SUNDAY, DailyEntryStatus.GOVERNMENT_HOLIDAY -> false
            }

            // Determine if it's an applicable MDM day (Working day or Overridden working day)
            val isMdmDay = when (day.status) {
                DailyEntryStatus.NORMAL -> true
                DailyEntryStatus.OVERRIDDEN -> true
                DailyEntryStatus.SUNDAY, DailyEntryStatus.GOVERNMENT_HOLIDAY -> day.isOverridden
            }

            if (isWorking) {
                workingDays++
            }

            if (isMdmDay) {
                totalMdmDays++
                mdmDays++
                if (day.studentCount != null) {
                    completedCount++
                    studentsServed += day.studentCount
                }
            }
        }

        return MonthlySummary(
            workingDays = workingDays,
            mdmDays = mdmDays,
            studentsServed = studentsServed,
            completedEntriesCount = completedCount,
            totalMdmDaysCount = totalMdmDays
        )
    }
}
