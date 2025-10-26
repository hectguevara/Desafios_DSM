package com.example.desafio3.data.model

data class Usuario(
    val id: String = "",
    val nombre: String = "",
    val apellido: String = "",
    val correo: String = "",
    val rol: String = "", // "Candidato" o "Entrevistador"
    val anosExperiencia: Int = 0,
    val tituloAcademico: String = ""
)

