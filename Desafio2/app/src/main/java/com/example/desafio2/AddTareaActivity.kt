package com.example.desafio2

import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.desafio2.Datos.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import android.app.DatePickerDialog
import java.util.*

class AddTareaActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_tarea)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val txtTitulo = findViewById<EditText>(R.id.txtTitulo)
        val txtDescripcion = findViewById<EditText>(R.id.txtDescripcion)
        val chkEstado = findViewById<CheckBox>(R.id.chkEstado)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)

        val etFecha = findViewById<EditText>(R.id.etFecha)

        etFecha.setOnClickListener {
            val calendar = Calendar.getInstance()
            val datePicker = DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    val fechaSeleccionada = String.format("%02d/%02d/%04d", dayOfMonth, month + 1, year)
                    etFecha.setText(fechaSeleccionada)
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            datePicker.show()
        }

        btnGuardar.setOnClickListener {
            val titulo = txtTitulo.text.toString().trim()
            val descripcion = txtDescripcion.text.toString().trim()
            val estado = chkEstado.isChecked


            if (titulo.isEmpty()) {
                Toast.makeText(this, "El título es obligatorio", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val fecha = etFecha.text.toString().trim()

            if (fecha.isEmpty()) {
                Toast.makeText(this, "La fecha es obligatoria", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val tarea = Tarea(
                Titulo = titulo,
                Descripcion = descripcion,
                Estado = estado,
                Fecha = fecha,
                key = null
            )

            val user = FirebaseAuth.getInstance().currentUser
            val uid = user?.uid

            if (uid != null) {
                val ref = FirebaseDatabase.getInstance().getReference("tareas").child(uid)
                ref.push().setValue(tarea).addOnSuccessListener {
                    Toast.makeText(this, "Tarea guardada", Toast.LENGTH_SHORT).show()
                    finish() // Regresa a MainActivity
                }.addOnFailureListener {
                    Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}