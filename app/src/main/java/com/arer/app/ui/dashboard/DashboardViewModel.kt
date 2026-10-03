package com.arer.app.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.domain.usecase.CalculateMonthlySummaryUseCase
import com.arer.app.domain.usecase.GetMonthlyEntriesUseCase
import com.arer.app.domain.usecase.MonthlySummary
import com.arer.app.domain.repository.SchoolRepository
import com.arer.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    schoolRepository: SchoolRepository,
    getMonthlyEntriesUseCase: GetMonthlyEntriesUseCase,
    calculateMonthlySummaryUseCase: CalculateMonthlySummaryUseCase
) : ViewModel() {

    val schoolName: StateFlow<String> = schoolRepository.getSchoolProfile()
        .map { it?.schoolName ?: "ARER Government School" }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "ARER Government School"
        )

    private val cal = Calendar.getInstance()
    private val currentYearMonth = DateUtils.getYearMonthString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1)

    private val months = listOf("", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
    val currentMonthTitle: StateFlow<String> = MutableStateFlow(
        "${months.getOrElse(cal.get(Calendar.MONTH) + 1) { "" }} ${cal.get(Calendar.YEAR)}"
    ).asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    val monthlySummary: StateFlow<MonthlySummary> = getMonthlyEntriesUseCase(currentYearMonth)
        .map { days -> calculateMonthlySummaryUseCase(days) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MonthlySummary(0, 0, 0, 0, 0)
        )
}
