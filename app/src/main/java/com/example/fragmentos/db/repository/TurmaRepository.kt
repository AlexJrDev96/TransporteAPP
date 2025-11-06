package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.TurmaDao
import com.example.fragmentos.db.entity.Turma
import kotlinx.coroutines.flow.Flow

class TurmaRepository(private val turmaDao: TurmaDao) {

    val allTurmas: Flow<List<Turma>> = turmaDao.getAll()

    suspend fun insert(turma: Turma) {
        turmaDao.insert(turma)
    }

    suspend fun update(turma: Turma) {
        turmaDao.update(turma)
    }

    suspend fun delete(turma: Turma) {
        turmaDao.delete(turma)
    }
}