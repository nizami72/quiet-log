package com.quietlog.app.ui.screens.logdetails

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.ui.AttackFieldOptions
import com.quietlog.app.ui.components.SelectableChipGroup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogDetailsScreen(
    onDone: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LogDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Добавить детали") }) },
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
                    text = "Интенсивность: $intensity/10",
                    style = MaterialTheme.typography.titleLarge,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Локализация боли", style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = AttackFieldOptions.LOCATION_ZONE_OPTIONS,
                    selected = uiState.locationZones,
                    onToggle = viewModel::toggleLocationZone,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Симптомы", style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = AttackFieldOptions.SYMPTOM_OPTIONS,
                    selected = uiState.symptoms,
                    onToggle = viewModel::toggleSymptom,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Возможные триггеры", style = MaterialTheme.typography.titleMedium)
                SelectableChipGroup(
                    options = AttackFieldOptions.TRIGGER_OPTIONS,
                    selected = uiState.triggers,
                    onToggle = viewModel::toggleTrigger,
                )
            }

            OutlinedTextField(
                value = uiState.note,
                onValueChange = viewModel::updateNote,
                label = { Text("Заметка") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(
                    onClick = { viewModel.save(onDone) },
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Готово")
                }
                OutlinedButton(
                    onClick = onDone,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text("Пропустить")
                }
            }
        }
    }
}
