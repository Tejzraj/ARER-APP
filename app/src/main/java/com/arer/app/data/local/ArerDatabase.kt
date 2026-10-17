package com.arer.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
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
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ArerDatabase : RoomDatabase() {
    abstract fun schoolDao(): SchoolDao
    abstract fun mdmDao(): MdmDao
    abstract fun holidayDao(): HolidayDao
    abstract fun reportDao(): ReportDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS `daily_mdm_entries_new` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `date` INTEGER NOT NULL,
                        `yearMonth` TEXT NOT NULL,
                        `studentCount` INTEGER,
                        `status` TEXT NOT NULL,
                        `isOverridden` INTEGER NOT NULL,
                        `overrideReason` TEXT,
                        `updatedAt` INTEGER NOT NULL
                    )
                """.trimIndent())

                db.execSQL("""
                    INSERT INTO `daily_mdm_entries_new` (`id`, `date`, `yearMonth`, `studentCount`, `status`, `isOverridden`, `overrideReason`, `updatedAt`)
                    SELECT `id`, `date`, `yearMonth`, `studentCount`, `status`, `isOverridden`, `overrideReason`, `updatedAt`
                    FROM `daily_mdm_entries`
                """.trimIndent())

                db.execSQL("DROP TABLE `daily_mdm_entries`")
                db.execSQL("ALTER TABLE `daily_mdm_entries_new` RENAME TO `daily_mdm_entries`")

                db.execSQL("CREATE UNIQUE INDEX IF NOT EXISTS `index_daily_mdm_entries_date` ON `daily_mdm_entries` (`date`)")
                db.execSQL("CREATE INDEX IF NOT EXISTS `index_daily_mdm_entries_yearMonth` ON `daily_mdm_entries` (`yearMonth`)")
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("UPDATE mdm_items SET isActive = 0 WHERE englishName = 'Egg' OR englishName = 'Banana'")
                db.execSQL("""
                    INSERT OR IGNORE INTO mdm_items (id, englishName, kannadaName, calculationType, isActive, createdDate, updatedDate)
                    VALUES (8, 'Egg / Banana', 'ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು', 'PER_STUDENT', 1, 0, 0)
                """.trimIndent())
            }
        }
    }
}
