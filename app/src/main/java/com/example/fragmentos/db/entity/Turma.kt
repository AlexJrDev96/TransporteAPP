package com.example.fragmentos.db.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "turma",
    foreignKeys = [
        ForeignKey(entity = Escola::class, parentColumns = ["id"], childColumns = ["escolaId"]),
        ForeignKey(entity = Tripulante::class, parentColumns = ["id"], childColumns = ["tripulanteId"])
    ])
data class Turma(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val escolaId: Int,
    val tripulanteId: Int
)