package com.arer.app.domain.repository

import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.data.local.entity.HolidayEntity
import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.data.local.entity.MonthlyRateConfirmationEntity
import com.arer.app.data.local.entity.MonthlyReportEntity
import kotlinx.coroutines.flow.Flow

interface MdmRepository {
    fun getAllActiveItems(): Flow<List<MDMItemEntity>>
    suspend fun initializeDefaultMdmItemsIfNeeded()
    fun getRatesForItem(itemId: Long): Flow<List<ItemRateEntity>>
    suspend fun getRateForDate(itemId: Long, timestamp: Long): ItemRateEntity?
    suspend fun saveItemRate(rate: ItemRateEntity)
    fun getEntriesForMonth(yearMonth: String): Flow<List<DailyMDMEntryEntity>>
    suspend fun saveDailyEntry(entry: DailyMDMEntryEntity)
    fun getHolidaysBetween(startDate: Long, endDate: Long): Flow<List<HolidayEntity>>
    suspend fun saveHoliday(holiday: HolidayEntity)
    fun getRateConfirmation(yearMonth: String): Flow<MonthlyRateConfirmationEntity?>
    suspend fun getRateConfirmationSync(yearMonth: String): MonthlyRateConfirmationEntity?
    suspend fun saveRateConfirmation(confirmation: MonthlyRateConfirmationEntity)
    suspend fun logAudit(action: String, details: String)
    fun getAllReports(): Flow<List<MonthlyReportEntity>>
    suspend fun saveReport(report: MonthlyReportEntity, items: List<com.arer.app.data.local.entity.MonthlyReportItemEntity>)
}
