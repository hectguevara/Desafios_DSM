package com.example.desafio3.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.desafio3.R
import com.example.desafio3.data.model.Oferta

class OfertaAdapter(
    private var ofertas: List<Oferta>,
    private val onAplicarClick: (Oferta) -> Unit
) : RecyclerView.Adapter<OfertaAdapter.OfertaViewHolder>() {

    inner class OfertaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTituloOferta)
        val tvEmpresa: TextView = itemView.findViewById(R.id.tvEmpresa)
        val tvUbicacion: TextView = itemView.findViewById(R.id.tvUbicacion)
        val tvSalario: TextView = itemView.findViewById(R.id.tvSalario)
        val tvDescripcion: TextView = itemView.findViewById(R.id.tvDescripcion)
        val btnAplicar: Button = itemView.findViewById(R.id.btnAplicar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfertaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_oferta, parent, false)
        return OfertaViewHolder(view)
    }

    override fun onBindViewHolder(holder: OfertaViewHolder, position: Int) {
        val oferta = ofertas[position]

        holder.tvTitulo.text = oferta.title
        holder.tvEmpresa.text = oferta.company
        
        if (oferta.location.isNotEmpty()) {
            holder.tvUbicacion.visibility = View.VISIBLE
            holder.tvUbicacion.text = oferta.location
        } else {
            holder.tvUbicacion.visibility = View.GONE
        }
        
        holder.tvSalario.text = "$${oferta.salary}"
        holder.tvDescripcion.text = oferta.description

        holder.btnAplicar.setOnClickListener {
            onAplicarClick(oferta)
        }
    }

    override fun getItemCount(): Int = ofertas.size

    fun actualizarOfertas(nuevasOfertas: List<Oferta>) {
        ofertas = nuevasOfertas
        notifyDataSetChanged()
    }
}

