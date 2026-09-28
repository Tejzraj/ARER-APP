package com.arer.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.arer.app.data.local.converter.Converters
import com.arer.app.data.local.dao.HolidayDao
import com.arer.app.data.local.dao.MdmDao
import com.arer.app.data.local.dao.ReportDao
import com.arer.app.data.local.dao.SchoolDao
import com.arer.app.data.local.entity.AuditLogEntity
import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.HolidayEntity
import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.data.local.entity.MonthlyRateConfirmationEntity
import com.arer.app.data.local.entity.MonthlyReportEntity
import com.arer.app.data.local.entity.MonthlyReportItemEntity
import com.arer.app.data.local.entity.SchoolProfileEntity

@Database(
    entities = [
        SchoolProfileEntity::class,
        HMProfileEntity::class,
        MDMItemEntity::class,
        ItemRateEntity::class,
        HolidayEntity::class,
        DailyMDMEntryEntity::class,
        MonthlyRateConfirmationEntity::class,
        MonthlyReportEntity::class,
        MonthlyReportItemEntity::class,
        AuditLogEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArerDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun mdmDao(): MdmDao
    abstract fun holidayDao(): HolidayDao
    abstract fun reportDao(): ReportDao
}
