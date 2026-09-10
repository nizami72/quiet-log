package com.quietlog.app.data.repository

import com.quietlog.app.settings.UserSettings
import com.quietlog.app.settings.UserSettingsDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SettingsRepository {
    val settings: Flow<UserSettings>
    suspend fun setUnits(units: String)
    suspend fun setPremiumStatus(enabled: Boolean)
    suspend fun setCycleTrackingEnabled(enabled: Boolean)
    suspend fun setRemindersEnabled(enabled: Boolean)
}

class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: UserSettingsDataStore,
) : SettingsRepository {
    override val settings: Flow<UserSettings> = dataStore.settings

    override suspend fun setUnits(units: String) = dataStore.setUnits(units)
    override suspend fun setPremiumStatus(enabled: Boolean) = dataStore.setPremiumStatus(enabled)
    override suspend fun setCycleTrackingEnabled(enabled: Boolean) =
        dataStore.setCycleTrackingEnabled(enabled)
    override suspend fun setRemindersEnabled(enabled: Boolean) =
        dataStore.setRemindersEnabled(enabled)
}
