package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.repository.MdmRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

data class ItemRateInfo(
    val item: MDMItemEntity,
    val ratePaise: Long?, // null = not configured, 0+ = valid rate
    val rateFormatted: String
)

class ResolveMonthlyRatesUseCase @Inject constructor(
    private val mdmRepository: MdmRepository
) {
    suspend operator fun invoke(yearMonth: String): List<ItemRateInfo> {
        val items = mdmRepository.getAllActiveItems().first()
        val result = mutableListOf<ItemRateInfo>()

        for (item in items) {
            // Settings -> Rate Management is the single source of truth for current rates
            val rateEntity = mdmRepository.getLatestRateForItem(item.id)
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
