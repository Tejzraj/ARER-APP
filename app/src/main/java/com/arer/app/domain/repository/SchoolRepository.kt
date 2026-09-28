package com.arer.app.domain.repository

import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import kotlinx.coroutines.flow.Flow

interface SchoolRepository {
    fun getSchoolProfile(): Flow<SchoolProfileEntity?>
    suspend fun saveSchoolProfile(school: SchoolProfileEntity)
    fun getHMProfile(): Flow<HMProfileEntity?>
    suspend fun saveHMProfile(hm: HMProfileEntity)
}
