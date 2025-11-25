package com.example.fragmentos.api

import com.google.gson.annotations.SerializedName

data class Endereco(
    @SerializedName("cep")
    val cep: String,
    
    @SerializedName("logradouro")
    val logradouro: String,
    
    @SerializedName("bairro")
    val bairro: String,
    
    @SerializedName("localidade")
    val localidade: String,
    
    @SerializedName("uf")
    val uf: String
)
