package com.quietlog.app.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val seeder: DebugDataSeeder,
) : ViewModel() {

    private val _isBusy = MutableStateFlow(false)
    val isBusy: StateFlow<Boolean> = _isBusy.asStateFlow()

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
}
