package com.quietlog.app.ui.screens.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.repository.AttackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private val ZONE = ZoneId.systemDefault()
private val RU = Locale("ru")
private val WEEK_LABEL_FORMATTER = DateTimeFormatter.ofPattern("dd.MM", RU)
private val MONTH_LABEL_FORMATTER = DateTimeFormatter.ofPattern("LLL yyyy", RU)

@HiltViewModel
class HistoryViewModel @Inject constructor(
    attackRepository: AttackRepository,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(HistoryPeriod.MONTH)

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
            chartPoints = buildChartPoints(filtered, byMonth = period.bucketsByMonth),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = HistoryUiState(),
    )

    fun selectPeriod(period: HistoryPeriod) {
        selectedPeriod.value = period
    }

    private fun buildChartPoints(attacks: List<AttackEntity>, byMonth: Boolean): List<HistoryChartPoint> {
        return attacks
            .groupBy { bucketStart(it.timestampStart, byMonth) }
            .entries
            .sortedBy { it.key.toEpochDay() }
            .map { (bucketStart, bucketAttacks) ->
                HistoryChartPoint(
                    label = bucketStart.format(if (byMonth) MONTH_LABEL_FORMATTER else WEEK_LABEL_FORMATTER),
                    count = bucketAttacks.size,
                    avgIntensity = bucketAttacks.map { it.intensity }.average().toFloat(),
                )
            }
    }

    private fun bucketStart(timestampMillis: Long, byMonth: Boolean): LocalDate {
        val date = Instant.ofEpochMilli(timestampMillis).atZone(ZONE).toLocalDate()
        return if (byMonth) {
            date.withDayOfMonth(1)
        } else {
            date.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        }
    }
}
