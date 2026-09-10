package com.quietlog.app.ui.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.quietlog.app.settings.UserSettingsDataStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AppStartupViewModel @Inject constructor(
    dataStore: UserSettingsDataStore,
) : ViewModel() {

    /** null while the flag is still loading from DataStore. */
    val onboardingCompleted: StateFlow<Boolean?> = dataStore.onboardingCompleted
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null,
        )
}
