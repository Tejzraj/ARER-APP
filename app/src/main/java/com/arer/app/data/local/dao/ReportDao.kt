package com.arer.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.arer.app.data.local.entity.MonthlyReportEntity
import com.arer.app.data.local.entity.MonthlyReportItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM monthly_reports ORDER BY yearMonth DESC")
    fun getAllReports(): Flow<List<MonthlyReportEntity>>

    @Query("SELECT * FROM monthly_reports WHERE yearMonth = :yearMonth LIMIT 1")
    suspend fun getReportForMonth(yearMonth: String): MonthlyReportEntity?

    @Query("SELECT * FROM monthly_report_items WHERE reportId = :reportId")
    suspend fun getReportItems(reportId: Long): List<MonthlyReportItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: MonthlyReportEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReportItems(items: List<MonthlyReportItemEntity>)

    @Transaction
    suspend fun saveCompleteReport(report: MonthlyReportEntity, items: List<MonthlyReportItemEntity>) {
        val reportId = insertReport(report)
        val itemsWithReportId = items.map { it.copy(reportId = reportId) }
        insertReportItems(itemsWithReportId)
    }
}
