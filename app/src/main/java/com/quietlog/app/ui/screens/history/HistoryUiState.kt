package com.quietlog.app.ui.screens.history

import com.quietlog.app.data.local.entity.AttackEntity

data class HistoryUiState(
    val period: HistoryPeriod = HistoryPeriod.MONTH,
    val attacks: List<AttackEntity> = emptyList(),
    val chartPoints: List<HistoryChartPoint> = emptyList(),
)
