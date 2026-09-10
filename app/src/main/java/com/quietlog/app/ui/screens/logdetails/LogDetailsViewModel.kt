package com.quietlog.app.ui.screens.logdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.repository.MedicationRepository
import com.quietlog.app.ui.navigation.Destination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LogDetailsViewModel @Inject constructor(
    private val attackRepository: AttackRepository,
    private val medicationRepository: MedicationRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val attackId: Long = checkNotNull(savedStateHandle[Destination.LogDetails.ARG_ATTACK_ID])

    private val _uiState = MutableStateFlow(LogDetailsUiState())
    val uiState: StateFlow<LogDetailsUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            attackRepository.getAttack(attackId)?.let { attack ->
                _uiState.update {
                    it.copy(
                        intensity = attack.intensity,
                        locationZones = attack.locationZones.toSet(),
                        symptoms = attack.symptoms.toSet(),
                        triggers = attack.triggers.toSet(),
                        note = attack.note.orEmpty(),
                    )
                }
            }
            val selectedMedicationIds = attackRepository.getMedicationIdsForAttack(attackId).toSet()
            _uiState.update { it.copy(selectedMedicationIds = selectedMedicationIds) }
        }
        viewModelScope.launch {
            medicationRepository.observeMedications().collect { medications ->
                _uiState.update { it.copy(medications = medications) }
            }
        }
    }

    fun toggleLocationZone(zone: String) {
        _uiState.update { it.copy(locationZones = it.locationZones.toggled(zone)) }
    }

    fun toggleSymptom(symptom: String) {
        _uiState.update { it.copy(symptoms = it.symptoms.toggled(symptom)) }
    }

    fun toggleTrigger(trigger: String) {
        _uiState.update { it.copy(triggers = it.triggers.toggled(trigger)) }
    }

    fun toggleMedication(medicationId: Long) {
        _uiState.update { it.copy(selectedMedicationIds = it.selectedMedicationIds.toggled(medicationId)) }
    }

    fun updateNote(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun save(onSaved: () -> Unit) {
        viewModelScope.launch {
            val attack = attackRepository.getAttack(attackId) ?: return@launch
            val state = _uiState.value
            attackRepository.saveAttack(
                attack.copy(
                    locationZones = state.locationZones.toList(),
                    symptoms = state.symptoms.toList(),
                    triggers = state.triggers.toList(),
                    note = state.note.ifBlank { null },
                ),
            )
            attackRepository.setMedicationsForAttack(attackId, state.selectedMedicationIds.toList())
            onSaved()
        }
    }

    private fun <T> Set<T>.toggled(value: T): Set<T> =
        if (value in this) this - value else this + value
}
