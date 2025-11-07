package com.example.fragmentos.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.fragmentos.db.dao.*
import com.example.fragmentos.db.entity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Tripulante::class, Aluno::class, Responsavel::class, Escola::class, Turma::class, User::class], version = 10, exportSchema = false)
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
                .addCallback(AppDatabaseCallback(context))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let {
                database ->
                CoroutineScope(Dispatchers.IO).launch {
                    prePopulateDatabase(database)
                }
            }
        }

        suspend fun prePopulateDatabase(database: AppDatabase) {
            val userDao = database.userDao()
            userDao.insert(User(email = "admin@admin.com", password = "admin"))

            val responsavelDao = database.responsavelDao()
            responsavelDao.insert(Responsavel(nome = "João da Silva", cpf = "111.111.111-11", telefone = "11987654321", email = "joao@email.com", endereco = "Rua A, 123"))
            responsavelDao.insert(Responsavel(nome = "Maria Oliveira", cpf = "222.222.222-22", telefone = "11912345678", email = "maria@email.com", endereco = "Rua B, 456"))

            val tripulanteDao = database.tripulanteDao()
            tripulanteDao.insert(Tripulante(nome = "Carlos Souza", funcao = "Motorista", telefone = "11999998888", cpf = "333.333.333-33"))
            tripulanteDao.insert(Tripulante(nome = "Fernanda Lima", funcao = "Monitora", telefone = "11977776666", cpf = "444.444.444-44"))

            val escolaDao = database.escolaDao()
            escolaDao.insert(Escola(nome = "Escola Modelo", endereco = "Avenida Principal, 789", telefone = "1140028922"))

            val turmaDao = database.turmaDao()
            turmaDao.insert(Turma(nome = "Turma da Manhã", periodo = "Manhã", escolaId = 1, tripulanteId = 1))

            val alunoDao = database.alunoDao()
            alunoDao.insert(Aluno(nome = "Pedro Silva", dataNascimento = "01/01/2015", nomeResponsavel = "João da Silva", telefoneResponsavel = "11987654321", endereco = "Rua A, 123", turmaId = 1))
            alunoDao.insert(Aluno(nome = "Ana Oliveira", dataNascimento = "02/02/2016", nomeResponsavel = "Maria Oliveira", telefoneResponsavel = "11912345678", endereco = "Rua B, 456", turmaId = 1))
        }
    }
}