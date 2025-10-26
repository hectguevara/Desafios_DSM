package com.example.desafio3

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio3.data.database.DatabaseHelper
import com.example.desafio3.data.repository.UsuarioRepository
import com.example.desafio3.ui.HomeActivity
import com.example.desafio3.ui.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dbHelper = DatabaseHelper(this)
        val usuarioRepository = UsuarioRepository(dbHelper)

        val userId = usuarioRepository.obtenerUsuarioActual()

        if (userId != null) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        finish()
    }
}