package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "aluno")
data class Aluno(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val dataNascimento: String,
    val nomeResponsavel: String,
    val telefoneResponsavel: String,
    val endereco: String
)