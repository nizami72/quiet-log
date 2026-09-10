package com.quietlog.app.data.repository

import com.quietlog.app.data.local.dao.MedicationDao
import com.quietlog.app.data.local.entity.MedicationEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface MedicationRepository {
    fun observeMedications(): Flow<List<MedicationEntity>>
    suspend fun saveMedication(medication: MedicationEntity): Long
    suspend fun deleteMedication(medication: MedicationEntity)
}

class MedicationRepositoryImpl @Inject constructor(
    private val dao: MedicationDao,
) : MedicationRepository {
    override fun observeMedications(): Flow<List<MedicationEntity>> = dao.observeAll()

    override suspend fun saveMedication(medication: MedicationEntity): Long {
        return if (medication.id == 0L) {
            dao.insert(medication)
        } else {
            dao.update(medication)
            medication.id
        }
    }

    override suspend fun deleteMedication(medication: MedicationEntity) = dao.delete(medication)
}
