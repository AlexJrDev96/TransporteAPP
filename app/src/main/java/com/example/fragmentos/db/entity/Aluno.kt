package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "aluno",
    foreignKeys = [
        ForeignKey(entity = Turma::class, parentColumns = ["id"], childColumns = ["turmaId"], onDelete = ForeignKey.CASCADE)
    ])
data class Aluno(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val dataNascimento: String,
    val nomeResponsavel: String,
    val telefoneResponsavel: String,
    val cep: String,
    val logradouro: String,
    val bairro: String,
    val numero: String,
    val turmaId: Int
)