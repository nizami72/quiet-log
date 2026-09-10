package com.quietlog.app.ui.screens.pdfreport

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.size
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.ui.StatsPeriod

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PdfReportScreen(
    onNavigateToPremium: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PdfReportViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(uiState.pendingShareUri) {
        val uri = uiState.pendingShareUri ?: return@LaunchedEffect
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(shareIntent, "Поделиться отчётом"))
        viewModel.shareHandled()
    }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("PDF-отчёт для врача") }) },
    ) { padding ->
        if (!uiState.isPremium) {
            Column(
                modifier = Modifier
                    .padding(padding)
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "PDF-отчёт для врача доступен в QuietLog Premium",
                    style = MaterialTheme.typography.titleMedium,
                )
                Button(onClick = onNavigateToPremium, modifier = Modifier.padding(top = 16.dp)) {
                    Text("Открыть Premium")
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Text(
                "Отчёт включает список приступов, интенсивность, симптомы, триггеры, " +
                    "медикаменты и график за выбранный период. Генерируется локально на устройстве.",
                style = MaterialTheme.typography.bodyLarge,
            )

            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                StatsPeriod.entries.forEachIndexed { index, period ->
                    SegmentedButton(
                        selected = uiState.period == period,
                        onClick = { viewModel.selectPeriod(period) },
                        shape = SegmentedButtonDefaults.itemShape(index, StatsPeriod.entries.size),
                    ) {
                        Text(period.label)
                    }
                }
            }

            Button(
                onClick = viewModel::generateReport,
                enabled = !uiState.isGenerating,
                modifier = Modifier.fillMaxWidth(),
            ) {
                if (uiState.isGenerating) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(16.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp,
                    )
                }
                Text(if (uiState.isGenerating) "Генерация…" else "Сгенерировать и поделиться")
            }
        }
    }
}
