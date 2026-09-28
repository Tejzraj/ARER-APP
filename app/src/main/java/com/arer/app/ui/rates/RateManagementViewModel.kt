package com.arer.app.ui.rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arer.app.data.local.entity.ItemRateEntity
import com.arer.app.data.local.entity.MDMItemEntity
import com.arer.app.domain.model.Money
import com.arer.app.domain.repository.MdmRepository
import com.arer.app.domain.usecase.ResolveRateForDateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

data class RateItemRow(
    val item: MDMItemEntity,
    val currentRatePaise: Long?,
    val currentRateFormatted: String
)

@HiltViewModel
class RateManagementViewModel @Inject constructor(
    private val mdmRepository: MdmRepository,
    private val resolveRateForDateUseCase: ResolveRateForDateUseCase
) : ViewModel() {

    val itemsWithRates: StateFlow<List<RateItemRow>> = flow {
        mdmRepository.getAllActiveItems().collect { activeItems ->
            val now = System.currentTimeMillis()
            val list = activeItems.map { item ->
                val rate = resolveRateForDateUseCase(item.id, now)
                val paise = rate?.ratePaise
                val formatted = if (paise != null) Money(paise).formatInRupees() else "Not Configured"
                RateItemRow(item, paise, formatted)
            }
            emit(list)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun saveRate(itemId: Long, rateRupeesStr: String, effectiveDateMillis: Long, onSuccess: () -> Unit) {
        val rupees = rateRupeesStr.toDoubleOrNull() ?: return
        if (rupees < 0.0) return

        val paise = Money.fromRupees(rupees).paise

        viewModelScope.launch {
            val entity = ItemRateEntity(
                itemId = itemId,
                ratePaise = paise,
                effectiveDate = effectiveDateMillis
            )
            mdmRepository.saveItemRate(entity)
            mdmRepository.logAudit("RATE_UPDATED", "Updated rate for item ID $itemId to $paise paise")
            onSuccess()
        }
    }

    fun getHistoryForItem(itemId: Long): StateFlow<List<ItemRateEntity>> {
        return mdmRepository.getRatesForItem(itemId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }
}
