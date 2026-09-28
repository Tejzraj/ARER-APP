package com.arer.app.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.preferences.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

data class AppState(
    val isLoggedIn: Boolean = false,
    val isSchoolSetupComplete: Boolean = false,
    val isHmSetupComplete: Boolean = false,
    val isMdmSetupComplete: Boolean = false
)

@HiltViewModel
class SplashViewModel @Inject constructor(
    appPreferences: AppPreferences
) : ViewModel() {

    val appState: StateFlow<AppState> = combine(
        appPreferences.isLoggedIn,
        appPreferences.isSchoolSetupComplete,
        appPreferences.isHmSetupComplete,
        appPreferences.isMdmSetupComplete
    ) { loggedIn, schoolSetup, hmSetup, mdmSetup ->
        AppState(loggedIn, schoolSetup, hmSetup, mdmSetup)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = AppState()
    )
}
