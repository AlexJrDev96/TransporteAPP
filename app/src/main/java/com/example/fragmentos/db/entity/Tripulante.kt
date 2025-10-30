package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tripulante")
data class Tripulante(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val funcao: String,
    val telefone: String,
    val cpf: String
)