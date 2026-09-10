package com.quietlog.app.ui.screens.quicklog

import androidx.lifecycle.ViewModel
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.data.sensor.PressureSensorReader
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickLogViewModel @Inject constructor(
    private val attackRepository: AttackRepository,
    private val pressureSensorReader: PressureSensorReader,
) : ViewModel() {

    suspend fun logAttack(intensity: Int): Long {
        val attack = AttackEntity(
            timestampStart = System.currentTimeMillis(),
            intensity = intensity,
            pressureHpa = pressureSensorReader.readPressureHpa(),
        )
        return attackRepository.saveAttack(attack)
    }
}
