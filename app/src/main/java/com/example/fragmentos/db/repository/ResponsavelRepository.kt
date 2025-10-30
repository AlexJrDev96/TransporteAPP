package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.ResponsavelDao
import com.example.fragmentos.db.entity.Responsavel
import kotlinx.coroutines.flow.Flow

class ResponsavelRepository(private val responsavelDao: ResponsavelDao) {

    val allResponsaveis: Flow<List<Responsavel>> = responsavelDao.getAll()

    suspend fun insert(responsavel: Responsavel) {
        responsavelDao.insert(responsavel)
    }
}