package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fragmentos.db.entity.Turma
import kotlinx.coroutines.flow.Flow

@Dao
interface TurmaDao {

    @Insert
    suspend fun insert(turma: Turma)

    @Query("SELECT * FROM turma ORDER BY nome ASC")
    fun getAll(): Flow<List<Turma>>

    @Query("DELETE FROM turma")
    suspend fun deleteAll()
}