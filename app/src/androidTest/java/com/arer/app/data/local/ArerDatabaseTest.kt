package com.arer.app.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arer.app.data.local.dao.SchoolDao
import com.arer.app.data.local.entity.SchoolProfileEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ArerDatabaseTest {
    private lateinit var db: ArerDatabase
    private lateinit var schoolDao: SchoolDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, ArerDatabase::class.java).build()
        schoolDao = db.schoolDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeAndReadSchoolProfile() = runBlocking {
        val school = SchoolProfileEntity(
            schoolName = "Government Higher Primary School",
            schoolCode = "SCH001",
            udiseCode = "29123456789",
            kgidNumber = "KGID123",
            district = "Bengaluru",
            taluk = "Bengaluru South",
            cluster = "Jayanagar",
            villageTown = "Bengaluru",
            address = "Main Road",
            pinCode = "560011"
        )
        schoolDao.insertOrUpdateSchoolProfile(school)
        val loaded = schoolDao.getSchoolProfile().first()
        assertNotNull(loaded)
        assertEquals("Government Higher Primary School", loaded?.schoolName)
        assertEquals("SCH001", loaded?.schoolCode)
    }
}
