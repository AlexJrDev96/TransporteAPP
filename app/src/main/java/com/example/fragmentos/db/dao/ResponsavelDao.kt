package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fragmentos.db.entity.Responsavel
import kotlinx.coroutines.flow.Flow

@Dao
interface ResponsavelDao {

    @Insert
    suspend fun insert(responsavel: Responsavel)

    @Query("SELECT * FROM responsavel ORDER BY nome ASC")
    fun getAll(): Flow<List<Responsavel>>

    @Query("DELETE FROM responsavel")
    suspend fun deleteAll()
}