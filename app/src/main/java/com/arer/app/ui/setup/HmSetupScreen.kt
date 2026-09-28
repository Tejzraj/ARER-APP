package com.arer.app.ui.setup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.arer.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HmSetupScreen(
    onNext: () -> Unit,
    viewModel: HmSetupViewModel = hiltViewModel()
) {
    var hmName by remember { mutableStateOf("") }
    var designation by remember { mutableStateOf("Head Master") }
    var hmKgidNumber by remember { mutableStateOf("") }
    var mobileNumber by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.hm_setup_title)) })
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
            OutlinedTextField(
                value = hmName,
                onValueChange = { hmName = it },
                label = { Text("${stringResource(id = R.string.hm_name)} *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = designation,
                onValueChange = { designation = it },
                label = { Text(stringResource(id = R.string.designation)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = hmKgidNumber,
                onValueChange = { hmKgidNumber = it },
                label = { Text(stringResource(id = R.string.hm_kgid)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = mobileNumber,
                onValueChange = { if (it.length <= 10) mobileNumber = it },
                label = { Text(stringResource(id = R.string.mobile_number)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveHm(hmName, designation, hmKgidNumber, mobileNumber, onNext)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(stringResource(id = R.string.save_continue))
            }
        }
    }
}
