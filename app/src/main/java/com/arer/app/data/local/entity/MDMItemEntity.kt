package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arer.app.domain.model.CalculationType

@Entity(tableName = "mdm_items")
data class MDMItemEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val englishName: String,
    val kannadaName: String,
    val calculationType: CalculationType,
    val isActive: Boolean = true,
    val createdDate: Long = System.currentTimeMillis(),
    val updatedDate: Long = System.currentTimeMillis()
)
