package com.example.desafio3.data.model

data class Solicitud(
    val id: Int = 0,
    val candidatoId: String = "",
    val candidatoNombre: String = "",
    val ofertaId: String = "",
    val ofertaTitulo: String = "",
    val resultado: String = "Pendiente", // "Pendiente", "Aprobada", "Rechazada"
    val fechaAplicacion: String = "",
    val observaciones: String = ""
)

