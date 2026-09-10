package com.quietlog.app.data.repository

import com.quietlog.app.data.local.dao.AttackDao
import com.quietlog.app.data.local.entity.AttackEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface AttackRepository {
    fun observeAttacks(): Flow<List<AttackEntity>>
    suspend fun getAttack(id: Long): AttackEntity?
    suspend fun saveAttack(attack: AttackEntity): Long
    suspend fun deleteAttack(attack: AttackEntity)
}

class AttackRepositoryImpl @Inject constructor(
    private val dao: AttackDao,
) : AttackRepository {
    override fun observeAttacks(): Flow<List<AttackEntity>> = dao.observeAll()
    override suspend fun getAttack(id: Long): AttackEntity? = dao.getById(id)

    override suspend fun saveAttack(attack: AttackEntity): Long {
        return if (attack.id == 0L) {
            dao.insert(attack)
        } else {
            dao.update(attack)
            attack.id
        }
    }

    override suspend fun deleteAttack(attack: AttackEntity) = dao.delete(attack)
}
