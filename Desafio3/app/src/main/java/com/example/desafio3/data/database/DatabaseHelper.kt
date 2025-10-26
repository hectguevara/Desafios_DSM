package com.example.desafio3.data.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.desafio3.data.model.Solicitud
import com.example.desafio3.data.model.Usuario

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "reclutamiento.db"
        private const val DATABASE_VERSION = 1
        
        private const val TABLE_USUARIOS = "usuarios"
        private const val TABLE_SOLICITUDES = "solicitudes"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("""
            CREATE TABLE $TABLE_USUARIOS (
                id TEXT PRIMARY KEY,
                nombre TEXT,
                apellido TEXT,
                correo TEXT,
                rol TEXT,
                anosExperiencia INTEGER,
                tituloAcademico TEXT
            )
        """)

        db?.execSQL("""
            CREATE TABLE $TABLE_SOLICITUDES (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                candidatoId TEXT,
                candidatoNombre TEXT,
                ofertaId TEXT,
                ofertaTitulo TEXT,
                resultado TEXT,
                fechaAplicacion TEXT,
                observaciones TEXT
            )
        """)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USUARIOS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_SOLICITUDES")
        onCreate(db)
    }

    fun insertarUsuario(usuario: Usuario): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put("id", usuario.id)
        values.put("nombre", usuario.nombre)
        values.put("apellido", usuario.apellido)
        values.put("correo", usuario.correo)
        values.put("rol", usuario.rol)
        values.put("anosExperiencia", usuario.anosExperiencia)
        values.put("tituloAcademico", usuario.tituloAcademico)
        return db.insertWithOnConflict(TABLE_USUARIOS, null, values, SQLiteDatabase.CONFLICT_REPLACE)
    }

    fun obtenerUsuarioPorId(id: String): Usuario? {
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_USUARIOS WHERE id = ?", arrayOf(id))
        
        var usuario: Usuario? = null
        if (cursor.moveToFirst()) {
            usuario = Usuario(
                id = cursor.getString(0),
                nombre = cursor.getString(1),
                apellido = cursor.getString(2),
                correo = cursor.getString(3),
                rol = cursor.getString(4),
                anosExperiencia = cursor.getInt(5),
                tituloAcademico = cursor.getString(6)
            )
        }
        cursor.close()
        return usuario
    }

    fun insertarSolicitud(solicitud: Solicitud): Long {
        val db = writableDatabase
        val values = ContentValues()
        values.put("candidatoId", solicitud.candidatoId)
        values.put("candidatoNombre", solicitud.candidatoNombre)
        values.put("ofertaId", solicitud.ofertaId)
        values.put("ofertaTitulo", solicitud.ofertaTitulo)
        values.put("resultado", solicitud.resultado)
        values.put("fechaAplicacion", solicitud.fechaAplicacion)
        values.put("observaciones", solicitud.observaciones)
        return db.insert(TABLE_SOLICITUDES, null, values)
    }

    fun obtenerSolicitudes(candidatoId: String? = null): List<Solicitud> {
        val solicitudes = mutableListOf<Solicitud>()
        val db = readableDatabase
        
        val query = if (candidatoId != null) {
            "SELECT * FROM $TABLE_SOLICITUDES WHERE candidatoId = ?"
        } else {
            "SELECT * FROM $TABLE_SOLICITUDES"
        }
        
        val cursor = if (candidatoId != null) {
            db.rawQuery(query, arrayOf(candidatoId))
        } else {
            db.rawQuery(query, null)
        }

        while (cursor.moveToNext()) {
            solicitudes.add(Solicitud(
                id = cursor.getInt(0),
                candidatoId = cursor.getString(1),
                candidatoNombre = cursor.getString(2),
                ofertaId = cursor.getString(3),
                ofertaTitulo = cursor.getString(4),
                resultado = cursor.getString(5),
                fechaAplicacion = cursor.getString(6),
                observaciones = cursor.getString(7)
            ))
        }
        cursor.close()
        return solicitudes
    }

    fun actualizarSolicitud(id: Int, resultado: String, observaciones: String): Int {
        val db = writableDatabase
        val values = ContentValues()
        values.put("resultado", resultado)
        values.put("observaciones", observaciones)
        return db.update(TABLE_SOLICITUDES, values, "id = ?", arrayOf(id.toString()))
    }

    fun existeAplicacion(candidatoId: String, ofertaId: String): Boolean {
        val db = readableDatabase
        val cursor = db.rawQuery(
            "SELECT 1 FROM $TABLE_SOLICITUDES WHERE candidatoId = ? AND ofertaId = ?",
            arrayOf(candidatoId, ofertaId)
        )
        val existe = cursor.count > 0
        cursor.close()
        return existe
    }
}
