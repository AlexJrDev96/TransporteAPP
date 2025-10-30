package com.example.fragmentos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentos.db.dao.AlunoDao
import com.example.fragmentos.db.dao.EscolaDao
import com.example.fragmentos.db.dao.ResponsavelDao
import com.example.fragmentos.db.dao.TripulanteDao
import com.example.fragmentos.db.dao.TurmaDao
import com.example.fragmentos.db.entity.Aluno
import com.example.fragmentos.db.entity.Escola
import com.example.fragmentos.db.entity.Responsavel
import com.example.fragmentos.db.entity.Tripulante
import com.example.fragmentos.db.entity.Turma

@Database(entities = [Tripulante::class, Aluno::class, Responsavel::class, Escola::class, Turma::class], version = 5, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripulanteDao(): TripulanteDao
    abstract fun alunoDao(): AlunoDao
    abstract fun responsavelDao(): ResponsavelDao
    abstract fun escolaDao(): EscolaDao
    abstract fun turmaDao(): TurmaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}