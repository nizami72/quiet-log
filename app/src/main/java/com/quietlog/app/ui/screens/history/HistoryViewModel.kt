package com.quietlog.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.ui.StatsPeriod
import com.quietlog.app.ui.buildStatsBuckets
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    attackRepository: AttackRepository,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)

    val uiState: StateFlow<HistoryUiState> = combine(
        attackRepository.observeAttacks(),
        selectedPeriod,
    ) { attacks, period ->
        val boundary = period.startMillis()
        val filtered = attacks
            .filter { boundary == null || it.timestampStart >= boundary }
            .sortedByDescending { it.timestampStart }

        HistoryUiState(
            period = period,
            attacks = filtered,
            chartPoints = buildStatsBuckets(filtered, byMonth = period.bucketsByMonth),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )

    fun selectPeriod(period: StatsPeriod) {
        selectedPeriod.value = period
    }
}
