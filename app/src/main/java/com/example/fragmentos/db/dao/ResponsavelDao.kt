package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fragmentos.db.entity.Responsavel
import kotlinx.coroutines.flow.Flow

@Dao
interface ResponsavelDao {

    @Insert
    suspend fun insert(responsavel: Responsavel)

    @Update
    suspend fun update(responsavel: Responsavel)

    @Delete
    suspend fun delete(responsavel: Responsavel)

    @Query("SELECT * FROM responsavel ORDER BY nome ASC")
    fun getAll(): Flow<List<Responsavel>>

    @Query("DELETE FROM responsavel")
    suspend fun deleteAll()
}