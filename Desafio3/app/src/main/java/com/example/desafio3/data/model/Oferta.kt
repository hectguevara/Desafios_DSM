package com.example.desafio3.data.model

import com.google.gson.annotations.SerializedName

data class Oferta(
    val id: String = "",
    
    @SerializedName(value = "puesto", alternate = ["title", "titulo"])
    val title: String = "",
    
    @SerializedName(value = "empresa", alternate = ["company"])
    val company: String = "",
    
    @SerializedName(value = "ubicacion", alternate = ["location"])
    val location: String = "",
    
    @SerializedName(value = "descripcion", alternate = ["description"])
    val description: String = "",
    
    @SerializedName(value = "salario", alternate = ["salary"])
    val salary: String = "",
    
    @SerializedName(value = "requisitos", alternate = ["requirements"])
    val requirements: String = ""
)

