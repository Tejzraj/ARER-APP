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
fun SchoolSetupScreen(
    onNext: () -> Unit,
    viewModel: SchoolSetupViewModel = hiltViewModel()
) {
    var schoolName by remember { mutableStateOf("") }
    var schoolCode by remember { mutableStateOf("") }
    var udiseCode by remember { mutableStateOf("") }
    var kgidNumber by remember { mutableStateOf("") }
    var district by remember { mutableStateOf("") }
    var taluk by remember { mutableStateOf("") }
    var cluster by remember { mutableStateOf("") }
    var villageTown by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var pinCode by remember { mutableStateOf("") }

    val errorMessage by viewModel.errorMessage.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.school_setup_title)) })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = schoolName,
                onValueChange = { schoolName = it },
                label = { Text("${stringResource(id = R.string.school_name)} *") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = schoolCode,
                onValueChange = { schoolCode = it },
                label = { Text(stringResource(id = R.string.school_code)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = udiseCode,
                onValueChange = { udiseCode = it },
                label = { Text(stringResource(id = R.string.udise_code)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = kgidNumber,
                onValueChange = { kgidNumber = it },
                label = { Text(stringResource(id = R.string.kgid_number)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = district,
                onValueChange = { district = it },
                label = { Text(stringResource(id = R.string.district)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = taluk,
                onValueChange = { taluk = it },
                label = { Text(stringResource(id = R.string.taluk)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = cluster,
                onValueChange = { cluster = it },
                label = { Text(stringResource(id = R.string.cluster)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = villageTown,
                onValueChange = { villageTown = it },
                label = { Text(stringResource(id = R.string.village_town)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text(stringResource(id = R.string.address)) },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = pinCode,
                onValueChange = { if (it.length <= 6) pinCode = it },
                label = { Text(stringResource(id = R.string.pin_code)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorMessage != null) {
                Text(text = errorMessage ?: "", color = MaterialTheme.colorScheme.error)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.saveSchool(
                        schoolName, schoolCode, udiseCode, kgidNumber,
                        district, taluk, cluster, villageTown, address, pinCode,
                        onNext
                    )
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
