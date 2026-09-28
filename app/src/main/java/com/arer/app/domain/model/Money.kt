package com.arer.app.domain.model

import java.text.NumberFormat
import java.util.Locale

@JvmInline
value class Money(val paise: Long) {
    
    operator fun plus(other: Money): Money = Money(this.paise + other.paise)
    operator fun minus(other: Money): Money = Money(this.paise - other.paise)
    operator fun times(multiplier: Long): Money = Money(this.paise * multiplier)
    operator fun times(multiplier: Int): Money = Money(this.paise * multiplier)
    operator fun times(multiplier: Double): Money = Money((this.paise * multiplier).toLong())

    fun toRupees(): Double = paise / 100.0

    fun formatInRupees(): String {
        val rupees = toRupees()
        val formatter = NumberFormat.getNumberInstance(Locale.Builder().setLanguage("en").setRegion("IN").build()).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return "₹${formatter.format(rupees)}"
    }

    companion object {
        val ZERO = Money(0L)

        fun fromRupees(rupees: Double): Money {
            return Money(Math.round(rupees * 100))
        }

        fun fromRupeesString(value: String): Money {
            val d = value.toDoubleOrNull() ?: 0.0
            return fromRupees(d)
        }
    }
}
