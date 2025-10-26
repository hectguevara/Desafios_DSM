package com.example.desafio3.ui.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio3.R
import com.example.desafio3.data.model.Solicitud

class SolicitudAdapter(
    private var solicitudes: List<Solicitud>,
    private val esEntrevistador: Boolean,
    private val onAprobarClick: ((Solicitud) -> Unit)? = null,
    private val onRechazarClick: ((Solicitud) -> Unit)? = null
) : RecyclerView.Adapter<SolicitudAdapter.SolicitudViewHolder>() {

    inner class SolicitudViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCandidatoNombre: TextView = itemView.findViewById(R.id.tvCandidatoNombre)
        val tvOfertaTitulo: TextView = itemView.findViewById(R.id.tvOfertaTitulo)
        val tvEstado: TextView = itemView.findViewById(R.id.tvEstado)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
        val tvObservaciones: TextView = itemView.findViewById(R.id.tvObservaciones)
        val layoutBotones: LinearLayout = itemView.findViewById(R.id.layoutBotonesEntrevistador)
        val btnAprobar: Button = itemView.findViewById(R.id.btnAprobar)
        val btnRechazar: Button = itemView.findViewById(R.id.btnRechazar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SolicitudViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_solicitud, parent, false)
        return SolicitudViewHolder(view)
    }

    override fun onBindViewHolder(holder: SolicitudViewHolder, position: Int) {
        val solicitud = solicitudes[position]

        holder.tvCandidatoNombre.text = solicitud.candidatoNombre
        holder.tvOfertaTitulo.text = "Oferta: ${solicitud.ofertaTitulo}"
        holder.tvEstado.text = solicitud.resultado
        holder.tvFecha.text = "Fecha: ${solicitud.fechaAplicacion}"

        when (solicitud.resultado) {
            "Pendiente" -> holder.tvEstado.setTextColor(Color.parseColor("#FF9800"))
            "Aprobada" -> holder.tvEstado.setTextColor(Color.parseColor("#4CAF50"))
            "Rechazada" -> holder.tvEstado.setTextColor(Color.parseColor("#F44336"))
        }

        if (solicitud.observaciones.isNotEmpty()) {
            holder.tvObservaciones.visibility = View.VISIBLE
            holder.tvObservaciones.text = solicitud.observaciones
        } else {
            holder.tvObservaciones.visibility = View.GONE
        }

        if (esEntrevistador && solicitud.resultado == "Pendiente") {
            holder.layoutBotones.visibility = View.VISIBLE
            holder.btnAprobar.setOnClickListener { onAprobarClick?.invoke(solicitud) }
            holder.btnRechazar.setOnClickListener { onRechazarClick?.invoke(solicitud) }
        } else {
            holder.layoutBotones.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = solicitudes.size

    fun actualizarSolicitudes(nuevasSolicitudes: List<Solicitud>) {
        solicitudes = nuevasSolicitudes
        notifyDataSetChanged()
    }
}

