package com.arer.app.util

import java.util.Calendar
import java.util.Locale

data class DateItem(
    val dateMillis: Long,
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val isSunday: Boolean
)

object DateUtils {

    fun getYearMonthString(year: Int, month: Int): String {
        // month: 1-12
        return String.format(Locale.US, "%04d-%02d", year, month)
    }

    fun parseYearMonth(yearMonth: String): Pair<Int, Int>? {
        // "2026-09" -> Pair(2026, 9)
        val parts = yearMonth.split("-")
        if (parts.size != 2) return null
        val y = parts[0].toIntOrNull() ?: return null
        val m = parts[1].toIntOrNull() ?: return null
        return Pair(y, m)
    }

    fun getDatesForMonth(yearMonth: String): List<DateItem> {
        val parsed = parseYearMonth(yearMonth) ?: return emptyList()
        val (year, month) = parsed

        val calendar = Calendar.getInstance().apply {
            clear()
            set(year, month - 1, 1)
        }

        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val list = mutableListOf<DateItem>()

        for (day in 1..maxDay) {
            calendar.set(year, month - 1, day, 0, 0, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val millis = calendar.timeInMillis
            val dayOfWeekEnum = calendar.get(Calendar.DAY_OF_WEEK)
            val isSunday = (dayOfWeekEnum == Calendar.SUNDAY)
            val dayOfWeekStr = when (dayOfWeekEnum) {
                Calendar.SUNDAY -> "SUN"
                Calendar.MONDAY -> "MON"
                Calendar.TUESDAY -> "TUE"
                Calendar.WEDNESDAY -> "WED"
                Calendar.THURSDAY -> "THU"
                Calendar.FRIDAY -> "FRI"
                Calendar.SATURDAY -> "SAT"
                else -> ""
            }

            list.add(DateItem(millis, day, dayOfWeekStr, isSunday))
        }

        return list
    }

    fun normalizeToStartOfDay(millis: Long): Long {
        val calendar = Calendar.getInstance().apply {
            timeInMillis = millis
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return calendar.timeInMillis
    }
}
