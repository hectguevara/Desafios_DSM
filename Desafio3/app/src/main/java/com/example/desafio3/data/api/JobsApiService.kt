package com.example.desafio3.data.api

import com.example.desafio3.data.model.Oferta
import retrofit2.Response
import retrofit2.http.GET

interface JobsApiService {
    @GET("jobs")
    suspend fun obtenerOfertas(): Response<List<Oferta>>
}

