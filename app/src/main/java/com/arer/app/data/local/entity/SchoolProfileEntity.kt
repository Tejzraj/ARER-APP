package com.arer.app.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "school_profile",
    indices = [Index(value = ["schoolCode"], unique = true), Index(value = ["udiseCode"], unique = true)]
)
data class SchoolProfileEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val schoolName: String,
    val schoolCode: String,
    val udiseCode: String,
    val kgidNumber: String,
    val district: String,
    val taluk: String,
    val cluster: String,
    val villageTown: String,
    val address: String,
    val pinCode: String,
    val updatedAt: Long = System.currentTimeMillis()
)
