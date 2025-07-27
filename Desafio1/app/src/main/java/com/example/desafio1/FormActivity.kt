package com.example.desafio1

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class FormActivity : AppCompatActivity() {
    private val CODIGO_CAMARA = 100
    private lateinit var permisoText: TextView
    private lateinit var campos: Map<String, EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_form)

        permisoText = findViewById(R.id.txtPermiso)

        campos = mapOf(
            "nombre" to findViewById(R.id.inputNombre),
            "correo" to findViewById(R.id.inputCorreo),
            "telefono" to findViewById(R.id.inputTelefono),
            "fecha" to findViewById(R.id.inputFecha),
            "direccion" to findViewById(R.id.inputDireccion)
        )

        findViewById<Button>(R.id.btnFoto).setOnClickListener {
            solicitarPermisoCamara()
        }

        findViewById<Button>(R.id.btnGuardar).setOnClickListener {
            if (validarCampos()) {
                val intent = Intent(this, SummaryActivity::class.java).apply {
                    campos.forEach { putExtra(it.key, it.value.text.toString()) }
                }
                startActivity(intent)
            }
        }
    }

    private fun solicitarPermisoCamara() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.CAMERA), CODIGO_CAMARA)
        } else {
            permisoText.text = "Permiso de cámara concedido"
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CODIGO_CAMARA) {
            permisoText.text = if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                "Permiso de cámara concedido"
            } else {
                "No se puede acceder a la cámara"
            }
        }
    }

    private fun validarCampos(): Boolean {
        val correoValido = campos["correo"]!!.text.toString().contains("@")
        val telefonoValido = campos["telefono"]!!.text.toString().matches(Regex("^\\d{8,}$"))
        val fechaValida = campos["fecha"]!!.text.toString().matches(Regex("^\\d{2}/\\d{2}/\\d{4}$"))
        val vacios = campos.values.any { it.text.isEmpty() }

        if (vacios || !correoValido || !telefonoValido || !fechaValida) {
            Toast.makeText(this, "Datos inválidos", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}