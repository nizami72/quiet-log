package com.quietlog.app.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attacks")
data class AttackEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampStart: Long,
    val timestampEnd: Long? = null,
    val intensity: Int,
    val locationZones: List<String> = emptyList(),
    val symptoms: List<String> = emptyList(),
    val triggers: List<String> = emptyList(),
    val note: String? = null,
)
