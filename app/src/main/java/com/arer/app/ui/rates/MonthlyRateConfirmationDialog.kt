package com.arer.app.ui.rates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arer.app.R
import com.arer.app.domain.usecase.ItemRateInfo

@Composable
fun MonthlyRateConfirmationDialog(
    yearMonth: String,
    rates: List<ItemRateInfo>,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = {}, // Must confirm
        title = { Text("${stringResource(id = R.string.review_mdm_rates)} — $yearMonth") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(id = R.string.rate_confirmation_msg), style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(8.dp))
                LazyColumn(modifier = Modifier.height(280.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(rates) { info ->
                        Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(info.item.englishName, fontWeight = FontWeight.Bold)
                                    Text(info.item.kannadaName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                                }
                                Text(info.rateFormatted, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(stringResource(id = R.string.confirm_rates))
            }
        }
    )
}
