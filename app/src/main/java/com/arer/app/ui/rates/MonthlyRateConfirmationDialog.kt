package com.arer.app.ui.rates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.arer.app.R
import com.arer.app.domain.usecase.ItemRateInfo

@Composable
fun MonthlyRateConfirmationDialog(
    yearMonth: String,
    rates: List<ItemRateInfo>,
    onSaveRate: (itemId: Long, rateStr: String) -> Unit,
    onConfirm: () -> Unit
) {
    val rateInputs = remember(rates) {
        mutableStateMapOf<Long, String>().apply {
            rates.forEach { info ->
                put(info.item.id, if (info.ratePaise != null) (info.ratePaise / 100.0).toString() else "")
            }
        }
    }

    AlertDialog(
        onDismissRequest = {}, // Must confirm
        title = { Text("${stringResource(id = R.string.review_mdm_rates)} — $yearMonth") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(id = R.string.rate_confirmation_msg), style = MaterialTheme.typography.bodyMedium)
                Text("You can edit rates below before confirming.", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(300.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(rates) { info ->
                        var currentInput by remember(info.item.id) {
                            mutableStateOf(rateInputs[info.item.id] ?: if (info.ratePaise != null) (info.ratePaise / 100.0).toString() else "")
                        }

                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(info.item.englishName, fontWeight = FontWeight.Bold)
                                    Text(info.item.kannadaName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                }
                                OutlinedTextField(
                                    value = currentInput,
                                    onValueChange = { 
                                        currentInput = it
                                        rateInputs[info.item.id] = it
                                    },
                                    label = { Text("Rate (₹)") },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier.width(110.dp),
                                    singleLine = true
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                TextButton(onClick = {
                                    onSaveRate(info.item.id, currentInput)
                                }) {
                                    Text("Save")
                                }
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                rateInputs.forEach { (itemId, str) ->
                    if (str.isNotBlank()) {
                        onSaveRate(itemId, str)
                    }
                }
                onConfirm()
            }) {
                Text(stringResource(id = R.string.confirm_rates))
            }
        }
    )
}
