package com.example.desafio2.Datos

data class Tarea(
    var Titulo: String? = null,
    var Descripcion: String? = null,
    var Estado: Boolean = false,
    var Fecha: String? = null,
    var key: String? = null,
    var ta: MutableMap<String, Boolean>? = mutableMapOf()
) {
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "Titulo" to Titulo,
            "Descripcion" to Descripcion,
            "Estado" to Estado,
            "Fecha" to Fecha,
            "ta" to ta
        )
    }
}