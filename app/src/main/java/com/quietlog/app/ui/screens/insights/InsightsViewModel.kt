package com.quietlog.app.ui.screens.insights

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.repository.MedicationRepository
import com.quietlog.app.data.repository.SettingsRepository
import com.quietlog.app.ui.StatsBucket
import com.quietlog.app.ui.StatsPeriod
import com.quietlog.app.ui.buildDayPartTrends
import com.quietlog.app.ui.buildMedicationStats
import com.quietlog.app.ui.buildSeasonTrends
import com.quietlog.app.ui.buildTriggerCorrelations
import com.quietlog.app.ui.buildWeekdayTrends
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.math.floor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val PRESSURE_BUCKET_SIZE_HPA = 5

@HiltViewModel
class InsightsViewModel @Inject constructor(
    attackRepository: AttackRepository,
    medicationRepository: MedicationRepository,
    settingsRepository: SettingsRepository,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)

    val uiState: StateFlow<InsightsUiState> = combine(
        attackRepository.observeAttacks(),
        selectedPeriod,
        settingsRepository.settings,
        medicationRepository.observeMedications(),
        attackRepository.observeAllCrossRefs(),
    ) { attacks, period, settings, medications, crossRefs ->
        val boundary = period.startMillis()
        val filtered = attacks.filter { boundary == null || it.timestampStart >= boundary }

        InsightsUiState(
            period = period,
            totalCount = filtered.size,
            avgIntensity = if (filtered.isEmpty()) 0f else filtered.map { it.intensity }.average().toFloat(),
            topSymptoms = topTags(filtered) { it.symptoms },
            topTriggers = topTags(filtered) { it.triggers },
            pressureBuckets = buildPressureBuckets(filtered),
            isPremium = settings.premiumStatus,
            // Trends need more history than a single period filter to mean anything, so these
            // are computed over the full dataset rather than the period selected above.
            triggerCorrelations = buildTriggerCorrelations(attacks),
            medicationStats = buildMedicationStats(attacks, medications, crossRefs),
            weekdayTrends = buildWeekdayTrends(attacks),
            dayPartTrends = buildDayPartTrends(attacks),
            seasonTrends = buildSeasonTrends(attacks),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = InsightsUiState(),
    )

    fun selectPeriod(period: StatsPeriod) {
        selectedPeriod.value = period
    }

    private fun topTags(attacks: List<AttackEntity>, selector: (AttackEntity) -> List<String>): List<Pair<String, Int>> =
        attacks
            .flatMap(selector)
            .groupingBy { it }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .take(5)
            .map { it.key to it.value }

    private fun buildPressureBuckets(attacks: List<AttackEntity>): List<StatsBucket> =
        attacks
            .mapNotNull { attack -> attack.pressureHpa?.let { attack to it } }
            .groupBy { (_, pressure) ->
                (floor(pressure / PRESSURE_BUCKET_SIZE_HPA) * PRESSURE_BUCKET_SIZE_HPA).toInt()
            }
            .entries
            .sortedBy { it.key }
            .map { (bucketStart, bucketAttacks) ->
                StatsBucket(
                    label = "$bucketStart–${bucketStart + PRESSURE_BUCKET_SIZE_HPA}",
                    count = bucketAttacks.size,
                    avgIntensity = bucketAttacks.map { it.first.intensity }.average().toFloat(),
                )
            }
}
