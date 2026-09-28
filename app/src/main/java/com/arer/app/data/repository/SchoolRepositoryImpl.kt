package com.arer.app.data.repository

import com.arer.app.data.local.dao.SchoolDao
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.domain.repository.SchoolRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SchoolRepositoryImpl @Inject constructor(
    private val schoolDao: SchoolDao
) : SchoolRepository {

    override fun getSchoolProfile(): Flow<SchoolProfileEntity?> = schoolDao.getSchoolProfile()

    override suspend fun saveSchoolProfile(school: SchoolProfileEntity) {
        schoolDao.insertOrUpdateSchoolProfile(school)
    }

    override fun getHMProfile(): Flow<HMProfileEntity?> = schoolDao.getHMProfile()

    override suspend fun saveHMProfile(hm: HMProfileEntity) {
        schoolDao.insertOrUpdateHMProfile(hm)
    }
}
