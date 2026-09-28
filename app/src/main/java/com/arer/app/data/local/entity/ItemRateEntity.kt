package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "item_rates",
    foreignKeys = [
        ForeignKey(
            entity = MDMItemEntity::class,
            parentColumns = ["id"],
            childColumns = ["itemId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["itemId", "effectiveDate"], unique = true)]
)
data class ItemRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val itemId: Long,
    val ratePaise: Long, // Integer minor currency units (paise)
    val effectiveDate: Long, // Epoch timestamp or day identifier
    val createdAt: Long = System.currentTimeMillis()
)
