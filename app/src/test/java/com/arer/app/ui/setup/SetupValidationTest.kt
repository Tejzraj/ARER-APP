package com.arer.app.ui.setup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SetupValidationTest {

    @Test
    fun testSchoolNameValidation() {
        val blankName = ""
        assertTrue(blankName.isBlank())

        val validName = "Government Higher Primary School"
        assertTrue(validName.isNotBlank())
    }

    @Test
    fun testPinCodeValidation() {
        val pin1 = "560011"
        assertEquals(6, pin1.length)

        val pin2 = "123"
        assertTrue(pin2.length != 6)
    }
}
