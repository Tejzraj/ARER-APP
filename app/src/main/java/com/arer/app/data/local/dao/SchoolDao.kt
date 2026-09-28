package com.arer.app.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SchoolDao {
    @Query("SELECT * FROM school_profile LIMIT 1")
    fun getSchoolProfile(): Flow<SchoolProfileEntity?>

    @Query("SELECT * FROM school_profile LIMIT 1")
    suspend fun getSchoolProfileSync(): SchoolProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateSchoolProfile(school: SchoolProfileEntity)

    @Query("SELECT * FROM hm_profile LIMIT 1")
    fun getHMProfile(): Flow<HMProfileEntity?>

    @Query("SELECT * FROM hm_profile LIMIT 1")
    suspend fun getHMProfileSync(): HMProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateHMProfile(hm: HMProfileEntity)
}
