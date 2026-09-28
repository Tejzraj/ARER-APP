package com.arer.app.ui.reports

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Monthly Report & Bill") })
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Monthly item-wise calculation & PDF/CSV export placeholder.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onBack) {
                Text("Back to Dashboard")
            }
        }
    }
}
