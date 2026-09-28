package com.arer.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.data.local.entity.MonthlyRateConfirmationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MdmDao {
    @Query("SELECT * FROM mdm_items WHERE isActive = 1")
    fun getAllActiveItems(): Flow<List<MDMItemEntity>>

    @Query("SELECT * FROM mdm_items")
    suspend fun getAllItemsSync(): List<MDMItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: MDMItemEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItems(items: List<MDMItemEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRate(rate: ItemRateEntity)

    @Query("SELECT * FROM item_rates WHERE itemId = :itemId AND effectiveDate <= :timestamp ORDER BY effectiveDate DESC LIMIT 1")
    suspend fun getRateForDate(itemId: Long, timestamp: Long): ItemRateEntity?

    @Query("SELECT * FROM daily_mdm_entries WHERE yearMonth = :yearMonth")
    fun getEntriesForMonth(yearMonth: String): Flow<List<DailyMDMEntryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDailyEntry(entry: DailyMDMEntryEntity)

    @Query("SELECT * FROM monthly_rate_confirmations WHERE yearMonth = :yearMonth LIMIT 1")
    fun getRateConfirmation(yearMonth: String): Flow<MonthlyRateConfirmationEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRateConfirmation(confirmation: MonthlyRateConfirmationEntity)
}
