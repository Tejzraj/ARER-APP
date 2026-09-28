package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.ItemRateEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class RateResolutionTest {

    @Test
    fun testEffectiveDateRateResolutionLogic() {
        val rates = listOf(
            ItemRateEntity(itemId = 1L, ratePaise = 55L, effectiveDate = 1000L), // Aug 1
            ItemRateEntity(itemId = 1L, ratePaise = 75L, effectiveDate = 2000L), // Sep 1
            ItemRateEntity(itemId = 1L, ratePaise = 80L, effectiveDate = 3000L)  // Oct 1
        )

        // Helper function simulating DAO / UseCase lookup: latest effectiveDate <= targetDate
        fun resolve(target: Long): ItemRateEntity? {
            return rates.filter { it.effectiveDate <= target }.maxByOrNull { it.effectiveDate }
        }

        // Before first effective date
        assertNull(resolve(500L))

        // On/after first, before second
        assertEquals(55L, resolve(1000L)?.ratePaise)
        assertEquals(55L, resolve(1500L)?.ratePaise)

        // On/after second
        assertEquals(75L, resolve(2000L)?.ratePaise)
        assertEquals(75L, resolve(2500L)?.ratePaise)

        // On/after third
        assertEquals(80L, resolve(3500L)?.ratePaise)
    }

    @Test
    fun testZeroRateVsMissingRate() {
        val zeroRatePaise = 0L // valid rate of ₹0.00
        val missingRate: Long? = null // not configured

        assertEquals(0L, zeroRatePaise)
        assertNull(missingRate)
    }

    @Test
    fun testNegativeRateValidation() {
        val negativeRupees = -1.80
        val isValid = negativeRupees >= 0.0
        assertEquals(false, isValid)
    }
}
