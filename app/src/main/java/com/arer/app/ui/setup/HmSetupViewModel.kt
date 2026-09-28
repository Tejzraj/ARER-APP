package com.arer.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.preferences.AppPreferences
import com.arer.app.domain.repository.SchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HmSetupViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun saveHm(
        hmName: String,
        designation: String,
        hmKgidNumber: String,
        mobileNumber: String,
        onSuccess: () -> Unit
    ) {
        if (hmName.isBlank()) {
            _errorMessage.value = "Please enter the Head Master (HM) name."
            return
        }

        viewModelScope.launch {
            val entity = HMProfileEntity(
                schoolCode = "SCH001",
                hmName = hmName.trim(),
                designation = designation.trim().ifBlank { "Head Master" },
                hmKgidNumber = hmKgidNumber.trim(),
                mobileNumber = mobileNumber.trim()
            )
            schoolRepository.saveHMProfile(entity)
            appPreferences.setHmSetupComplete(true)
            onSuccess()
        }
    }
}
