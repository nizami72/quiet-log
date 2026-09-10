package com.quietlog.app.ui.screens.medications

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R
import com.quietlog.app.data.local.entity.MedicationEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationsScreen(
    modifier: Modifier = Modifier,
    viewModel: MedicationsViewModel = hiltViewModel(),
) {
    val medications by viewModel.medications.collectAsState()
    var editingMedication by remember { mutableStateOf<MedicationEntity?>(null) }
    var isAddingNew by remember { mutableStateOf(false) }
    var deletingMedication by remember { mutableStateOf<MedicationEntity?>(null) }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text(stringResource(R.string.label_medications)) }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { isAddingNew = true }) {
                Icon(Icons.Filled.Add, contentDescription = stringResource(R.string.action_add_medication))
            }
        },
    ) { padding ->
        if (medications.isEmpty()) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(stringResource(R.string.medications_empty))
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(medications, key = MedicationEntity::id) { medication ->
                    MedicationRow(
                        medication = medication,
                        onClick = { editingMedication = medication },
                        onDeleteClick = { deletingMedication = medication },
                    )
                }
            }
        }
    }

    if (isAddingNew) {
        MedicationEditDialog(
            initial = null,
            onDismiss = { isAddingNew = false },
            onSave = { name, dosage ->
                viewModel.save(MedicationEntity(name = name, dosage = dosage, createdAt = System.currentTimeMillis()))
                isAddingNew = false
            },
        )
    }

    editingMedication?.let { medication ->
        MedicationEditDialog(
            initial = medication,
            onDismiss = { editingMedication = null },
            onSave = { name, dosage ->
                viewModel.save(medication.copy(name = name, dosage = dosage))
                editingMedication = null
            },
        )
    }

    deletingMedication?.let { medication ->
        AlertDialog(
            onDismissRequest = { deletingMedication = null },
            title = { Text(stringResource(R.string.dialog_delete_medication_title)) },
            text = { Text(stringResource(R.string.dialog_action_irreversible)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.delete(medication)
                    deletingMedication = null
                }) {
                    Text(stringResource(R.string.action_delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { deletingMedication = null }) {
                    Text(stringResource(R.string.action_cancel))
                }
            },
        )
    }
}

@Composable
private fun MedicationRow(
    medication: MedicationEntity,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column {
                Text(medication.name, style = MaterialTheme.typography.titleMedium)
                Text(medication.dosage, style = MaterialTheme.typography.bodyLarge)
            }
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Filled.Delete, contentDescription = stringResource(R.string.action_delete))
            }
        }
    }
}

@Composable
private fun MedicationEditDialog(
    initial: MedicationEntity?,
    onDismiss: () -> Unit,
    onSave: (name: String, dosage: String) -> Unit,
) {
    var name by remember { mutableStateOf(initial?.name.orEmpty()) }
    var dosage by remember { mutableStateOf(initial?.dosage.orEmpty()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                stringResource(
                    if (initial == null) R.string.dialog_new_medication_title else R.string.dialog_edit_medication_title,
                ),
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text(stringResource(R.string.label_medication_name)) },
                    modifier = Modifier.fillMaxWidth(),
                )
                OutlinedTextField(
                    value = dosage,
                    onValueChange = { dosage = it },
                    label = { Text(stringResource(R.string.label_medication_dosage)) },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                enabled = name.isNotBlank(),
                onClick = { onSave(name.trim(), dosage.trim()) },
            ) {
                Text(stringResource(R.string.action_save))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.action_cancel))
            }
        },
    )
}
