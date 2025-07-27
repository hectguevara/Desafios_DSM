package com.example.desafio1

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SummaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_summary)

        val campos = listOf("nombre", "correo", "telefono", "fecha", "direccion")
        campos.forEach {
            findViewById<TextView>(resources.getIdentifier("txt_$it", "id", packageName)).text =
                intent.getStringExtra(it)
        }

        findViewById<Button>(R.id.btnInicio).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        findViewById<Button>(R.id.btnNuevo).setOnClickListener {
            startActivity(Intent(this, FormActivity::class.java))
        }
    }
}
