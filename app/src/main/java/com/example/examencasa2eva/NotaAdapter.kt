package com.example.examencasa2eva

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Define la clase DiscosAdapter, que extiende RecyclerView.Adapter y especifica MyViewHolder como su ViewHolder.
class NotaAdapter(private val listaNotas: List<Nota>) : RecyclerView.Adapter<NotaAdapter.MyViewHolder>() {

    // Define la clase interna MyViewHolder, que extiende RecyclerView.ViewHolder.
    // Esta clase proporciona una referencia a las vistas de cada elemento de datos.
    // Encuentra y almacena referencias a los elementos de la interfaz de usuario en el layout del ítem.
    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var idNota: TextView = view.findViewById(R.id.textViewID)
        var tituloNota: TextView = view.findViewById(R.id.textViewTitulo)
        var contenidoNota: TextView = view.findViewById(R.id.textViewContenido)
    }

    // Crea nuevas vistas (invocadas por el layout manager).
    // Infla el layout del ítem de la lista (disco_item.xml) y lo pasa al constructor de MyViewHolder.

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.nota_item, parent, false)
        return MyViewHolder(itemView)
    }

    // Reemplaza el contenido de una vista (invocada por el layout manager).
    // Obtiene el elemento de la lista de discos en esta posición.
    // Reemplaza el contenido de las vistas con los datos del elemento en cuestión.

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val Nota = listaNotas[position]
        holder.idNota.text = Nota.id.toString()
        holder.tituloNota.text = Nota.titulo
        holder.contenidoNota.text = Nota.contenido
    }

    // Devuelve el tamaño de la lista de datos (invocado por el layout manager).
    override fun getItemCount() = listaNotas.size
}