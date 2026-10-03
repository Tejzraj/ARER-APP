package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.data.local.entity.HolidayEntity
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.model.HolidayType
import com.arer.app.domain.repository.MdmRepository
import com.arer.app.util.DateItem
import com.arer.app.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class DayUiModel(
    val dateMillis: Long,
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val status: DailyEntryStatus,
    val studentCount: Int?, // null = missing, 0+ = served count
    val isOverridden: Boolean,
    val overrideReason: String?,
    val holidayReason: String?,
    val isHolidayOn: Boolean = false
)

class GetMonthlyEntriesUseCase @Inject constructor(
    private val mdmRepository: MdmRepository
) {
    operator fun invoke(yearMonth: String): Flow<List<DayUiModel>> {
        val dateItems = DateUtils.getDatesForMonth(yearMonth)
        if (dateItems.isEmpty()) {
            return kotlinx.coroutines.flow.flowOf(emptyList())
        }

        val startDate = dateItems.first().dateMillis
        val endDate = dateItems.last().dateMillis

        return combine(
            mdmRepository.getEntriesForMonth(yearMonth),
            mdmRepository.getHolidaysBetween(startDate, endDate)
        ) { entries, holidays ->
            val entryMap = entries.associateBy { it.date }
            val holidayMap = holidays.associateBy { it.date }

            dateItems.map { dateItem ->
                val existingEntry = entryMap[dateItem.dateMillis]
                val holiday = holidayMap[dateItem.dateMillis]

                val defaultStatus = when {
                    existingEntry != null -> existingEntry.status
                    dateItem.isSunday -> DailyEntryStatus.HOLIDAY
                    holiday != null -> DailyEntryStatus.GOVERNMENT_HOLIDAY
                    else -> DailyEntryStatus.NORMAL
                }

                val isHolidayOn = when {
                    existingEntry != null && existingEntry.isOverridden -> {
                        existingEntry.status == DailyEntryStatus.HOLIDAY || existingEntry.status == DailyEntryStatus.GOVERNMENT_HOLIDAY
                    }
                    existingEntry != null -> {
                        existingEntry.status == DailyEntryStatus.HOLIDAY || existingEntry.status == DailyEntryStatus.GOVERNMENT_HOLIDAY
                    }
                    else -> dateItem.isSunday || holiday != null
                }

                DayUiModel(
                    dateMillis = dateItem.dateMillis,
                    dayOfMonth = dateItem.dayOfMonth,
                    dayOfWeek = dateItem.dayOfWeek,
                    status = defaultStatus,
                    studentCount = existingEntry?.studentCount,
                    isOverridden = existingEntry?.isOverridden ?: false,
                    overrideReason = existingEntry?.overrideReason,
                    holidayReason = holiday?.reason ?: if (dateItem.isSunday) "Sunday" else null,
                    isHolidayOn = isHolidayOn
                )
            }
        }
    }
}
