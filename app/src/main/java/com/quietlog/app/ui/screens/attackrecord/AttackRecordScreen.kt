package com.quietlog.app.ui.screens.attackrecord

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R
import com.quietlog.app.ui.LocationZone
import com.quietlog.app.ui.Symptom
import com.quietlog.app.ui.Trigger
import com.quietlog.app.ui.components.IntensityPicker
import com.quietlog.app.ui.components.MedicationSelector
import com.quietlog.app.ui.components.SelectableChipGroup
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttackRecordScreen(
    onSaved: () -> Unit,
    onDeleted: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AttackRecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val title = uiState.timestampStart?.let {
        Instant.ofEpochMilli(it).atZone(ZoneId.systemDefault()).format(TIMESTAMP_FORMATTER)
    } ?: stringResource(R.string.attack_record_title_fallback)

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(title) },
                actions = {
                    IconButton(onClick = { showDeleteConfirm = true }) {
                        Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.attack_record_delete_description))
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.label_intensity), style = MaterialTheme.typography.titleMedium)
                IntensityPicker(
                    selected = uiState.intensity,
                    onSelect = viewModel::selectIntensity,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
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

            Button(
                onClick = { viewModel.save(onSaved) },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(stringResource(R.string.action_save))
            }
        }
    }

    if (showDeleteConfirm) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirm = false },
            title = { Text(stringResource(R.string.dialog_delete_attack_title)) },
            text = { Text(stringResource(R.string.dialog_action_irreversible)) },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirm = false
                    viewModel.delete(onDeleted)
                }) {
                    Text(stringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirm = false }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}
