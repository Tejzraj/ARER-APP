package com.arer.app.domain.model

enum class CalculationStatus {
    SUCCESS,
    RATES_NOT_CONFIRMED,
    INCOMPLETE_ENTRIES,
    MISSING_RATES,
    NO_MDM_DAYS
}

data class MonthlyMdmItemCalculation(
    val itemId: Long,
    val englishName: String,
    val kannadaName: String,
    val calculationType: CalculationType,
    val applicableQuantity: Long,
    val ratePaise: Long,
    val amountPaise: Long
)

data class MonthlyMdmCalculationResult(
    val yearMonth: String,
    val totalCalendarDays: Int,
    val totalWorkingDays: Int,
    val totalMdmDays: Int,
    val completedEntryDays: Int,
    val missingEntryDays: Int,
    val totalStudentsServed: Int,
    val itemResults: List<MonthlyMdmItemCalculation>,
    val grandTotalPaise: Long,
    val status: CalculationStatus,
    val validationIssues: List<String>
)
