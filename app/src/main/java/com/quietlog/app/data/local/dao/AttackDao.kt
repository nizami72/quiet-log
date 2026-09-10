package com.quietlog.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.quietlog.app.data.local.entity.AttackEntity
import com.quietlog.app.data.local.entity.AttackMedicationCrossRef
import kotlinx.coroutines.flow.Flow

@Dao
interface AttackDao {

    @Insert
    suspend fun insert(attack: AttackEntity): Long

    @Update
    suspend fun update(attack: AttackEntity)

    @Delete
    suspend fun delete(attack: AttackEntity)

    @Query("SELECT * FROM attacks ORDER BY timestampStart DESC")
    fun observeAll(): Flow<List<AttackEntity>>

    @Query("SELECT * FROM attacks WHERE id = :id")
    suspend fun getById(id: Long): AttackEntity?

    @Query("SELECT medicationId FROM attack_medication_cross_ref WHERE attackId = :attackId")
    suspend fun getMedicationIdsForAttack(attackId: Long): List<Long>

    @Query("DELETE FROM attack_medication_cross_ref WHERE attackId = :attackId")
    suspend fun clearMedicationCrossRefs(attackId: Long)

    @Insert
    suspend fun insertMedicationCrossRefs(refs: List<AttackMedicationCrossRef>)

    @Transaction
    suspend fun setMedicationsForAttack(attackId: Long, medicationIds: List<Long>) {
        clearMedicationCrossRefs(attackId)
        if (medicationIds.isNotEmpty()) {
            insertMedicationCrossRefs(medicationIds.map { AttackMedicationCrossRef(attackId, it) })
        }
    }
}
