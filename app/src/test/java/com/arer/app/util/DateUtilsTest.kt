package com.arer.app.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class DateUtilsTest {

    @Test
    fn testYearMonthParsing() {
        val parsed = DateUtils.parseYearMonth("2026-09")
        assertNotNull(parsed)
        assertEquals(2026, parsed?.first)
        assertEquals(9, parsed?.second)
    }

    @Test
    fn testGetDatesForMonthSeptember2026() {
        val dates = DateUtils.getDatesForMonth("2026-09")
        assertEquals(30, dates.size)
        assertEquals(1, dates.first().dayOfMonth)
        assertEquals(30, dates.last().dayOfMonth)
    }

    @Test
    fn testLeapYearFebruary2028() {
        val dates = DateUtils.getDatesForMonth("2028-02")
        assertEquals(29, dates.size)
        assertEquals(29, dates.last().dayOfMonth)
    }

    @Test
    fn testNonLeapYearFebruary2027() {
        val dates = DateUtils.getDatesForMonth("2027-02")
        assertEquals(28, dates.size)
        assertEquals(28, dates.last().dayOfMonth)
    }
}
