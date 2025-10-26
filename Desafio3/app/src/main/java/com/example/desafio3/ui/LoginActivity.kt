package com.example.desafio3.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio3.R
import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.repository.UsuarioRepository
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var etCorreo: TextInputEditText
    private lateinit var etPassword: TextInputEditText
    private lateinit var btnLogin: Button
    private lateinit var btnIrRegistro: Button

    private lateinit var usuarioRepository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Inicializar componentes de la interfaz
        etCorreo = findViewById(R.id.etCorreoLogin)
        etPassword = findViewById(R.id.etPasswordLogin)
        btnLogin = findViewById(R.id.btnLogin)
        btnIrRegistro = findViewById(R.id.btnIrRegistro)

        // Crear instancia del repositorio
        val dbHelper = DatabaseHelper(this)
        usuarioRepository = UsuarioRepository(dbHelper)

        // Configurar eventos de botones
        btnLogin.setOnClickListener {
            iniciarSesion()
        }

        btnIrRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    private fun irAHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun iniciarSesion() {
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()

        if (correo.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        usuarioRepository.iniciarSesion(correo, password,
            onSuccess = { userId ->
                val usuarioLocal = usuarioRepository.obtenerUsuarioLocal(userId)
                
                if (usuarioLocal != null) {
                    Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                    irAHome()
                } else {
                    usuarioRepository.cargarUsuarioDeFirebase(userId,
                        onSuccess = { usuario ->
                            Toast.makeText(this, "Bienvenido", Toast.LENGTH_SHORT).show()
                            irAHome()
                        },
                        onError = { error ->
                            Toast.makeText(this, "Sesión iniciada", Toast.LENGTH_SHORT).show()
                            irAHome()
                        }
                    )
                }
            },
            onError = { error ->
                Toast.makeText(this, "Error: $error", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

