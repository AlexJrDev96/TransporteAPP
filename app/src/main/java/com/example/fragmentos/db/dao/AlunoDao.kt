package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fragmentos.db.entity.Aluno
import kotlinx.coroutines.flow.Flow

@Dao
interface AlunoDao {

    @Insert
    suspend fun insert(aluno: Aluno)

    @Update
    suspend fun update(aluno: Aluno)

    @Delete
    suspend fun delete(aluno: Aluno)

    @Query("SELECT * FROM aluno ORDER BY nome ASC")
    fun getAll(): Flow<List<Aluno>>

    @Query("""
        SELECT a.* FROM aluno AS a
        INNER JOIN turma AS t ON a.turmaId = t.id
        WHERE t.periodo = :periodo
        ORDER BY a.nome ASC
    """)
    fun getAlunosByPeriodo(periodo: String): Flow<List<Aluno>>

    @Query("DELETE FROM aluno")
    suspend fun deleteAll()
}