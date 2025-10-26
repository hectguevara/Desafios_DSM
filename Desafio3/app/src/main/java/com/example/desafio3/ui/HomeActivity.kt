package com.example.desafio3.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio3.R
import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.model.Oferta
import com.example.desafio3.data.model.Solicitud
import com.example.desafio3.data.model.Usuario
import com.example.desafio3.data.repository.OfertaRepository
import com.example.desafio3.data.repository.SolicitudRepository
import com.example.desafio3.data.repository.UsuarioRepository
import com.example.desafio3.ui.adapters.OfertaAdapter
import com.example.desafio3.ui.adapters.SolicitudAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeActivity : AppCompatActivity() {

    private lateinit var tvNombreUsuario: TextView
    private lateinit var tvRolUsuario: TextView
    private lateinit var btnCerrarSesion: Button
    private lateinit var layoutBotonesCandidato: LinearLayout
    private lateinit var btnVerOfertas: Button
    private lateinit var btnMisAplicaciones: Button
    private lateinit var tvTituloPrincipal: TextView
    private lateinit var recyclerView: RecyclerView

    private lateinit var usuarioRepository: UsuarioRepository
    private lateinit var ofertaRepository: OfertaRepository
    private lateinit var solicitudRepository: SolicitudRepository

    private var usuarioActual: Usuario? = null
    private var ofertas: List<Oferta> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Vincular vistas con sus IDs
        tvNombreUsuario = findViewById(R.id.tvNombreUsuario)
        tvRolUsuario = findViewById(R.id.tvRolUsuario)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        layoutBotonesCandidato = findViewById(R.id.layoutBotonesCandidato)
        btnVerOfertas = findViewById(R.id.btnVerOfertas)
        btnMisAplicaciones = findViewById(R.id.btnMisAplicaciones)
        tvTituloPrincipal = findViewById(R.id.tvTituloPrincipal)
        recyclerView = findViewById(R.id.recyclerView)

        // Configurar RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar repositorios
        val dbHelper = DatabaseHelper(this)
        usuarioRepository = UsuarioRepository(dbHelper)
        ofertaRepository = OfertaRepository()
        solicitudRepository = SolicitudRepository(dbHelper)

        // Cargar información del usuario actual
        cargarDatosUsuario()

        // Configurar eventos de botones
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        btnVerOfertas.setOnClickListener {
            cargarOfertas()
        }

        btnMisAplicaciones.setOnClickListener {
            val intent = Intent(this, MisAplicacionesActivity::class.java)
            startActivity(intent)
        }
    }

    private fun cargarDatosUsuario() {
        val userId = usuarioRepository.obtenerUsuarioActual()

        if (userId == null) {
            irALogin()
            return
        }

        val usuarioLocal = usuarioRepository.obtenerUsuarioLocal(userId)

        if (usuarioLocal != null) {
            usuarioActual = usuarioLocal
            mostrarDatosUsuario(usuarioLocal)
        } else {
            usuarioRepository.cargarUsuarioDeFirebase(userId,
                onSuccess = { usuario ->
                    usuarioActual = usuario
                    mostrarDatosUsuario(usuario)
                },
                onError = { error ->
                    val usuarioTemporal = Usuario(
                        id = userId,
                        nombre = "Usuario",
                        apellido = "",
                        correo = "",
                        rol = "Candidato",
                        anosExperiencia = 0,
                        tituloAcademico = ""
                    )
                    usuarioActual = usuarioTemporal
                    mostrarDatosUsuario(usuarioTemporal)
                }
            )
        }
    }

    private fun mostrarDatosUsuario(usuario: Usuario) {
        tvNombreUsuario.text = "Bienvenido ${usuario.nombre} ${usuario.apellido}"
        tvRolUsuario.text = usuario.rol

        if (usuario.rol == "Candidato") {
            layoutBotonesCandidato.visibility = View.VISIBLE
            cargarOfertas()
        } else {
            layoutBotonesCandidato.visibility = View.GONE
            cargarSolicitudes()
        }
    }

    private fun cargarOfertas() {
        tvTituloPrincipal.text = "Ofertas Disponibles"

        CoroutineScope(Dispatchers.IO).launch {
            val ofertasObtenidas = ofertaRepository.obtenerOfertas()

            withContext(Dispatchers.Main) {
                if (ofertasObtenidas.isNotEmpty()) {
                    ofertas = ofertasObtenidas
                    val adapter = OfertaAdapter(ofertas) { oferta ->
                        aplicarAOferta(oferta)
                    }
                    recyclerView.adapter = adapter
                } else {
                    Toast.makeText(
                        this@HomeActivity, 
                        "No hay ofertas. Verifica que la API tenga datos", 
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun aplicarAOferta(oferta: Oferta) {
        val candidatoId = usuarioActual?.id ?: return

        if (solicitudRepository.existeAplicacion(candidatoId, oferta.id)) {
            Toast.makeText(this, "Ya has aplicado a esta oferta", Toast.LENGTH_SHORT).show()
            return
        }

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
        val tituloOferta = if (oferta.title.isNotEmpty()) oferta.title else "Oferta sin título"

        val solicitud = Solicitud(
            candidatoId = candidatoId,
            candidatoNombre = "${usuarioActual?.nombre} ${usuarioActual?.apellido}",
            ofertaId = oferta.id,
            ofertaTitulo = tituloOferta,
            resultado = "Pendiente",
            fechaAplicacion = fechaActual,
            observaciones = ""
        )

        val resultado = solicitudRepository.crearSolicitud(solicitud)

        if (resultado > 0) {
            Toast.makeText(this, "Aplicación enviada exitosamente", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Error al enviar aplicación", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarSolicitudes() {
        tvTituloPrincipal.text = "Solicitudes Recibidas"

        val solicitudes = solicitudRepository.obtenerTodasLasSolicitudes()

        val adapter = SolicitudAdapter(
            solicitudes,
            esEntrevistador = true,
            onAprobarClick = { solicitud ->
                mostrarDialogoActualizarSolicitud(solicitud, "Aprobada")
            },
            onRechazarClick = { solicitud ->
                mostrarDialogoActualizarSolicitud(solicitud, "Rechazada")
            }
        )

        recyclerView.adapter = adapter

        if (solicitudes.isEmpty()) {
            Toast.makeText(this, "No hay solicitudes registradas", Toast.LENGTH_SHORT).show()
        }
    }

    private fun mostrarDialogoActualizarSolicitud(solicitud: Solicitud, nuevoEstado: String) {
        val etObservaciones = EditText(this)
        etObservaciones.hint = "Observaciones"
        etObservaciones.setPadding(50, 50, 50, 50)

        AlertDialog.Builder(this)
            .setTitle(nuevoEstado)
            .setView(etObservaciones)
            .setPositiveButton("Confirmar") { _, _ ->
                actualizarSolicitud(solicitud.id, nuevoEstado, etObservaciones.text.toString())
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun actualizarSolicitud(id: Int, resultado: String, observaciones: String) {
        val actualizado = solicitudRepository.actualizarSolicitud(id, resultado, observaciones)

        if (actualizado > 0) {
            Toast.makeText(this, "Solicitud actualizada", Toast.LENGTH_SHORT).show()
            cargarSolicitudes() // Recargar lista
        } else {
            Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cerrarSesion() {
        usuarioRepository.cerrarSesion()
        irALogin()
    }

    private fun irALogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}

