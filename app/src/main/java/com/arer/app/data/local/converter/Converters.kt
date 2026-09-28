package com.arer.app.data.local.converter

import androidx.room.TypeConverter
import com.arer.app.domain.model.CalculationType
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.model.HolidayType
import com.arer.app.domain.model.ReportStatus

class Converters {
    @TypeConverter
    fun fromCalculationType(value: CalculationType): String = value.name

    @TypeConverter
    fun toCalculationType(value: String): CalculationType = CalculationType.valueOf(value)

    @TypeConverter
    fun fromHolidayType(value: HolidayType): String = value.name

    @TypeConverter
    fun toHolidayType(value: String): HolidayType = HolidayType.valueOf(value)

    @TypeConverter
    fun fromDailyEntryStatus(value: DailyEntryStatus): String = value.name

    @TypeConverter
    fun toDailyEntryStatus(value: String): DailyEntryStatus = DailyEntryStatus.valueOf(value)

    @TypeConverter
    fun fromReportStatus(value: ReportStatus): String = value.name

    @TypeConverter
    fun toReportStatus(value: String): ReportStatus = ReportStatus.valueOf(value)
}
