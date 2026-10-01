package com.arer.app.ui.entry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arer.app.R
import com.arer.app.domain.model.DailyEntryStatus
import com.arer.app.domain.usecase.DayUiModel
import com.arer.app.ui.rates.MonthlyRateConfirmationDialog
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyEntryScreen(
    onBack: () -> Unit,
    viewModel: MonthlyEntryViewModel = hiltViewModel()
) {
    val displayTitle by viewModel.displayMonthTitle.collectAsState()
    val yearMonth by viewModel.yearMonthString.collectAsState()
    val days by viewModel.days.collectAsState()
    val summary by viewModel.summary.collectAsState()
    val isRateConfirmed by viewModel.isRateConfirmed.collectAsState()
    val monthlyRates by viewModel.monthlyRates.collectAsState()

    var showOverrideDialog by remember { mutableStateOf(false) }
    var selectedDayForOverride by remember { mutableStateOf<DayUiModel?>(null) }
    var overrideReasonInput by remember { mutableStateOf("") }

    if (!isRateConfirmed) {
        MonthlyRateConfirmationDialog(
            yearMonth = yearMonth,
            rates = monthlyRates,
            onConfirm = { viewModel.confirmRates() }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.daily_mdm_entry)) },
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
                .imePadding()
                .navigationBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Month Selector Bar
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
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

            // Monthly Summary Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Working Days", style = MaterialTheme.typography.bodySmall)
                            Text("${summary.workingDays}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text("MDM Days", style = MaterialTheme.typography.bodySmall)
                            Text("${summary.mdmDays}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                        Column {
                            Text(stringResource(id = R.string.students_served), style = MaterialTheme.typography.bodySmall)
                            Text("${summary.studentsServed}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Completion Progress", style = MaterialTheme.typography.bodyMedium)
                        Text(
                            text = "${summary.completedEntriesCount} / ${summary.totalMdmDaysCount} completed",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Daily List with keyboard-aware padding
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(days) { day ->
                    DayRowItem(
                        day = day,
                        onCountChange = { countStr ->
                            viewModel.updateStudentCount(day.dateMillis, countStr)
                        },
                        onOverrideClick = {
                            selectedDayForOverride = day
                            overrideReasonInput = ""
                            showOverrideDialog = true
                        }
                    )
                }
            }
        }
    }

    if (showOverrideDialog && selectedDayForOverride != null) {
        AlertDialog(
            onDismissRequest = { showOverrideDialog = false },
            title = { Text(stringResource(id = R.string.override_dialog_title)) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(id = R.string.override_dialog_message))
                    OutlinedTextField(
                        value = overrideReasonInput,
                        onValueChange = { overrideReasonInput = it },
                        label = { Text(stringResource(id = R.string.reason)) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        selectedDayForOverride?.let {
                            viewModel.overrideDay(it.dateMillis, overrideReasonInput)
                        }
                        showOverrideDialog = false
                    }
                ) {
                    Text("Confirm Override")
                }
            },
            dismissButton = {
                TextButton(onClick = { showOverrideDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun DayRowItem(
    day: DayUiModel,
    onCountChange: (String) -> Unit,
    onOverrideClick: () -> Unit
) {
    val isEditable = when (day.status) {
        DailyEntryStatus.NORMAL -> true
        DailyEntryStatus.OVERRIDDEN -> true
        DailyEntryStatus.HOLIDAY, DailyEntryStatus.GOVERNMENT_HOLIDAY -> day.isOverridden
    }

    var textValue by remember(day.dateMillis, day.studentCount) {
        mutableStateOf(day.studentCount?.toString() ?: "")
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (day.status == DailyEntryStatus.HOLIDAY || day.status == DailyEntryStatus.GOVERNMENT_HOLIDAY) {
                if (day.isOverridden) MaterialTheme.colorScheme.tertiaryContainer else MaterialTheme.colorScheme.surfaceVariant
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Date & Status info
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = String.format(Locale.US, "%02d", day.dayOfMonth),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = day.dayOfWeek,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                val statusLabel = when (day.status) {
                    DailyEntryStatus.NORMAL -> stringResource(id = R.string.working_day)
                    DailyEntryStatus.HOLIDAY -> if (day.isOverridden) stringResource(id = R.string.exceptional_day) else stringResource(id = R.string.sunday)
                    DailyEntryStatus.GOVERNMENT_HOLIDAY -> if (day.isOverridden) stringResource(id = R.string.exceptional_day) else stringResource(id = R.string.government_holiday)
                    DailyEntryStatus.OVERRIDDEN -> stringResource(id = R.string.exceptional_day)
                }
                Text(text = statusLabel, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)

                if (!day.holidayReason.isNullOrBlank()) {
                    Text(text = day.holidayReason, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                }
            }

            // Student Count Input or Lock
            if (isEditable) {
                Column(horizontalAlignment = Alignment.End) {
                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { newVal ->
                            if (newVal.all { it.isDigit() } && newVal.length <= 4) {
                                textValue = newVal
                                onCountChange(newVal)
                            }
                        },
                        label = { Text("Count") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.width(110.dp),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    if (day.studentCount != null) {
                        Text(text = stringResource(id = R.string.saved), fontSize = 11.sp, color = MaterialTheme.colorScheme.primary)
                    } else {
                        Text(text = stringResource(id = R.string.missing_entry), fontSize = 10.sp, color = MaterialTheme.colorScheme.error)
                    }
                }
            } else {
                Column(horizontalAlignment = Alignment.End) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("0", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        IconButton(onClick = onOverrideClick) {
                            Icon(Icons.Default.Lock, contentDescription = "Locked / Override", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                    Text(text = "Tap lock to override", fontSize = 10.sp, color = MaterialTheme.colorScheme.secondary)
                }
            }
        }
    }
}
