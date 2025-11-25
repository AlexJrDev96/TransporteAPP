package com.example.fragmentos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.fragmentos.db.dao.*
import com.example.fragmentos.db.entity.*

@Database(entities = [Tripulante::class, Aluno::class, Responsavel::class, Escola::class, Turma::class, User::class], version = 17, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun tripulanteDao(): TripulanteDao
    abstract fun alunoDao(): AlunoDao
    abstract fun responsavelDao(): ResponsavelDao
    abstract fun escolaDao(): EscolaDao
    abstract fun turmaDao(): TurmaDao
    abstract fun userDao(): UserDao

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