package com.example.fragmentos.db.repository

import com.example.fragmentos.db.dao.AlunoDao
import com.example.fragmentos.db.entity.Aluno
import kotlinx.coroutines.flow.Flow

class AlunoRepository(private val alunoDao: AlunoDao) {

    val allAlunos: Flow<List<Aluno>> = alunoDao.getAll()

    suspend fun insert(aluno: Aluno) {
        alunoDao.insert(aluno)
    }

    suspend fun update(aluno: Aluno) {
        alunoDao.update(aluno)
    }

    suspend fun delete(aluno: Aluno) {
        alunoDao.delete(aluno)
    }
}