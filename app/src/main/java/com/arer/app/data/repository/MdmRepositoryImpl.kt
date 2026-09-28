package com.arer.app.data.repository

import com.arer.app.data.local.dao.HolidayDao
import com.arer.app.data.local.dao.MdmDao
import com.arer.app.data.local.dao.ReportDao
import com.arer.app.data.local.entity.AuditLogEntity
import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.data.local.entity.HolidayEntity
import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.data.local.entity.MonthlyRateConfirmationEntity
import com.arer.app.data.local.entity.MonthlyReportEntity
import com.arer.app.data.local.entity.MonthlyReportItemEntity
import com.arer.app.domain.model.CalculationType
import com.arer.app.domain.repository.MdmRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MdmRepositoryImpl @Inject constructor(
    private val mdmDao: MdmDao,
    private val holidayDao: HolidayDao,
    private val reportDao: ReportDao
) : MdmRepository {

    override fun getAllActiveItems(): Flow<List<MDMItemEntity>> = mdmDao.getAllActiveItems()

    override suspend fun initializeDefaultMdmItemsIfNeeded() {
        val existing = mdmDao.getAllItemsSync()
        if (existing.isEmpty()) {
            val defaultItems = listOf(
                MDMItemEntity(englishName = "Vegetables", kannadaName = "ತರಕಾರಿಗಳು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Sambar Items", kannadaName = "ಸಾಂಬಾರ್ ಪದಾರ್ಥಗಳು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Salt", kannadaName = "ಉಪ್ಪು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Sugar", kannadaName = "ಸಕ್ಕರೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Dal", kannadaName = "ಬೇಳೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Oil", kannadaName = "ಎಣ್ಣೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Gas", kannadaName = "ಗ್ಯಾಸ್", calculationType = CalculationType.FIXED_MONTHLY),
                MDMItemEntity(englishName = "Egg", kannadaName = "ಮೊಟ್ಟೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Milk", kannadaName = "ಹಾಲು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(englishName = "Girini", kannadaName = "ಗಿರಿಣಿ", calculationType = CalculationType.CUSTOM_COUNT),
                MDMItemEntity(englishName = "Banana", kannadaName = "ಬಾಳೆಹಣ್ಣು", calculationType = CalculationType.PER_STUDENT)
            )
            mdmDao.insertItems(defaultItems)
        }
    }

    override fun getRatesForItem(itemId: Long): Flow<List<ItemRateEntity>> = mdmDao.getRatesForItem(itemId)

    override suspend fun getRateForDate(itemId: Long, timestamp: Long): ItemRateEntity? = mdmDao.getRateForDate(itemId, timestamp)

    override suspend fun saveItemRate(rate: ItemRateEntity) {
        mdmDao.insertRate(rate)
    }

    override fun getEntriesForMonth(yearMonth: String): Flow<List<DailyMDMEntryEntity>> = mdmDao.getEntriesForMonth(yearMonth)

    override suspend fun saveDailyEntry(entry: DailyMDMEntryEntity) {
        mdmDao.insertDailyEntry(entry)
    }

    override fun getHolidaysBetween(startDate: Long, endDate: Long): Flow<List<HolidayEntity>> =
        holidayDao.getHolidaysBetween(startDate, endDate)

    override suspend fun saveHoliday(holiday: HolidayEntity) {
        holidayDao.insertHoliday(holiday)
    }

    override fun getRateConfirmation(yearMonth: String): Flow<MonthlyRateConfirmationEntity?> =
        mdmDao.getRateConfirmation(yearMonth)

    override suspend fun getRateConfirmationSync(yearMonth: String): MonthlyRateConfirmationEntity? =
        mdmDao.getRateConfirmationSync(yearMonth)

    override suspend fun saveRateConfirmation(confirmation: MonthlyRateConfirmationEntity) {
        mdmDao.insertRateConfirmation(confirmation)
    }

    override suspend fun logAudit(action: String, details: String) {
        mdmDao.insertAuditLog(AuditLogEntity(action = action, details = details))
    }

    override fun getAllReports(): Flow<List<MonthlyReportEntity>> = reportDao.getAllReports()

    override suspend fun saveReport(report: MonthlyReportEntity, items: List<MonthlyReportItemEntity>) {
        reportDao.saveCompleteReport(report, items)
    }
}
