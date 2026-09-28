package com.arer.app.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "arer_app_preferences")

@Singleton
class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    object Keys {
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
        val SCHOOL_SETUP_COMPLETE = booleanPreferencesKey("school_setup_complete")
        val HM_SETUP_COMPLETE = booleanPreferencesKey("hm_setup_complete")
        val MDM_SETUP_COMPLETE = booleanPreferencesKey("mdm_setup_complete")
    }

    val isLoggedIn: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.IS_LOGGED_IN] ?: false
    }

    val isSchoolSetupComplete: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.SCHOOL_SETUP_COMPLETE] ?: false
    }

    val isHmSetupComplete: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.HM_SETUP_COMPLETE] ?: false
    }

    val isMdmSetupComplete: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[Keys.MDM_SETUP_COMPLETE] ?: false
    }

    suspend fun setLoggedIn(loggedIn: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.IS_LOGGED_IN] = loggedIn
        }
    }

    suspend fun setSchoolSetupComplete(complete: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.SCHOOL_SETUP_COMPLETE] = complete
        }
    }

    suspend fun setHmSetupComplete(complete: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.HM_SETUP_COMPLETE] = complete
        }
    }

    suspend fun setMdmSetupComplete(complete: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.MDM_SETUP_COMPLETE] = complete
        }
    }

    suspend fun clearAll() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
