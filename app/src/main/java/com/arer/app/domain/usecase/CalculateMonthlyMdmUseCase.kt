package com.arer.app.domain.usecase

import com.arer.app.domain.model.MonthlyMdmCalculationResult
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class CalculateMonthlyMdmUseCase @Inject constructor(
    private val getMonthlyEntriesUseCase: GetMonthlyEntriesUseCase,
    private val resolveMonthlyRatesUseCase: ResolveMonthlyRatesUseCase
) {
    private val engine = MdmCalculationEngine()

    suspend operator fun invoke(yearMonth: String): MonthlyMdmCalculationResult {
        val days = getMonthlyEntriesUseCase(yearMonth).first()
        val rates = resolveMonthlyRatesUseCase(yearMonth)

        return engine.calculate(
            yearMonth = yearMonth,
            days = days,
            rates = rates,
            isRateConfirmed = true
        )
    }
}
