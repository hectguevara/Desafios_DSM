package com.example.desafio3.data.repository

import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.model.Solicitud

class SolicitudRepository(private val dbHelper: DatabaseHelper) {

    fun crearSolicitud(solicitud: Solicitud): Long {
        return dbHelper.insertarSolicitud(solicitud)
    }

    fun obtenerSolicitudesPorCandidato(candidatoId: String): List<Solicitud> {
        return dbHelper.obtenerSolicitudes(candidatoId)
    }

    fun obtenerTodasLasSolicitudes(): List<Solicitud> {
        return dbHelper.obtenerSolicitudes()
    }

    fun actualizarSolicitud(id: Int, resultado: String, observaciones: String): Int {
        return dbHelper.actualizarSolicitud(id, resultado, observaciones)
    }

    fun existeAplicacion(candidatoId: String, ofertaId: String): Boolean {
        return dbHelper.existeAplicacion(candidatoId, ofertaId)
    }
}

