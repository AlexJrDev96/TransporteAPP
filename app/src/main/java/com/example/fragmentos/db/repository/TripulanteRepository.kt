package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.TripulanteDao
import com.example.fragmentos.db.entity.Tripulante
import kotlinx.coroutines.flow.Flow

class TripulanteRepository(private val tripulanteDao: TripulanteDao) {

    val allTripulantes: Flow<List<Tripulante>> = tripulanteDao.getAll()

    suspend fun insert(tripulante: Tripulante) {
        tripulanteDao.insert(tripulante)
    }
}