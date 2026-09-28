package com.arer.app.ui.rates

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arer.app.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RateManagementScreen(
    onBack: () -> Unit,
    viewModel: RateManagementViewModel = hiltViewModel()
) {
    val rows by viewModel.itemsWithRates.collectAsState()

    var showEditDialog by remember { mutableStateOf(false) }
    var selectedRowForEdit by remember { mutableStateOf<RateItemRow?>(null) }
    var newRateInput by remember { mutableStateOf("") }

    var showHistoryDialog by remember { mutableStateOf(false) }
    var selectedRowForHistory by remember { mutableStateOf<RateItemRow?>(null) }
    val historyList = selectedRowForHistory?.let { viewModel.getHistoryForItem(it.item.id).collectAsState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.rate_management)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Header card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Item Name", fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                    Text("Current Rate", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(rows) { row ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(row.item.englishName, fontWeight = FontWeight.Bold)
                                Text(row.item.kannadaName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(row.currentRateFormatted, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(8.dp))
                            TextButton(onClick = {
                                selectedRowForHistory = row
                                showHistoryDialog = true
                            }) {
                                Text(stringResource(id = R.string.rate_history))
                            }
                            Button(onClick = {
                                selectedRowForEdit = row
                                newRateInput = if (row.currentRatePaise != null) (row.currentRatePaise / 100.0).toString() else ""
                                showEditDialog = true
                            }) {
                                Text(stringResource(id = R.string.edit_rate))
                            }
                        }
                    }
                }
            }
        }
    }

    if (showEditDialog && selectedRowForEdit != null) {
        val row = selectedRowForEdit!!
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("${stringResource(id = R.string.edit_rate)}: ${row.item.englishName}") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Current Rate: ${row.currentRateFormatted}")
                    OutlinedTextField(
                        value = newRateInput,
                        onValueChange = { newRateInput = it },
                        label = { Text(stringResource(id = R.string.new_rate) + " (₹)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.saveRate(
                            itemId = row.item.id,
                            rateRupeesStr = newRateInput,
                            effectiveDateMillis = System.currentTimeMillis()
                        ) {
                            showEditDialog = false
                        }
                    }
                ) {
                    Text(stringResource(id = R.string.save_rate))
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showHistoryDialog && selectedRowForHistory != null && historyList != null) {
        val row = selectedRowForHistory!!
        val history = historyList.value
        val dateFormat = SimpleDateFormat("dd-MMM-yyyy", Locale.US)

        AlertDialog(
            onDismissRequest = { showHistoryDialog = false },
            title = { Text("${stringResource(id = R.string.rate_history)}: ${row.item.englishName}") },
            text = {
                LazyColumn(modifier = Modifier.height(250.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    if (history.isEmpty()) {
                        item { Text("No historical rates recorded.") }
                    } else {
                        items(history) { rate ->
                            Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant), modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Effective: ${dateFormat.format(Date(rate.effectiveDate))}")
                                    Text(com.arer.app.domain.model.Money(rate.ratePaise).formatInRupees(), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showHistoryDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}
