package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.EscolaDao
import com.example.fragmentos.db.entity.Escola
import kotlinx.coroutines.flow.Flow

class EscolaRepository(private val escolaDao: EscolaDao) {

    val allEscolas: Flow<List<Escola>> = escolaDao.getAll()

    suspend fun insert(escola: Escola) {
        escolaDao.insert(escola)
    }

    suspend fun update(escola: Escola) {
        escolaDao.update(escola)
    }

    suspend fun delete(escola: Escola) {
        escolaDao.delete(escola)
    }
}