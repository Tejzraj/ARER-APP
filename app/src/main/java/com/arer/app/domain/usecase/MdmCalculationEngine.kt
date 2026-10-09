package com.arer.app.domain.usecase

import com.arer.app.domain.model.CalculationStatus
import com.arer.app.domain.model.CalculationType
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import com.arer.app.domain.model.MonthlyMdmItemCalculation

class MdmCalculationEngine {

    fun calculate(
        yearMonth: String,
        days: List<DayUiModel>,
        rates: List<ItemRateInfo>,
        isRateConfirmed: Boolean
    ): MonthlyMdmCalculationResult {
        val validationIssues = mutableListOf<String>()

        if (!isRateConfirmed) {
            return MonthlyMdmCalculationResult(
                yearMonth = yearMonth,
                totalCalendarDays = days.size,
                totalWorkingDays = days.count { !it.isHolidayOn },
                totalMdmDays = days.count { !it.isHolidayOn },
                completedEntryDays = days.count { !it.isHolidayOn && it.studentCount != null },
                missingEntryDays = days.count { it.isHolidayOn.not() && it.studentCount == null },
                totalStudentsServed = 0,
                itemResults = emptyList(),
                grandTotalPaise = 0L,
                status = CalculationStatus.RATES_NOT_CONFIRMED,
                validationIssues = listOf("Monthly rates have not been confirmed.")
            )
        }

        var totalWorkingDays = 0
        var totalMdmDays = 0
        var completedEntryDays = 0
        var missingEntryDays = 0
        var totalStudentsServed = 0

        for (day in days) {
            val isMdmDay = !day.isHolidayOn
            val isWorking = !day.isHolidayOn

            if (isWorking) totalWorkingDays++

            if (isMdmDay) {
                totalMdmDays++
                if (day.studentCount != null) {
                    completedEntryDays++
                    if (day.studentCount < 0) {
                        validationIssues.add("Negative student count detected on date: ${day.dateMillis}")
                    } else {
                        totalStudentsServed += day.studentCount
                    }
                } else {
                    missingEntryDays++
                }
            }
        }

        if (totalMdmDays == 0) {
            return MonthlyMdmCalculationResult(
                yearMonth = yearMonth,
                totalCalendarDays = days.size,
                totalWorkingDays = totalWorkingDays,
                totalMdmDays = totalMdmDays,
                completedEntryDays = completedEntryDays,
                missingEntryDays = missingEntryDays,
                totalStudentsServed = totalStudentsServed,
                itemResults = emptyList(),
                grandTotalPaise = 0L,
                status = CalculationStatus.NO_MDM_DAYS,
                validationIssues = listOf("No applicable MDM days found for this month.")
            )
        }

        val itemResults = mutableListOf<MonthlyMdmItemCalculation>()
        var grandTotalPaise = 0L

        for (rateInfo in rates) {
            val ratePaise = rateInfo.ratePaise
            if (ratePaise == null) {
                // Unconfigured items are omitted without blocking export
                continue
            }

            if (ratePaise < 0L) {
                validationIssues.add("Negative rate detected for ${rateInfo.item.englishName}")
                continue
            }

            val quantity = when (rateInfo.item.calculationType) {
                CalculationType.PER_STUDENT -> totalStudentsServed.toLong()
                CalculationType.FIXED_MONTHLY -> 1L
                CalculationType.CUSTOM_COUNT -> 1L
            }

            val amountPaise = quantity * ratePaise
            grandTotalPaise += amountPaise

            itemResults.add(
                MonthlyMdmItemCalculation(
                    itemId = rateInfo.item.id,
                    englishName = rateInfo.item.englishName,
                    kannadaName = rateInfo.item.kannadaName,
                    calculationType = rateInfo.item.calculationType,
                    applicableQuantity = quantity,
                    ratePaise = ratePaise,
                    amountPaise = amountPaise
                )
            )
        }

        val status = when {
            missingEntryDays > 0 -> CalculationStatus.INCOMPLETE_ENTRIES
            else -> CalculationStatus.SUCCESS
        }

        return MonthlyMdmCalculationResult(
            yearMonth = yearMonth,
            totalCalendarDays = days.size,
            totalWorkingDays = totalWorkingDays,
            totalMdmDays = totalMdmDays,
            completedEntryDays = completedEntryDays,
            missingEntryDays = missingEntryDays,
            totalStudentsServed = totalStudentsServed,
            itemResults = itemResults,
            grandTotalPaise = grandTotalPaise,
            status = status,
            validationIssues = validationIssues
        )
    }
}
