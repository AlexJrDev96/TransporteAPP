package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fragmentos.db.entity.Turma
import kotlinx.coroutines.flow.Flow

@Dao
interface TurmaDao {

    @Insert
    suspend fun insert(turma: Turma)

    @Update
    suspend fun update(turma: Turma)

    @Delete
    suspend fun delete(turma: Turma)

    @Query("SELECT * FROM turma ORDER BY nome ASC")
    fun getAll(): Flow<List<Turma>>

    @Query("DELETE FROM turma")
    suspend fun deleteAll()
}