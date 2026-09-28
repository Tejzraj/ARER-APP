package com.arer.app.ui.entry

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyEntryScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daily MDM Entry") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Daily student count entry & holiday validation placeholder.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Back to Dashboard")
            }
        }
    }
}
