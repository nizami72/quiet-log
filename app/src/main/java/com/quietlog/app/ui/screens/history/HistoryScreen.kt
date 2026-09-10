package com.quietlog.app.ui.screens.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.ui.components.SimpleBarChart
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private val TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onOpenAttack: (attackId: Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HistoryViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("История приступов") }) },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
            ) {
                HistoryPeriod.entries.forEachIndexed { index, period ->
                    SegmentedButton(
                        selected = uiState.period == period,
                        onClick = { viewModel.selectPeriod(period) },
                        shape = SegmentedButtonDefaults.itemShape(index, HistoryPeriod.entries.size),
                    ) {
                        Text(period.label)
                    }
                }
            }

            if (uiState.chartPoints.isNotEmpty()) {
                SimpleBarChart(
                    points = uiState.chartPoints,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }

            if (uiState.attacks.isEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text("Записей за этот период нет")
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    items(uiState.attacks, key = AttackEntity::id) { attack ->
                        AttackRow(attack = attack, onClick = { onOpenAttack(attack.id) })
                    }
                }
            }
        }
    }
}

@Composable
private fun AttackRow(attack: AttackEntity, onClick: () -> Unit) {
    Card(modifier = Modifier
        .fillMaxWidth()
        .clickable(onClick = onClick)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Интенсивность: ${attack.intensity}/10",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = Instant.ofEpochMilli(attack.timestampStart)
                    .atZone(ZoneId.systemDefault())
                    .format(TIMESTAMP_FORMATTER),
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}
