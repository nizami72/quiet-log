package com.quietlog.app.ui.screens.attackrecord

data class AttackRecordUiState(
    val timestampStart: Long? = null,
    val intensity: Int? = null,
    val locationZones: Set<String> = emptySet(),
    val symptoms: Set<String> = emptySet(),
    val triggers: Set<String> = emptySet(),
    val note: String = "",
)
