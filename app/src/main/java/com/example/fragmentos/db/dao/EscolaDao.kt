package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fragmentos.db.entity.Escola
import kotlinx.coroutines.flow.Flow

@Dao
interface EscolaDao {

    @Insert
    suspend fun insert(escola: Escola)

    @Update
    suspend fun update(escola: Escola)

    @Delete
    suspend fun delete(escola: Escola)

    @Query("SELECT * FROM escola ORDER BY nome ASC")
    fun getAll(): Flow<List<Escola>>

    @Query("DELETE FROM escola")
    suspend fun deleteAll()
}