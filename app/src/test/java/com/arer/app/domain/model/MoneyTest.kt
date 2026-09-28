package com.arer.app.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class MoneyTest {

    @Test
    fun testMoneyArithmetic() {
        val m1 = Money(55) // ₹0.55
        val m2 = Money(180) // ₹1.80

        val sum = m1 + m2
        assertEquals(235L, sum.paise)

        val multiplied = m1 * 10
        assertEquals(550L, multiplied.paise)

        val multipliedStudents = m2 * 856
        assertEquals(154080L, multipliedStudents.paise)
    }

    @Test
    fun testRupeesConversion() {
        val money = Money(748156L) // ₹7,481.56
        assertEquals(7481.56, money.toRupees(), 0.001)

        val fromR = Money.fromRupees(7481.56)
        assertEquals(748156L, fromR.paise)
    }

    @Test
    fun testFormatting() {
        val money = Money(154080L)
        val formatted = money.formatInRupees()
        assertEquals("₹1,540.80", formatted)
    }
}
