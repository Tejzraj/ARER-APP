package com.arer.app.data.local

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class ArerMigrationTest {

    private val TEST_DB = "migration-test"

    @get:Rule
    val helper: MigrationTestHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        ArerDatabase::class.java.canonicalName,
        FrameworkSQLiteOpenHelperFactory()
    )

    @Test
    @Throws(IOException::class)
    fun migrate1To2() {
        // Create version 1 database with old schema (studentCount NOT NULL)
        helper.createDatabase(TEST_DB, 1).apply {
            execSQL(
                "INSERT INTO daily_mdm_entries (id, date, yearMonth, studentCount, status, isOverridden, overrideReason, updatedAt) " +
                        "VALUES (1, 1000, '2026-09', 38, 'NORMAL', 0, NULL, 12345)"
            )
            execSQL(
                "INSERT INTO daily_mdm_entries (id, date, yearMonth, studentCount, status, isOverridden, overrideReason, updatedAt) " +
                        "VALUES (2, 2000, '2026-09', 0, 'NORMAL', 0, NULL, 12345)"
            )
            execSQL(
                "INSERT INTO daily_mdm_entries (id, date, yearMonth, studentCount, status, isOverridden, overrideReason, updatedAt) " +
                        "VALUES (3, 3000, '2026-09', 42, 'NORMAL', 0, NULL, 12345)"
            )
            close()
        }

        // Run migration to version 2 and validate schema
        val db = helper.runMigrationsAndValidate(TEST_DB, 2, true, ArerDatabase.MIGRATION_1_2)

        // Verify data preservation and nullability support
        val cursor = db.query("SELECT studentCount FROM daily_mdm_entries ORDER BY id ASC")
        cursor.moveToFirst()
        assertEquals(38, cursor.getInt(0))
        cursor.moveToNext()
        assertEquals(0, cursor.getInt(0))
        cursor.moveToNext()
        assertEquals(42, cursor.getInt(0))
        cursor.close()
    }
}
