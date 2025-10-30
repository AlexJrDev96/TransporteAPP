package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "responsavel")
data class Responsavel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val cpf: String,
    val telefone: String,
    val email: String,
    val endereco: String
)