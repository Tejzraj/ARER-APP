package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hm_profile")
data class HMProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val schoolCode: String,
    val hmName: String,
    val designation: String,
    val hmKgidNumber: String,
    val mobileNumber: String,
    val updatedAt: Long = System.currentTimeMillis()
)
