package com.example.desafio2

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.Datos.Tarea
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var rvTareas: RecyclerView
    private lateinit var tareaAdapter: TareaAdapter
    private val tareas = mutableListOf<Tarea>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Mostrar correo del usuario
        val userEmail = FirebaseAuth.getInstance().currentUser?.email
        findViewById<TextView>(R.id.txtBienvenida).text = "Bienvenido, $userEmail"

        // Configurar RecyclerView
        rvTareas = findViewById(R.id.rvTareas)
        rvTareas.layoutManager = LinearLayoutManager(this)
        tareaAdapter = TareaAdapter(tareas, this)
        rvTareas.adapter = tareaAdapter

        // Cargar tareas del usuario desde Firebase
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("tareas").child(uid ?: "")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tareas.clear()
                for (dato in snapshot.children) {
                    val tarea = dato.getValue(Tarea::class.java)
                    tarea?.key = dato.key
                    if (tarea != null) {
                        tareas.add(tarea)
                    }
                }
                tareaAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, RegisterActivity::class.java))
                finish()
            }

            R.id.action_add -> {
                startActivity(Intent(this, AddTareaActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }
}