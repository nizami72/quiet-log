package com.quietlog.app.ui.screens.insights

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.ui.StatsPeriod
import com.quietlog.app.ui.components.SimpleBarChart

private data class PremiumTeaser(val title: String, val description: String)

private val PREMIUM_TEASERS = listOf(
    PremiumTeaser(
        "Расширенный анализ триггеров",
        "Автоматический подсчёт корреляции по каждому триггеру, а не просто список",
    ),
    PremiumTeaser(
        "Эффективность медикаментов",
        "Сравнение препаратов по среднему снижению интенсивности боли",
    ),
    PremiumTeaser(
        "Корреляция с циклом",
        "Если включён модуль отслеживания гормонального цикла",
    ),
    PremiumTeaser(
        "Расширенные тренды",
        "День недели, время суток, сезонность",
    ),
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    onNavigateToPremium: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: InsightsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("Статистика / Инсайты") }) },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
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

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    title = "Всего приступов",
                    value = uiState.totalCount.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = "Средняя интенсивность",
                    value = "%.1f/10".format(uiState.avgIntensity),
                    modifier = Modifier.weight(1f),
                )
            }

            TagFrequencyList(title = "Частые симптомы", tags = uiState.topSymptoms)
            TagFrequencyList(title = "Частые триггеры", tags = uiState.topTriggers)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Давление ↔ частота приступов", style = MaterialTheme.typography.titleMedium)
                if (uiState.pressureBuckets.isEmpty()) {
                    Text(
                        "Пока недостаточно записей с показанием барометра — данные появятся " +
                            "по мере логирования приступов на устройстве с датчиком давления.",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    SimpleBarChart(points = uiState.pressureBuckets)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("QuietLog Premium", style = MaterialTheme.typography.titleMedium)
                PREMIUM_TEASERS.forEach { teaser ->
                    PremiumTeaserCard(teaser = teaser, onClick = onNavigateToPremium)
                }
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = value, style = MaterialTheme.typography.headlineLarge)
            Text(text = title, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
private fun TagFrequencyList(title: String, tags: List<Pair<String, Int>>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (tags.isEmpty()) {
            Text("Нет данных за выбранный период", style = MaterialTheme.typography.bodyLarge)
        } else {
            tags.forEach { (tag, count) ->
                Text("$tag — $count", style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}

@Composable
private fun PremiumTeaserCard(teaser: PremiumTeaser, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(Icons.Filled.Lock, contentDescription = null)
            Column {
                Text(teaser.title, style = MaterialTheme.typography.titleMedium)
                Text(teaser.description, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
