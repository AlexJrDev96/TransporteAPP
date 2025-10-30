package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fragmentos.db.entity.Aluno
import kotlinx.coroutines.flow.Flow

@Dao
interface AlunoDao {

    @Insert
    suspend fun insert(aluno: Aluno)

    @Query("SELECT * FROM aluno ORDER BY nome ASC")
    fun getAll(): Flow<List<Aluno>>

    @Query("DELETE FROM aluno")
    suspend fun deleteAll()
}