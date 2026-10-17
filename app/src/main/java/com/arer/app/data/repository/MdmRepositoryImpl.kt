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
                MDMItemEntity(id = 1L, englishName = "Vegetables", kannadaName = "ತರಕಾರಿಗಳು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 2L, englishName = "Sambar Items", kannadaName = "ಸಾಂಬಾರ್ ಪದಾರ್ಥಗಳು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 3L, englishName = "Salt", kannadaName = "ಉಪ್ಪು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 4L, englishName = "Sugar", kannadaName = "ಸಕ್ಕರೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 5L, englishName = "Dal", kannadaName = "ಬೇಳೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 6L, englishName = "Oil", kannadaName = "ಎಣ್ಣೆ", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 7L, englishName = "Gas", kannadaName = "ಗ್ಯಾಸ್", calculationType = CalculationType.FIXED_MONTHLY),
                MDMItemEntity(id = 8L, englishName = "Egg / Banana", kannadaName = "ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 9L, englishName = "Milk", kannadaName = "ಹಾಲು", calculationType = CalculationType.PER_STUDENT),
                MDMItemEntity(id = 10L, englishName = "Girini", kannadaName = "ಗಿರಿಣಿ", calculationType = CalculationType.FIXED_MONTHLY)
            )
            mdmDao.insertItems(defaultItems)

            val defaultRates = listOf(
                ItemRateEntity(itemId = 1L, ratePaise = 180L, effectiveDate = 0L),
                ItemRateEntity(itemId = 2L, ratePaise = 55L, effectiveDate = 0L),
                ItemRateEntity(itemId = 3L, ratePaise = 4L, effectiveDate = 0L),
                ItemRateEntity(itemId = 4L, ratePaise = 32L, effectiveDate = 0L),
                ItemRateEntity(itemId = 5L, ratePaise = 252L, effectiveDate = 0L),
                ItemRateEntity(itemId = 6L, ratePaise = 77L, effectiveDate = 0L),
                ItemRateEntity(itemId = 7L, ratePaise = 110000L, effectiveDate = 0L),
                ItemRateEntity(itemId = 8L, ratePaise = 700L, effectiveDate = 0L),
                ItemRateEntity(itemId = 9L, ratePaise = 0L, effectiveDate = 0L),
                ItemRateEntity(itemId = 10L, ratePaise = 50000L, effectiveDate = 0L)
            )
            for (rate in defaultRates) {
                mdmDao.insertRate(rate)
            }
        } else {
            var foundCombined = false
            for (item in existing) {
                if (item.englishName == "Egg" || item.englishName == "Banana") {
                    mdmDao.insertItem(item.copy(isActive = false))
                } else if (item.englishName == "Egg / Banana" || item.id == 8L) {
                    foundCombined = true
                    if (!item.isActive || item.englishName != "Egg / Banana") {
                        mdmDao.insertItem(
                            item.copy(
                                englishName = "Egg / Banana",
                                kannadaName = "ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು",
                                calculationType = CalculationType.PER_STUDENT,
                                isActive = true
                            )
                        )
                    }
                }
            }
            if (!foundCombined) {
                mdmDao.insertItem(
                    MDMItemEntity(
                        id = 8L,
                        englishName = "Egg / Banana",
                        kannadaName = "ಮೊಟ್ಟೆ / ಬಾಳೆಹಣ್ಣು",
                        calculationType = CalculationType.PER_STUDENT,
                        isActive = true
                    )
                )
                if (mdmDao.getLatestRateForItem(8L) == null) {
                    mdmDao.insertRate(ItemRateEntity(itemId = 8L, ratePaise = 700L, effectiveDate = 0L))
                }
            }
        }
    }

    override fun getRatesForItem(itemId: Long): Flow<List<ItemRateEntity>> = mdmDao.getRatesForItem(itemId)

    override suspend fun getRateForDate(itemId: Long, timestamp: Long): ItemRateEntity? = mdmDao.getRateForDate(itemId, timestamp)

    override suspend fun getLatestRateForItem(itemId: Long): ItemRateEntity? = mdmDao.getLatestRateForItem(itemId)

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
