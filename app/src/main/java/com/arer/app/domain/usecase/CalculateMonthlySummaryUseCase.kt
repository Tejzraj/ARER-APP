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
            val isMdmDay = !day.isHolidayOn
            val isWorking = !day.isHolidayOn

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
