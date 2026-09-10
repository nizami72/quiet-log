package com.quietlog.app.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "attack_medication_cross_ref",
    primaryKeys = ["attackId", "medicationId"],
    foreignKeys = [
        ForeignKey(
            entity = AttackEntity::class,
            parentColumns = ["id"],
            childColumns = ["attackId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = MedicationEntity::class,
            parentColumns = ["id"],
            childColumns = ["medicationId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [Index("attackId"), Index("medicationId")],
)
data class AttackMedicationCrossRef(
    val attackId: Long,
    val medicationId: Long,
)
