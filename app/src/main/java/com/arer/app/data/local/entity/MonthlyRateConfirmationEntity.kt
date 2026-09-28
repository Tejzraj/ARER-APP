package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "monthly_rate_confirmations",
    indices = [Index(value = ["yearMonth"], unique = true)]
)
data class MonthlyRateConfirmationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val yearMonth: String, // e.g. "2026-09"
    val isConfirmed: Boolean,
    val confirmedAt: Long = System.currentTimeMillis()
)
