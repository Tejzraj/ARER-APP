package com.arer.app.ui.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.data.local.preferences.AppPreferences
import com.arer.app.domain.repository.MdmRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MdmSetupViewModel @Inject constructor(
    private val mdmRepository: MdmRepository,
    private val appPreferences: AppPreferences
) : ViewModel() {

    val items: StateFlow<List<MDMItemEntity>> = mdmRepository.getAllActiveItems()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    init {
        viewModelScope.launch {
            mdmRepository.initializeDefaultMdmItemsIfNeeded()
        }
    }

    fun completeMdmSetup(onSuccess: () -> Unit) {
        viewModelScope.launch {
            appPreferences.setMdmSetupComplete(true)
            onSuccess()
        }
    }
}
