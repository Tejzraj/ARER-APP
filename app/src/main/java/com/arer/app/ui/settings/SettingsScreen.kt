package com.arer.app.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arer.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onBack: () -> Unit,
    onNavigateToRateManagement: () -> Unit,
    onLoggedOut: () -> Unit,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val school by viewModel.schoolProfile.collectAsState()
    val hm by viewModel.hmProfile.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.settings)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "School Profile", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Name: ${school?.schoolName ?: "N/A"}")
                    Text(text = "UDISE: ${school?.udiseCode ?: "N/A"}")
                    Text(text = "District: ${school?.district ?: "N/A"}")
                }
            }

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = "Head Master (HM) Profile", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Name: ${hm?.hmName ?: "N/A"}")
                    Text(text = "Designation: ${hm?.designation ?: "N/A"}")
                    Text(text = "Mobile: ${hm?.mobileNumber ?: "N/A"}")
                }
            }

            Button(
                onClick = onNavigateToRateManagement,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(id = R.string.rate_management))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.logout(onLoggedOut) },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(id = R.string.logout))
            }
        }
    }
}
