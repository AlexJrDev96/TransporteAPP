package com.example.fragmentos

import android.app.Application
import com.example.fragmentos.db.AppDatabase
import com.example.fragmentos.db.repository.AlunoRepository
import com.example.fragmentos.db.repository.EscolaRepository
import com.example.fragmentos.db.repository.ResponsavelRepository
import com.example.fragmentos.db.repository.TripulanteRepository
import com.example.fragmentos.db.repository.TurmaRepository
import com.example.fragmentos.db.repository.UserRepository

class TransporteApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val tripulanteRepository by lazy { TripulanteRepository(database.tripulanteDao()) }
    val alunoRepository by lazy { AlunoRepository(database.alunoDao()) }
    val responsavelRepository by lazy { ResponsavelRepository(database.responsavelDao()) }
    val escolaRepository by lazy { EscolaRepository(database.escolaDao()) }
    val turmaRepository by lazy { TurmaRepository(database.turmaDao()) }
    val userRepository by lazy { UserRepository(database.userDao()) }
}