package com.example.fragmentos.api

import retrofit2.http.GET
import retrofit2.http.Path

interface ViaCepService {

    @GET("ws/{cep}/json/")
    suspend fun getEndereco(@Path("cep") cep: String): Endereco
}
