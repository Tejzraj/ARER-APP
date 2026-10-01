package com.arer.app.ui.reports

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arer.app.R
import com.arer.app.domain.model.CalculationStatus
import com.arer.app.domain.model.Money
import com.arer.app.util.FileUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(
    onBack: () -> Unit,
    viewModel: ReportsViewModel = hiltViewModel()
) {
    val displayTitle by viewModel.displayMonthTitle.collectAsState()
    val calculationResult by viewModel.calculationResult.collectAsState()
    val school by viewModel.schoolProfile.collectAsState()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.view_reports)) },
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
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Compact Month Selector Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { viewModel.previousMonth() }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous Month")
                    }
                    Text(text = displayTitle, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    IconButton(onClick = { viewModel.nextMonth() }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next Month")
                    }
                }
            }

            if (calculationResult == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                val res = calculationResult!!

                // Status Banner
                val statusContainerColor = when (res.status) {
                    CalculationStatus.SUCCESS -> MaterialTheme.colorScheme.primaryContainer
                    else -> MaterialTheme.colorScheme.errorContainer
                }
                val statusTextColor = when (res.status) {
                    CalculationStatus.SUCCESS -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onErrorContainer
                }

                Card(
                    colors = CardDefaults.cardColors(containerColor = statusContainerColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        val statusText = when (res.status) {
                            CalculationStatus.SUCCESS -> stringResource(id = R.string.calculation_success)
                            CalculationStatus.RATES_NOT_CONFIRMED -> stringResource(id = R.string.rates_not_configured)
                            CalculationStatus.INCOMPLETE_ENTRIES -> stringResource(id = R.string.incomplete_entries)
                            CalculationStatus.MISSING_RATES -> stringResource(id = R.string.missing_item_rates)
                            CalculationStatus.NO_MDM_DAYS -> "No MDM Days Found"
                        }
                        Text(text = "Status: $statusText", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, color = statusTextColor)
                        if (res.validationIssues.isNotEmpty()) {
                            for (issue in res.validationIssues) {
                                Text(text = "• $issue", style = MaterialTheme.typography.bodySmall, color = statusTextColor)
                            }
                        }
                    }
                }

                // Compact Summary Dashboard Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(text = school?.schoolName ?: "School Name", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Working Days: ${res.totalWorkingDays}", style = MaterialTheme.typography.bodySmall)
                            Text("MDM Days: ${res.totalMdmDays}", style = MaterialTheme.typography.bodySmall)
                            Text("Students Served: ${res.totalStudentsServed}", style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Compact Item-wise Table Header
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Item Name", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                        Text("Rate", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.width(65.dp))
                        Text("Qty", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.width(45.dp))
                        Text("Total", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.width(75.dp))
                    }
                }

                // Item Rows
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    contentPadding = PaddingValues(vertical = 4.dp)
                ) {
                    items(res.itemResults) { item ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(item.englishName, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                    Text(item.kannadaName, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                                }
                                Text(Money(item.ratePaise).formatInRupees(), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(65.dp))
                                Text("${item.applicableQuantity}", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.width(45.dp))
                                Text(Money(item.amountPaise).formatInRupees(), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold, modifier = Modifier.width(75.dp))
                            }
                        }
                    }
                }

                // Prominent Grand Total Card
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(stringResource(id = R.string.grand_total_label), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        Text(Money(res.grandTotalPaise).formatInRupees(), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // Export Actions ("Excel" terminology)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val file = viewModel.exportPdf(context)
                                    if (file != null) {
                                        Toast.makeText(context, "PDF generated successfully", Toast.LENGTH_SHORT).show()
                                        FileUtils.openOrShareFile(context, file, "application/pdf")
                                    } else {
                                        Toast.makeText(context, "Cannot export: Calculation must be successful.", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "PDF export error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        enabled = res.status == CalculationStatus.SUCCESS
                    ) {
                        Text("Export PDF")
                    }
                    Button(
                        onClick = {
                            scope.launch {
                                try {
                                    val file = viewModel.exportCsv(context)
                                    if (file != null) {
                                        Toast.makeText(context, "Excel report generated successfully", Toast.LENGTH_SHORT).show()
                                        FileUtils.openOrShareFile(context, file, "text/csv")
                                    } else {
                                        Toast.makeText(context, "Cannot export: Calculation must be successful.", Toast.LENGTH_SHORT).show()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Excel export error: ${e.localizedMessage}", Toast.LENGTH_LONG).show()
                                }
                            }
                        },
                        modifier = Modifier.weight(1f).height(48.dp),
                        enabled = res.status == CalculationStatus.SUCCESS
                    ) {
                        Text(stringResource(id = R.string.export_excel))
                    }
                }
            }
        }
    }
}
