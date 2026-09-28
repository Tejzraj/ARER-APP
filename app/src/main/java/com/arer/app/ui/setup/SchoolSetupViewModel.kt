package com.arer.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.data.local.preferences.AppPreferences
import com.arer.app.domain.repository.SchoolRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SchoolSetupViewModel @Inject constructor(
    private val schoolRepository: SchoolRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    fun saveSchool(
        schoolName: String,
        schoolCode: String,
        udiseCode: String,
        kgidNumber: String,
        district: String,
        taluk: String,
        cluster: String,
        villageTown: String,
        address: String,
        pinCode: String,
        onSuccess: () -> Unit
    ) {
        if (schoolName.isBlank()) {
            _errorMessage.value = "Please enter the school name."
            return
        }
        if (pinCode.isNotBlank() && pinCode.length != 6) {
            _errorMessage.value = "PIN Code must be 6 digits."
            return
        }

        viewModelScope.launch {
            val entity = SchoolProfileEntity(
                schoolName = schoolName.trim(),
                schoolCode = schoolCode.trim().ifBlank { "SCH001" },
                udiseCode = udiseCode.trim(),
                kgidNumber = kgidNumber.trim(),
                district = district.trim(),
                taluk = taluk.trim(),
                cluster = cluster.trim(),
                villageTown = villageTown.trim(),
                address = address.trim(),
                pinCode = pinCode.trim()
            )
            schoolRepository.saveSchoolProfile(entity)
            appPreferences.setSchoolSetupComplete(true)
            onSuccess()
        }
    }
}
