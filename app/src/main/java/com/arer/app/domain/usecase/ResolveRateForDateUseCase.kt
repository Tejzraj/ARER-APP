package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.domain.repository.MdmRepository
import javax.inject.Inject

class ResolveRateForDateUseCase @Inject constructor(
    private val mdmRepository: MdmRepository
) {
    suspend operator fun invoke(itemId: Long, targetDateMillis: Long): ItemRateEntity? {
        return mdmRepository.getRateForDate(itemId, targetDateMillis)
    }
}
