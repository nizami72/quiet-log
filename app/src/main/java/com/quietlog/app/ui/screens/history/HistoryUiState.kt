package com.quietlog.app.ui.screens.history

import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.ui.StatsBucket
import com.quietlog.app.ui.StatsPeriod

data class HistoryUiState(
    val period: StatsPeriod = StatsPeriod.MONTH,
    val attacks: List<AttackEntity> = emptyList(),
    val chartPoints: List<StatsBucket> = emptyList(),
)
