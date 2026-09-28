package com.arer.app.ui.entry

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.usecase.CalculateMonthlySummaryUseCase
import com.arer.app.domain.usecase.DayUiModel
import com.arer.app.domain.usecase.GetMonthlyEntriesUseCase
import com.arer.app.domain.usecase.MonthlySummary
import com.arer.app.domain.usecase.SaveDailyMdmEntryUseCase
import com.arer.app.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class MonthlyEntryViewModel @Inject constructor(
    private val getMonthlyEntriesUseCase: GetMonthlyEntriesUseCase,
    private val saveDailyMdmEntryUseCase: SaveDailyMdmEntryUseCase,
    private val calculateMonthlySummaryUseCase: CalculateMonthlySummaryUseCase
) : ViewModel() {

    private val cal = Calendar.getInstance()
    private val _currentYear = MutableStateFlow(cal.get(Calendar.YEAR))
    private val _currentMonth = MutableStateFlow(cal.get(Calendar.MONTH) + 1) // 1-12

    val yearMonthString: StateFlow<String> = combine(_currentYear, _currentMonth) { y, m ->
        DateUtils.getYearMonthString(y, m)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateUtils.getYearMonthString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1))

    val displayMonthTitle: StateFlow<String> = combine(_currentYear, _currentMonth) { y, m ->
        val months = listOf("", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        "${months.getOrElse(m) { "" }} $y"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    @OptIn(ExperimentalCoroutinesApi::class)
    val days: StateFlow<List<DayUiModel>> = yearMonthString.flatMapLatest { ym ->
        getMonthlyEntriesUseCase(ym)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val summary: StateFlow<MonthlySummary> = days.map { list ->
        calculateMonthlySummaryUseCase(list)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MonthlySummary(0, 0, 0, 0, 0))

    fun previousMonth() {
        var m = _currentMonth.value - 1
        var y = _currentYear.value
        if (m < 1) {
            m = 12
            y -= 1
        }
        _currentYear.value = y
        _currentMonth.value = m
    }

    fun nextMonth() {
        var m = _currentMonth.value + 1
        var y = _currentYear.value
        if (m > 12) {
            m = 1
            y += 1
        }
        _currentYear.value = y
        _currentMonth.value = m
    }

    fun updateStudentCount(dateMillis: Long, countStr: String) {
        val count = countStr.toIntOrNull()
        val ym = yearMonthString.value
        val dayModel = days.value.find { it.dateMillis == dateMillis } ?: return

        viewModelScope.launch {
            saveDailyMdmEntryUseCase(
                dateMillis = dateMillis,
                yearMonth = ym,
                studentCount = count,
                status = dayModel.status,
                isOverridden = dayModel.isOverridden,
                overrideReason = dayModel.overrideReason
            )
        }
    }

    fun overrideDay(dateMillis: Long, reason: String) {
        val ym = yearMonthString.value
        val dayModel = days.value.find { it.dateMillis == dateMillis } ?: return

        val newStatus = if (dayModel.status == DailyEntryStatus.HOLIDAY || dayModel.status == DailyEntryStatus.GOVERNMENT_HOLIDAY) {
            DailyEntryStatus.OVERRIDDEN
        } else {
            DailyEntryStatus.HOLIDAY
        }

        viewModelScope.launch {
            saveDailyMdmEntryUseCase(
                dateMillis = dateMillis,
                yearMonth = ym,
                studentCount = dayModel.studentCount,
                status = newStatus,
                isOverridden = true,
                overrideReason = reason.ifBlank { "Special working day override" }
            )
        }
    }

    private fun <T1, T2, R> combine(flow1: StateFlow<T1>, flow2: StateFlow<T2>, transform: (T1, T2) -> R): StateFlow<R> {
        val combined = kotlinx.coroutines.flow.combine(flow1, flow2) { t1, t2 -> transform(t1, t2) }
        return combined.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), transform(flow1.value, flow2.value))
    }
}
