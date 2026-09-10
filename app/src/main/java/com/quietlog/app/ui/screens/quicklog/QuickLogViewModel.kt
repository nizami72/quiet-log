package com.quietlog.app.ui.screens.quicklog

import androidx.lifecycle.ViewModel
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.repository.AttackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuickLogViewModel @Inject constructor(
    private val attackRepository: AttackRepository,
) : ViewModel() {

    suspend fun logAttack(intensity: Int): Long {
        val attack = AttackEntity(
            timestampStart = System.currentTimeMillis(),
            intensity = intensity,
        )
        return attackRepository.saveAttack(attack)
    }
}
