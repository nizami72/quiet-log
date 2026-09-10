package com.quietlog.app.debug

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

/** Debug-build-only panel on Settings — see [DebugDataSeeder]. Not localized: developer-facing only. */
@Composable
fun DebugToolsSection(modifier: Modifier = Modifier, viewModel: DebugToolsViewModel = hiltViewModel()) {
    val isBusy by viewModel.isBusy.collectAsState()

    Card(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text("Debug tools", style = MaterialTheme.typography.titleMedium)
            Text(
                "Debug builds only. Fills the last ~2 months with sample attacks to preview PDF export and Insights.",
                style = MaterialTheme.typography.bodyMedium,
            )
            Button(onClick = viewModel::seedSampleData, enabled = !isBusy, modifier = Modifier.fillMaxWidth()) {
                Text(if (isBusy) "Working…" else "Seed 2 months of test data")
            }
            OutlinedButton(onClick = viewModel::clearSeedData, enabled = !isBusy, modifier = Modifier.fillMaxWidth()) {
                Text("Clear test data")
            }
        }
    }
}
