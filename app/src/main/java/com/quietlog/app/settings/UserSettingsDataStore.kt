package com.quietlog.app.settings

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

private val Context.userSettingsDataStore by preferencesDataStore(name = "user_settings")

class UserSettingsDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private object Keys {
        val UNITS = stringPreferencesKey("units")
        val PREMIUM_STATUS = booleanPreferencesKey("premium_status")
        val CYCLE_TRACKING_ENABLED = booleanPreferencesKey("cycle_tracking_enabled")
        val REMINDERS_ENABLED = booleanPreferencesKey("reminders_enabled")
    }

    val settings: Flow<UserSettings> = context.userSettingsDataStore.data.map { prefs ->
        UserSettings(
            units = prefs[Keys.UNITS] ?: "metric",
            premiumStatus = prefs[Keys.PREMIUM_STATUS] ?: false,
            cycleTrackingEnabled = prefs[Keys.CYCLE_TRACKING_ENABLED] ?: false,
            remindersEnabled = prefs[Keys.REMINDERS_ENABLED] ?: false,
        )
    }

    suspend fun setUnits(units: String) {
        context.userSettingsDataStore.edit { it[Keys.UNITS] = units }
    }

    suspend fun setPremiumStatus(enabled: Boolean) {
        context.userSettingsDataStore.edit { it[Keys.PREMIUM_STATUS] = enabled }
    }

    suspend fun setCycleTrackingEnabled(enabled: Boolean) {
        context.userSettingsDataStore.edit { it[Keys.CYCLE_TRACKING_ENABLED] = enabled }
    }

    suspend fun setRemindersEnabled(enabled: Boolean) {
        context.userSettingsDataStore.edit { it[Keys.REMINDERS_ENABLED] = enabled }
    }
}
