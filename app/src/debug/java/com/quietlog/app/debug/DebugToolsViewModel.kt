package com.quietlog.app.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val seeder: DebugDataSeeder,
    private val settingsRepository: SettingsRepository,
) : ViewModel() {

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

    val isPremium: StateFlow<Boolean> = settingsRepository.settings
        .map { it.premiumStatus }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun seedSampleData() {
        if (_isBusy.value) return
        _isBusy.value = true
        viewModelScope.launch {
            seeder.seedSampleData()
            _isBusy.value = false
        }
    }

    fun clearSeedData() {
        if (_isBusy.value) return
        _isBusy.value = true
        viewModelScope.launch {
            seeder.clearSeedData()
            _isBusy.value = false
        }
    }

    fun setPremiumForTesting(enabled: Boolean) {
        viewModelScope.launch { settingsRepository.setPremiumStatus(enabled) }
    }
}
