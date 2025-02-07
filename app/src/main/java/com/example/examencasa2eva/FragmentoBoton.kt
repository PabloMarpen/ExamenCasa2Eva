package com.example.examencasa2eva

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class FragmentoBoton : Fragment() {

    private var comunicador: ComunicadorFragments? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        comunicador = context as? ComunicadorFragments
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragmento_boton, container, false)
        val boton = view.findViewById<Button>(R.id.buttonEnviar)
        val editTextLatitud = view.findViewById<EditText>(R.id.editTextLatitud)
        val editTextLongitud = view.findViewById<EditText>(R.id.editTextLongitud)

        boton.setOnClickListener {
            val latText = editTextLatitud.text.toString()
            val lonText = editTextLongitud.text.toString()

            if (latText.isNotEmpty() && lonText.isNotEmpty()) {
                comunicador?.enviarDatos(latText.toDouble(), lonText.toDouble())
            } else {
                Toast.makeText(context, "Introduce ambas coordenadas", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}