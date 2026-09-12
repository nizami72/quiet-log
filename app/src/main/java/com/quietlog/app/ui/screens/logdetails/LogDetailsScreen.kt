package com.quietlog.app.ui.screens.logdetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R
import com.quietlog.app.ui.LocationZone
import com.quietlog.app.ui.Symptom
import com.quietlog.app.ui.Trigger
import com.quietlog.app.ui.components.MedicationSelector
import com.quietlog.app.ui.components.SelectableChipGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailsScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    // The system back gesture/button bypasses the Done/Skip button handlers below and used to
    // silently discard whatever was typed — save on the way out instead, same as tapping Done.
    BackHandler(onBack = { viewModel.save(onDone) })

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.log_details_title)) }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            uiState.intensity?.let { intensity ->
                Text(
                    text = stringResource(R.string.format_intensity_value, intensity),
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.label_location_zone), style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = LocationZone.entries.map { it.name to stringResource(it.labelRes) },
                    selected = uiState.locationZones,
                    onToggle = viewModel::toggleLocationZone,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.label_symptoms), style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = Symptom.entries.map { it.name to stringResource(it.labelRes) },
                    selected = uiState.symptoms,
                    onToggle = viewModel::toggleSymptom,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.label_triggers), style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = Trigger.entries.map { it.name to stringResource(it.labelRes) },
                    selected = uiState.triggers,
                    onToggle = viewModel::toggleTrigger,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.label_medications), style = MaterialTheme.typography.titleMedium)
                MedicationSelector(
                    medications = uiState.medications,
                    selectedIds = uiState.selectedMedicationIds,
                    onToggle = viewModel::toggleMedication,
                )
            }

            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::updateNote,
                label = { Text(stringResource(R.string.label_note)) },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.save(onDone) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.action_done))
                }
                OutlinedButton(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(stringResource(R.string.action_skip))
                }
            }
        }
    }
}
