package com.arer.app.domain.usecase

import com.arer.app.data.local.entity.DailyMDMEntryEntity
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.repository.MdmRepository
import javax.inject.Inject

class SaveDailyMdmEntryUseCase @Inject constructor(
    private val mdmRepository: MdmRepository
) {
    suspend operator fun invoke(
        dateMillis: Long,
        yearMonth: String,
        studentCount: Int?,
        status: DailyEntryStatus,
        isOverridden: Boolean,
        overrideReason: String?
    ) {
        val entity = DailyMDMEntryEntity(
            date = dateMillis,
            yearMonth = yearMonth,
            studentCount = studentCount,
            status = status,
            isOverridden = isOverridden,
            overrideReason = overrideReason,
            updatedAt = System.currentTimeMillis()
        )
        mdmRepository.saveDailyEntry(entity)
    }
}
