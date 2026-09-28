package com.arer.app.di

import android.content.Context
import androidx.room.Room
import com.arer.app.data.local.ArerDatabase
import com.arer.app.data.local.dao.HolidayDao
import com.arer.app.data.local.dao.MdmDao
import com.arer.app.data.local.dao.ReportDao
import com.arer.app.data.local.dao.SchoolDao
import com.arer.app.data.repository.MdmRepositoryImpl
import com.arer.app.data.repository.SchoolRepositoryImpl
import com.arer.app.domain.repository.MdmRepository
import com.arer.app.domain.repository.SchoolRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideArerDatabase(@ApplicationContext context: Context): ArerDatabase {
        return Room.databaseBuilder(
            context,
            ArerDatabase::class.java,
            "arer_app_database"
        ).addMigrations(ArerDatabase.MIGRATION_1_2).build()
    }

    @Provides
    fun provideSchoolDao(database: ArerDatabase): SchoolDao = database.schoolDao()

    @Provides
    fun provideMdmDao(database: ArerDatabase): MdmDao = database.mdmDao()

    @Provides
    fun provideHolidayDao(database: ArerDatabase): HolidayDao = database.holidayDao()

    @Provides
    fun provideReportDao(database: ArerDatabase): ReportDao = database.reportDao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSchoolRepository(impl: SchoolRepositoryImpl): SchoolRepository

    @Binds
    @Singleton
    abstract fun bindMdmRepository(impl: MdmRepositoryImpl): MdmRepository
}
