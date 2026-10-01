package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.domain.model.CalculationStatus
import com.arer.app.domain.model.CalculationType
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.model.Money
import org.junit.Assert.assertEquals
import org.junit.Test

class MdmCalculationEngineTest {

    private val engine = MdmCalculationEngine()

    @Test
    fun testPerStudentCalculation() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 856, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 1L, englishName = "Sambar Items", kannadaName = "ಸಾಂಬಾರ್", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 55L, // ₹0.55
                rateFormatted = "₹0.55"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = true)
        assertEquals(CalculationStatus.SUCCESS, result.status)
        assertEquals(856, result.totalStudentsServed)
        assertEquals(47080L, result.grandTotalPaise) // 856 * 55 = 47080 paise (₹470.80)
        assertEquals("₹470.80", Money(result.grandTotalPaise).formatInRupees())
    }

    @Test
    fun testEggCalculation() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 856, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 2L, englishName = "Egg", kannadaName = "ಮೊಟ್ಟೆ", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 600L, // ₹6.00
                rateFormatted = "₹6.00"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = true)
        assertEquals(CalculationStatus.SUCCESS, result.status)
        assertEquals(513600L, result.grandTotalPaise) // 856 * 600 = 513600 paise (₹5,136.00)
    }

    @Test
    fun testUnconfirmedRatesReturnsError() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 100, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 1L, englishName = "Vegetables", kannadaName = "ತರಕಾರಿ", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 180L,
                rateFormatted = "₹1.80"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = false)
        assertEquals(CalculationStatus.RATES_NOT_CONFIRMED, result.status)
    }

    @Test
    fun testNullVsZeroStudentCount() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, null, false, null, null), // missing
            DayUiModel(2L, 2, "TUE", DailyEntryStatus.NORMAL, 0, false, null, null)     // explicit zero
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 1L, englishName = "Salt", kannadaName = "ಉಪ್ಪು", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 4L,
                rateFormatted = "₹0.04"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = true)
        assertEquals(CalculationStatus.INCOMPLETE_ENTRIES, result.status)
        assertEquals(0, result.totalStudentsServed) // 0 served
        assertEquals(1, result.missingEntryDays)
        assertEquals(1, result.completedEntryDays)
    }

    @Test
    fun testMissingRateHandling() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 100, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 1L, englishName = "Milk", kannadaName = "ಹಾಲು", calculationType = CalculationType.PER_STUDENT),
                ratePaise = null, // not configured
                rateFormatted = "Not Configured"
            ),
            ItemRateInfo(
                item = MDMItemEntity(id = 2L, englishName = "Salt", kannadaName = "ಉಪ್ಪು", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 4L,
                rateFormatted = "₹0.04"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = true)
        // Under Phase 8, missing unrelated rates do not block export, so calculation succeeds and omits the unconfigured item
        assertEquals(CalculationStatus.SUCCESS, result.status)
        assertEquals(1, result.itemResults.size) // only Salt included
        assertEquals(400L, result.grandTotalPaise) // 100 * 4 = 400 paise
    }

    @Test
    fun testConfiguredZeroRate() {
        val days = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 100, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 1L, englishName = "Milk", kannadaName = "ಹಾಲು", calculationType = CalculationType.PER_STUDENT),
                ratePaise = 0L, // valid ₹0.00 rate
                rateFormatted = "₹0.00"
            )
        )

        val result = engine.calculate("2026-09", days, rates, isRateConfirmed = true)
        assertEquals(CalculationStatus.SUCCESS, result.status)
        assertEquals(0L, result.grandTotalPaise)
    }

    @Test
    fun testGiriniIndependentMonthlyCost() {
        val days50 = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 50, false, null, null)
        )
        val days100 = listOf(
            DayUiModel(1L, 1, "MON", DailyEntryStatus.NORMAL, 100, false, null, null)
        )
        val rates = listOf(
            ItemRateInfo(
                item = MDMItemEntity(id = 10L, englishName = "Girini", kannadaName = "ಗಿರಿಣಿ", calculationType = CalculationType.FIXED_MONTHLY),
                ratePaise = 50000L, // ₹500.00 fixed monthly
                rateFormatted = "₹500.00"
            )
        )

        val result50 = engine.calculate("2026-09", days50, rates, isRateConfirmed = true)
        val result100 = engine.calculate("2026-09", days100, rates, isRateConfirmed = true)

        assertEquals(CalculationStatus.SUCCESS, result50.status)
        assertEquals(CalculationStatus.SUCCESS, result100.status)

        // Girini amount must be exactly 50000 paise (₹500.00) in both cases, regardless of students (50 vs 100)
        assertEquals(50000L, result50.grandTotalPaise)
        assertEquals(50000L, result100.grandTotalPaise)
        assertEquals(50000L, result50.itemResults[0].amountPaise)
        assertEquals(50000L, result100.itemResults[0].amountPaise)
    }
}
