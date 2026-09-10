package com.quietlog.app.settings

data class UserSettings(
    val units: String = "metric",
    val premiumStatus: Boolean = false,
    val cycleTrackingEnabled: Boolean = false,
    val remindersEnabled: Boolean = false,
)
