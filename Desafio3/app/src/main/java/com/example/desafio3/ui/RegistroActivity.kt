package com.example.desafio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio3.R
import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.model.Usuario
import com.example.desafio3.data.repository.UsuarioRepository
import com.google.android.material.textfield.TextInputEditText

class RegistroActivity : AppCompatActivity() {

    private lateinit var etNombre: TextInputEditText
    private lateinit var etApellido: TextInputEditText
    private lateinit var etCorreo: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var spinnerRol: Spinner
    private lateinit var etExperiencia: TextInputEditText
    private lateinit var etTitulo: TextInputEditText
    private lateinit var btnRegistrar: Button
    private lateinit var btnVolverLogin: Button

    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        spinnerRol = findViewById(R.id.spinnerRol)
        etExperiencia = findViewById(R.id.etExperiencia)
        etTitulo = findViewById(R.id.etTitulo)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        btnVolverLogin = findViewById(R.id.btnVolverLogin)

        val roles = arrayOf("Candidato", "Entrevistador")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        spinnerRol.adapter = adapter

        val dbHelper = DatabaseHelper(this)
        usuarioRepository = UsuarioRepository(dbHelper)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }

        btnVolverLogin.setOnClickListener {
            finish()
        }
    }

    private fun registrarUsuario() {
        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val rol = spinnerRol.selectedItem.toString()
        val experienciaStr = etExperiencia.text.toString().trim()
        val titulo = etTitulo.text.toString().trim()

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || 
            password.isEmpty() || experienciaStr.isEmpty() || titulo.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        val experiencia = experienciaStr.toIntOrNull() ?: 0

        val usuario = Usuario(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            rol = rol,
            anosExperiencia = experiencia,
            tituloAcademico = titulo
        )

        usuarioRepository.registrarUsuario(correo, password, usuario,
            onSuccess = { userId ->
                Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            },
            onError = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_LONG).show()
            }
        )
    }
}

