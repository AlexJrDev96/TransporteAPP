package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "escola")
data class Escola(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val endereco: String,
    val telefone: String
)