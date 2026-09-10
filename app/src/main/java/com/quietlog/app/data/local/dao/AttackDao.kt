package com.quietlog.app.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.quietlog.app.data.local.entity.AttackEntity
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
}
