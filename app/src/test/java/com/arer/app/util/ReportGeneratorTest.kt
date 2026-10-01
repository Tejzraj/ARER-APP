package com.arer.app.util

import com.arer.app.domain.model.CalculationStatus
import com.arer.app.domain.model.CalculationType
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import com.arer.app.domain.model.MonthlyMdmItemCalculation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReportGeneratorTest {

    @Test
    fun testCalculationResultForReport() {
        val itemResult = MonthlyMdmItemCalculation(
            itemId = 1L,
            englishName = "Sambar Items",
            kannadaName = "ಸಾಂಬಾರ್ ಪದಾರ್ಥಗಳು",
            calculationType = CalculationType.PER_STUDENT,
            applicableQuantity = 856L,
            ratePaise = 55L,
            amountPaise = 47080L
        )

        val result = MonthlyMdmCalculationResult(
            yearMonth = "2026-09",
            totalCalendarDays = 30,
            totalWorkingDays = 22,
            totalMdmDays = 21,
            completedEntryDays = 21,
            missingEntryDays = 0,
            totalStudentsServed = 856,
            itemResults = listOf(itemResult),
            grandTotalPaise = 47080L,
            status = CalculationStatus.SUCCESS,
            validationIssues = emptyList()
        )

        assertEquals("2026-09", result.yearMonth)
        assertEquals(47080L, result.grandTotalPaise)
        assertEquals("ಸಾಂಬಾರ್ ಪದಾರ್ಥಗಳು", result.itemResults[0].kannadaName)
        assertTrue(result.validationIssues.isEmpty())
    }
}
