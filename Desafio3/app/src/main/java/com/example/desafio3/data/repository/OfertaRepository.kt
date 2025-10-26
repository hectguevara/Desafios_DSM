package com.example.desafio3.data.repository

import com.example.desafio3.data.api.RetrofitClient
import com.example.desafio3.data.model.Oferta

class OfertaRepository {

    suspend fun obtenerOfertas(): List<Oferta> {
        return try {
            val response = RetrofitClient.jobsApiService.obtenerOfertas()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}

