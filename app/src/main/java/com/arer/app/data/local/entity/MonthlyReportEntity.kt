package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arer.app.domain.model.ReportStatus

@Entity(
    tableName = "monthly_reports",
    indices = [Index(value = ["yearMonth"], unique = true)]
)
data class MonthlyReportEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val yearMonth: String, // e.g. "2026-09"
    val totalWorkingDays: Int,
    val totalMdmDays: Int,
    val totalStudentsServed: Int,
    val grandTotalPaise: Long, // Integer minor currency units (paise)
    val status: ReportStatus,
    val generatedAt: Long = System.currentTimeMillis(),
    val pdfFilePath: String? = null
)
