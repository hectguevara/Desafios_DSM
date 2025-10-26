package com.example.desafio3.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://68163b7232debfe95dbdd500.mockapi.io/academic/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val jobsApiService: JobsApiService by lazy {
        retrofit.create(JobsApiService::class.java)
    }
}

