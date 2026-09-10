package com.quietlog.app.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.repository.AttackRepository
import com.quietlog.app.ui.StatsPeriod
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    attackRepository: AttackRepository,
) : ViewModel() {

    val uiState: StateFlow<HomeUiState> = attackRepository.observeAttacks()
        .map { attacks ->
            val boundary = StatsPeriod.WEEK.startMillis()
            val weekAttacks = attacks.filter { boundary == null || it.timestampStart >= boundary }
            HomeUiState(
                weekAttackCount = weekAttacks.size,
                weekAvgIntensity = if (weekAttacks.isEmpty()) 0f else weekAttacks.map { it.intensity }.average().toFloat(),
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = HomeUiState(),
        )
}
