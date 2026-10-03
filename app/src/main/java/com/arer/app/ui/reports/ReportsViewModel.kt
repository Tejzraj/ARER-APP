package com.arer.app.ui.reports

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.entity.HMProfileEntity
import com.arer.app.data.local.entity.MonthlyReportEntity
import com.arer.app.data.local.entity.MonthlyReportItemEntity
import com.arer.app.data.local.entity.SchoolProfileEntity
import com.arer.app.domain.model.CalculationStatus
import com.arer.app.domain.model.MonthlyMdmCalculationResult
import com.arer.app.domain.model.ReportStatus
import com.arer.app.domain.repository.MdmRepository
import com.arer.app.domain.repository.SchoolRepository
import com.arer.app.domain.usecase.CalculateMonthlyMdmUseCase
import com.arer.app.util.CsvReportGenerator
import com.arer.app.util.DateUtils
import com.arer.app.util.PdfReportGenerator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class ReportsViewModel @Inject constructor(
    private val calculateMonthlyMdmUseCase: CalculateMonthlyMdmUseCase,
    schoolRepository: SchoolRepository,
    private val mdmRepository: MdmRepository
) : ViewModel() {

    private val cal = Calendar.getInstance()
    private val _currentYear = MutableStateFlow(cal.get(Calendar.YEAR))
    private val _currentMonth = MutableStateFlow(cal.get(Calendar.MONTH) + 1)

    val yearMonthString: StateFlow<String> = combine(_currentYear, _currentMonth) { y, m ->
        DateUtils.getYearMonthString(y, m)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DateUtils.getYearMonthString(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1))

    val displayMonthTitle: StateFlow<String> = combine(_currentYear, _currentMonth) { y, m ->
        val months = listOf("", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December")
        "${months.getOrElse(m) { "" }} $y"
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")

    private val _calculationResult = MutableStateFlow<MonthlyMdmCalculationResult?>(null)
    val calculationResult: StateFlow<MonthlyMdmCalculationResult?> = _calculationResult.asStateFlow()

    val schoolProfile: StateFlow<SchoolProfileEntity?> = schoolRepository.getSchoolProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val hmProfile: StateFlow<HMProfileEntity?> = schoolRepository.getHMProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        viewModelScope.launch {
            yearMonthString.collect { ym ->
                val res = calculateMonthlyMdmUseCase(ym)
                _calculationResult.value = res
            }
        }
    }

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

    suspend fun exportPdf(context: Context): File? {
        val res = _calculationResult.value ?: return null
        if (res.status != CalculationStatus.SUCCESS) return null

        val school = schoolProfile.first()
        val hm = hmProfile.first()

        // Save snapshot to Room
        saveReportSnapshot(res)

        return PdfReportGenerator.generatePdf(context, res, school, hm)
    }

    suspend fun exportCsv(context: Context): File? {
        val res = _calculationResult.value ?: return null
        if (res.status != CalculationStatus.SUCCESS) return null

        val school = schoolProfile.first()
        val hm = hmProfile.first()

        // Save snapshot to Room
        saveReportSnapshot(res)

        return CsvReportGenerator.generateCsv(context, res, school, hm)
    }

    private suspend fun saveReportSnapshot(res: MonthlyMdmCalculationResult) {
        val reportEntity = MonthlyReportEntity(
            yearMonth = res.yearMonth,
            totalWorkingDays = res.totalWorkingDays,
            totalMdmDays = res.totalMdmDays,
            totalStudentsServed = res.totalStudentsServed,
            grandTotalPaise = res.grandTotalPaise,
            status = ReportStatus.FINALIZED
        )

        val itemEntities = res.itemResults.map { item ->
            MonthlyReportItemEntity(
                reportId = 0L,
                itemId = item.itemId,
                itemNameEnglish = item.englishName,
                itemNameKannada = item.kannadaName,
                calculationType = item.calculationType,
                ratePaise = item.ratePaise,
                applicableCount = item.applicableQuantity.toInt(),
                totalAmountPaise = item.amountPaise
            )
        }

        mdmRepository.saveReport(reportEntity, itemEntities)
        mdmRepository.logAudit("REPORT_FINALIZED", "Finalized and snapshotted monthly report for ${res.yearMonth}")
    }
}
