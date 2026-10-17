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

    @Test
    fun testCsvGenerationWithBomAndKannadaText() {
        val itemResult = MonthlyMdmItemCalculation(
            itemId = 8L,
            englishName = "Egg / Banana",
            kannadaName = "ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು",
            calculationType = CalculationType.PER_STUDENT,
            applicableQuantity = 100L,
            ratePaise = 700L,
            amountPaise = 70000L
        )

        val tempFile = java.io.File.createTempFile("test_report", ".csv")
        tempFile.deleteOnExit()

        val outputStream = java.io.FileOutputStream(tempFile)
        outputStream.write(byteArrayOf(0xEF.toByte(), 0xBB.toByte(), 0xBF.toByte()))
        val writer = java.io.OutputStreamWriter(outputStream, java.nio.charset.StandardCharsets.UTF_8)
        writer.write("\"Sl. No.\",\"Item Name (English)\",\"Item Name (ಕನ್ನಡ)\"\n")
        writer.write("\"1\",\"${itemResult.englishName}\",\"${itemResult.kannadaName}\"\n")
        writer.flush()
        writer.close()

        val bytes = tempFile.readBytes()
        assertEquals(0xEF.toByte(), bytes[0])
        assertEquals(0xBB.toByte(), bytes[1])
        assertEquals(0xBF.toByte(), bytes[2])

        val content = String(bytes, 3, bytes.size - 3, java.nio.charset.StandardCharsets.UTF_8)
        assertTrue(content.contains("Egg / Banana"))
        assertTrue(content.contains("ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು"))
    }
}
