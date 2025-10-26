package com.example.desafio3.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio3.R
import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.repository.SolicitudRepository
import com.example.desafio3.data.repository.UsuarioRepository
import com.example.desafio3.ui.adapters.SolicitudAdapter

class MisAplicacionesActivity : AppCompatActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var recyclerView: RecyclerView

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var solicitudRepository: SolicitudRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mis_aplicaciones)

        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.recyclerViewAplicaciones)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val dbHelper = DatabaseHelper(this)
        usuarioRepository = UsuarioRepository(dbHelper)
        solicitudRepository = SolicitudRepository(dbHelper)

        cargarAplicaciones()
    }

    private fun cargarAplicaciones() {
        val userId = usuarioRepository.obtenerUsuarioActual()

        if (userId == null) {
            Toast.makeText(this, "Error al obtener usuario", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val solicitudes = solicitudRepository.obtenerSolicitudesPorCandidato(userId)

        val adapter = SolicitudAdapter(solicitudes, esEntrevistador = false)
        recyclerView.adapter = adapter

        if (solicitudes.isEmpty()) {
            Toast.makeText(this, "No has aplicado a ninguna oferta aún", Toast.LENGTH_SHORT).show()
        }
    }
}

