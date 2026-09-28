package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arer.app.domain.model.CalculationType

@Entity(
    tableName = "monthly_report_items",
    foreignKeys = [
        ForeignKey(
            entity = MonthlyReportEntity::class,
            parentColumns = ["id"],
            childColumns = ["reportId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["reportId"])]
)
data class MonthlyReportItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val reportId: Long,
    val itemId: Long,
    val itemNameEnglish: String,
    val itemNameKannada: String,
    val calculationType: CalculationType,
    val ratePaise: Long,
    val applicableCount: Int,
    val totalAmountPaise: Long
)
