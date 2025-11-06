package com.example.fragmentos.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.fragmentos.db.entity.Tripulante
import kotlinx.coroutines.flow.Flow

@Dao
interface TripulanteDao {

    @Insert
    suspend fun insert(tripulante: Tripulante)

    @Update
    suspend fun update(tripulante: Tripulante)

    @Delete
    suspend fun delete(tripulante: Tripulante)

    @Query("SELECT * FROM tripulante ORDER BY nome ASC")
    fun getAll(): Flow<List<Tripulante>>

    @Query("DELETE FROM tripulante")
    suspend fun deleteAll()
}