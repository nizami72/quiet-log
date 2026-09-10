package com.quietlog.app.ui.screens.attackrecord

import com.quietlog.app.data.local.entity.MedicationEntity

data class AttackRecordUiState(
    val timestampStart: Long? = null,
    val intensity: Int? = null,
    val locationZones: Set<String> = emptySet(),
    val symptoms: Set<String> = emptySet(),
    val triggers: Set<String> = emptySet(),
    val note: String = "",
    val medications: List<MedicationEntity> = emptyList(),
    val selectedMedicationIds: Set<Long> = emptySet(),
)
