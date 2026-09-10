package com.quietlog.app.ui.screens.pdfreport

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.pdf.PdfReportGenerator
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.repository.MedicationRepository
import com.quietlog.app.data.repository.SettingsRepository
import com.quietlog.app.ui.StatsPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class PdfReportViewModel @Inject constructor(
    private val attackRepository: AttackRepository,
    private val medicationRepository: MedicationRepository,
    private val settingsRepository: SettingsRepository,
    private val pdfReportGenerator: PdfReportGenerator,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    private val selectedPeriod = MutableStateFlow(StatsPeriod.MONTH)
    private val isGenerating = MutableStateFlow(false)
    private val pendingShareUri = MutableStateFlow<Uri?>(null)

    val uiState: StateFlow<PdfReportUiState> = combine(
        settingsRepository.settings,
        selectedPeriod,
        isGenerating,
        pendingShareUri,
    ) { settings, period, generating, uri ->
        PdfReportUiState(
            isPremium = settings.premiumStatus,
            period = period,
            isGenerating = generating,
            pendingShareUri = uri,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PdfReportUiState(),
    )

    fun selectPeriod(period: StatsPeriod) {
        selectedPeriod.value = period
    }

    fun generateReport() {
        if (isGenerating.value) return
        viewModelScope.launch {
            isGenerating.value = true
            try {
                val period = selectedPeriod.value
                val boundary = period.startMillis()
                val attacks = attackRepository.observeAttacks().first()
                    .filter { boundary == null || it.timestampStart >= boundary }
                val medicationsById = medicationRepository.observeMedications().first().associateBy { it.id }
                val medicationNamesByAttackId = attacks.associate { attack ->
                    attack.id to attackRepository.getMedicationIdsForAttack(attack.id)
                        .mapNotNull { medicationsById[it] }
                        .map { "${it.name} ${it.dosage}".trim() }
                }
                val file = pdfReportGenerator.generate(attacks, medicationNamesByAttackId, period)
                pendingShareUri.value = FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.fileprovider",
                    file,
                )
            } finally {
                isGenerating.value = false
            }
        }
    }

    fun shareHandled() {
        pendingShareUri.value = null
    }
}
