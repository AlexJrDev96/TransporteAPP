package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.fragmentos.db.entity.Escola
import kotlinx.coroutines.flow.Flow

@Dao
interface EscolaDao {

    @Insert
    suspend fun insert(escola: Escola)

    @Query("SELECT * FROM escola ORDER BY nome ASC")
    fun getAll(): Flow<List<Escola>>

    @Query("DELETE FROM escola")
    suspend fun deleteAll()
}