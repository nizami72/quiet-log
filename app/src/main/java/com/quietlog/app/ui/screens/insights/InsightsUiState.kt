package com.quietlog.app.ui.screens.insights

import com.quietlog.app.ui.StatsBucket
import com.quietlog.app.ui.StatsPeriod

data class InsightsUiState(
    val period: StatsPeriod = StatsPeriod.MONTH,
    val totalCount: Int = 0,
    val avgIntensity: Float = 0f,
    val topSymptoms: List<Pair<String, Int>> = emptyList(),
    val topTriggers: List<Pair<String, Int>> = emptyList(),
    val pressureBuckets: List<StatsBucket> = emptyList(),
)
