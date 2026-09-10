package com.quietlog.app.ui.screens.medications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.local.entity.MedicationEntity
import com.quietlog.app.data.repository.MedicationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicationsViewModel @Inject constructor(
    private val medicationRepository: MedicationRepository,
) : ViewModel() {

    val medications: StateFlow<List<MedicationEntity>> = medicationRepository.observeMedications()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList(),
        )

    fun save(medication: MedicationEntity) {
        viewModelScope.launch {
            medicationRepository.saveMedication(medication)
        }
    }

    fun delete(medication: MedicationEntity) {
        viewModelScope.launch {
            medicationRepository.deleteMedication(medication)
        }
    }
}
