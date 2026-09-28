package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.repository.MdmRepository
import com.arer.app.util.DateUtils
import kotlinx.coroutines.flow.first
import java.util.Calendar
import javax.inject.Inject

data class ItemRateInfo(
    val item: MDMItemEntity,
    val ratePaise: Long?, // null = not configured, 0+ = valid rate
    val rateFormatted: String
)

class ResolveMonthlyRatesUseCase @Inject constructor(
    private val mdmRepository: MdmRepository,
    private val resolveRateForDateUseCase: ResolveRateForDateUseCase
) {
    suspend operator fun invoke(yearMonth: String): List<ItemRateInfo> {
        val parsed = DateUtils.parseYearMonth(yearMonth) ?: return emptyList()
        val (year, month) = parsed

        // Use the 15th of the month as target date for rate resolution
        val calendar = Calendar.getInstance().apply {
            clear()
            set(year, month - 1, 15, 0, 0, 0)
        }
        val targetMillis = calendar.timeInMillis

        val items = mdmRepository.getAllActiveItems().first()
        val result = mutableListOf<ItemRateInfo>()

        for (item in items) {
            val rateEntity = resolveRateForDateUseCase(item.id, targetMillis)
            val paise = rateEntity?.ratePaise
            val formatted = if (paise != null) {
                Money(paise).formatInRupees()
            } else {
                "Not Configured"
            }
            result.add(ItemRateInfo(item = item, ratePaise = paise, rateFormatted = formatted))
        }

        return result
    }
}
