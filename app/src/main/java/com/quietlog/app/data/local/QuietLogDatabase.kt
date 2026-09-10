package com.quietlog.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.quietlog.app.data.local.dao.AttackDao
import com.quietlog.app.data.local.dao.MedicationDao
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.local.entity.AttackMedicationCrossRef
import com.quietlog.app.data.local.entity.MedicationEntity

@Database(
    entities = [
        AttackEntity::class,
        MedicationEntity::class,
        AttackMedicationCrossRef::class,
    ],
    version = 3,
    exportSchema = true,
)
@TypeConverters(Converters::class)
abstract class QuietLogDatabase : RoomDatabase() {
    abstract fun attackDao(): AttackDao
    abstract fun medicationDao(): MedicationDao
}
