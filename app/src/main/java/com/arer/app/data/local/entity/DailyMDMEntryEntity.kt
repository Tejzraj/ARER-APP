package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arer.app.domain.model.DailyEntryStatus

@Entity(
    tableName = "daily_mdm_entries",
    indices = [Index(value = ["date"], unique = true), Index(value = ["yearMonth"])]
)
data class DailyMDMEntryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long, // Epoch timestamp at start of day
    val yearMonth: String, // e.g. "2026-09"
    val studentCount: Int?, // Nullable: null = missing entry, 0+ = actual served count
    val status: DailyEntryStatus,
    val isOverridden: Boolean = false,
    val overrideReason: String?,
    val updatedAt: Long = System.currentTimeMillis()
)
