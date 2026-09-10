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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.quietlog.app.R
import com.quietlog.app.ui.PREMIUM_FEATURES
import com.quietlog.app.ui.PremiumFeature
import com.quietlog.app.ui.StatsBucket
import com.quietlog.app.ui.StatsPeriod
import com.quietlog.app.ui.Symptom
import com.quietlog.app.ui.Trigger
import com.quietlog.app.ui.components.SimpleBarChart

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
        topBar = { TopAppBar(title = { Text(stringResource(R.string.insights_title)) }) },
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
                        Text(stringResource(period.labelRes))
                    }
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard(
                    title = stringResource(R.string.insights_total_attacks),
                    value = uiState.totalCount.toString(),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    title = stringResource(R.string.insights_avg_intensity),
                    value = "%.1f/10".format(uiState.avgIntensity),
                    modifier = Modifier.weight(1f),
                )
            }

            TagFrequencyList(
                title = stringResource(R.string.insights_top_symptoms),
                tags = uiState.topSymptoms,
                labelFor = { key -> Symptom.fromKey(key)?.let { stringResource(it.labelRes) } ?: key },
            )
            TagFrequencyList(
                title = stringResource(R.string.insights_top_triggers),
                tags = uiState.topTriggers,
                labelFor = { key -> Trigger.fromKey(key)?.let { stringResource(it.labelRes) } ?: key },
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(R.string.insights_pressure_correlation_title), style = MaterialTheme.typography.titleMedium)
                if (uiState.pressureBuckets.isEmpty()) {
                    Text(
                        stringResource(R.string.insights_pressure_correlation_empty),
                        style = MaterialTheme.typography.bodyLarge,
                    )
                } else {
                    SimpleBarChart(points = uiState.pressureBuckets)
                }
            }

            if (uiState.isPremium) {
                PremiumAnalyticsSection(uiState)
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(R.string.premium_brand_title), style = MaterialTheme.typography.titleMedium)
                    PREMIUM_FEATURES.forEach { feature ->
                        PremiumTeaserCard(feature = feature, onClick = onNavigateToPremium)
                    }
                }
            }
        }
    }
}

@Composable
private fun PremiumAnalyticsSection(uiState: InsightsUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.insights_trigger_correlation_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.insights_trigger_correlation_subtitle), style = MaterialTheme.typography.bodyMedium)
        if (uiState.triggerCorrelations.isEmpty()) {
            Text(stringResource(R.string.insights_no_data), style = MaterialTheme.typography.bodyLarge)
        } else {
            uiState.triggerCorrelations.forEach { (key, percent) ->
                val label = Trigger.fromKey(key)?.let { stringResource(it.labelRes) } ?: key
                Text(stringResource(R.string.format_trigger_correlation, label, percent), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.premium_feature_medication_effectiveness_title), style = MaterialTheme.typography.titleMedium)
        Text(stringResource(R.string.insights_medication_effectiveness_subtitle), style = MaterialTheme.typography.bodyMedium)
        if (uiState.medicationStats.isEmpty()) {
            Text(stringResource(R.string.insights_no_data), style = MaterialTheme.typography.bodyLarge)
        } else {
            uiState.medicationStats.forEach { stat ->
                Text(
                    "${stat.name} — " + stringResource(R.string.format_medication_stat, "%.1f".format(stat.avgIntensity), stat.attackCount),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.insights_weekday_trend_title), style = MaterialTheme.typography.titleMedium)
        if (uiState.weekdayTrends.isEmpty()) {
            Text(stringResource(R.string.insights_no_data), style = MaterialTheme.typography.bodyLarge)
        } else {
            SimpleBarChart(points = uiState.weekdayTrends)
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.insights_daypart_trend_title), style = MaterialTheme.typography.titleMedium)
        if (uiState.dayPartTrends.isEmpty()) {
            Text(stringResource(R.string.insights_no_data), style = MaterialTheme.typography.bodyLarge)
        } else {
            SimpleBarChart(points = uiState.dayPartTrends.map { StatsBucket(stringResource(it.part.labelRes), it.count, it.avgIntensity) })
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(stringResource(R.string.insights_season_trend_title), style = MaterialTheme.typography.titleMedium)
        if (uiState.seasonTrends.isEmpty()) {
            Text(stringResource(R.string.insights_no_data), style = MaterialTheme.typography.bodyLarge)
        } else {
            SimpleBarChart(points = uiState.seasonTrends.map { StatsBucket(stringResource(it.season.labelRes), it.count, it.avgIntensity) })
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
private fun TagFrequencyList(
    title: String,
    tags: List<Pair<String, Int>>,
    labelFor: @Composable (String) -> String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(title, style = MaterialTheme.typography.titleMedium)
        if (tags.isEmpty()) {
            Text(stringResource(R.string.insights_no_data_for_period), style = MaterialTheme.typography.bodyLarge)
        } else {
            tags.forEach { (tag, count) ->
                Text(
                    stringResource(R.string.format_tag_count, labelFor(tag), count),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun PremiumTeaserCard(feature: PremiumFeature, onClick: () -> Unit) {
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
                Text(stringResource(feature.titleRes), style = MaterialTheme.typography.titleMedium)
                Text(stringResource(feature.descriptionRes), style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}
