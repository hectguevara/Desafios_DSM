package com.example.desafio2

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio2.Datos.Tarea
import com.example.desafio2.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class TareaAdapter(private val tareas: List<Tarea>, private val context: Context) :
    RecyclerView.Adapter<TareaAdapter.TareaViewHolder>() {

    class TareaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitulo: TextView = itemView.findViewById(R.id.txtTitulo)
        val txtDescripcion: TextView = itemView.findViewById(R.id.txtDescripcion)
        val txtFecha: TextView = itemView.findViewById(R.id.txtFecha)
        val txtEstado: TextView = itemView.findViewById(R.id.txtEstado)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TareaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_tarea, parent, false)
        return TareaViewHolder(view)
    }

    override fun onBindViewHolder(holder: TareaViewHolder, position: Int) {
        val tarea = tareas[position]

        holder.txtTitulo.text = tarea.Titulo
        holder.txtDescripcion.text = tarea.Descripcion ?: ""
        holder.txtFecha.text = "Fecha: ${tarea.Fecha ?: "Desconocida"}"
        holder.txtEstado.text = if (tarea.Estado) "Completada" else "Pendiente"

        holder.itemView.setOnClickListener {
            mostrarDialogoOpciones(tarea)
        }
    }

    override fun getItemCount(): Int = tareas.size

    private fun mostrarDialogoOpciones(tarea: Tarea) {
        val opciones = arrayOf("Editar", "Eliminar")
        AlertDialog.Builder(context)
            .setTitle("Selecciona una acción")
            .setItems(opciones) { _, which ->
                when (which) {
                    0 -> mostrarDialogoEditar(tarea)
                    1 -> eliminarTarea(tarea)
                }
            }
            .show()
    }

    private fun mostrarDialogoEditar(tarea: Tarea) {
        val layout = LinearLayout(context)
        layout.orientation = LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)

        val inputTitulo = EditText(context).apply {
            hint = "Título"
            inputType = InputType.TYPE_CLASS_TEXT
            setText(tarea.Titulo)
        }

        val inputDescripcion = EditText(context).apply {
            hint = "Descripción"
            inputType = InputType.TYPE_CLASS_TEXT
            setText(tarea.Descripcion)
        }

        val chkEstado = CheckBox(context).apply {
            text = "¿Tarea completada?"
            isChecked = tarea.Estado
        }

        layout.addView(inputTitulo)
        layout.addView(inputDescripcion)
        layout.addView(chkEstado)

        AlertDialog.Builder(context)
            .setTitle("Editar tarea")
            .setView(layout)
            .setPositiveButton("Guardar") { _, _ ->
                val nuevoTitulo = inputTitulo.text.toString().trim()
                if (nuevoTitulo.isEmpty()) {
                    Toast.makeText(context, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                val nuevaDescripcion = inputDescripcion.text.toString().trim()
                val nuevoEstado = chkEstado.isChecked

                val actualizaciones = mapOf<String, Any?>(
                    "Titulo" to nuevoTitulo,
                    "Descripcion" to nuevaDescripcion,
                    "Estado" to nuevoEstado
                )

                val uid = FirebaseAuth.getInstance().currentUser?.uid
                if (uid != null && tarea.key != null) {
                    FirebaseDatabase.getInstance().getReference("tareas")
                        .child(uid)
                        .child(tarea.key!!)
                        .updateChildren(actualizaciones)
                        .addOnSuccessListener {
                            Toast.makeText(context, "Tarea actualizada", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show()
                        }
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun eliminarTarea(tarea: Tarea) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null && tarea.key != null) {
            FirebaseDatabase.getInstance().getReference("tareas")
                .child(uid)
                .child(tarea.key!!)
                .removeValue()
                .addOnSuccessListener {
                    Toast.makeText(context, "Tarea eliminada", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Error al eliminar", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
