package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.arer.app.domain.model.HolidayType

@Entity(
    tableName = "holidays",
    indices = [Index(value = ["date"], unique = true)]
)
data class HolidayEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val date: Long, // Epoch timestamp at start of day
    val holidayType: HolidayType,
    val reason: String?,
    val isSystemGenerated: Boolean = true
)
