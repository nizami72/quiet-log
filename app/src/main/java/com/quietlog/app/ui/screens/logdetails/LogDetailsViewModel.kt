package com.quietlog.app.ui.screens.logdetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.repository.AttackRepository
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
            onSaved()
        }
    }

    private fun Set<String>.toggled(value: String): Set<String> =
        if (value in this) this - value else this + value
}
