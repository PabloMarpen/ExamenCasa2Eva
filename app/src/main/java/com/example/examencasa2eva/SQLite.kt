package com.example.examencasa2eva

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SQLite : AppCompatActivity() {

    private lateinit var textID: EditText
    private lateinit var textTitulo: EditText
    private lateinit var textContenido: EditText
    private lateinit var buttonInsertar: Button
    private lateinit var buttonBorrar: Button
    private lateinit var buttonEditar: Button
    private lateinit var dbHandler: DatabaseHelper
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.sqlite)

        textID = findViewById(R.id.editTextID)
        textTitulo = findViewById(R.id.editTextTitulo)
        textContenido = findViewById(R.id.editTextContenido)
        buttonInsertar = findViewById(R.id.button5)
        buttonBorrar = findViewById(R.id.button6)
        buttonEditar = findViewById(R.id.button7)
        recyclerView = findViewById(R.id.Lista) // 🚀 Inicializar el RecyclerView
        dbHandler = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this) // 🚀 Configurar layoutManager
        viewDiscos()

        buttonInsertar.setOnClickListener { addDisco() }
        buttonBorrar.setOnClickListener { borrarDisco() }
        buttonEditar.setOnClickListener { actualizarDisco() }



    }

    private fun actualizarDisco() {
        val id = textID.text.toString()
        val titulo = textTitulo.text.toString()
        val contenido = textContenido.text.toString()

        if (titulo.isNotEmpty() && contenido.isNotEmpty() && id.isNotEmpty()) {
            val nota = Nota(titulo = titulo, contenido = contenido, id = id.toInt())
            val status = dbHandler.updateNota(nota)
            if (status > -1) {
                Toast.makeText(applicationContext, "Nota actualizado", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }
        } else {
            Toast.makeText(applicationContext, "Nombre, Año e id son requeridos", Toast.LENGTH_LONG).show()
        }
    }

    private fun borrarDisco() {
        val id = textID.text.toString()
        if (id.isNotEmpty()) {
            val nota = Nota(id = id.toInt())
            val status = dbHandler.deleteNota(nota)
            if (status > -1) {
                Toast.makeText(applicationContext, "Nota eliminada", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }else {
                Toast.makeText(applicationContext, "id es requerido", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun addDisco() {
        val titulo = textTitulo.text.toString()
        val contenido = textContenido.text.toString()
        if (titulo.isNotEmpty() && contenido.isNotEmpty()) {
            val nota = Nota(titulo = titulo, contenido = contenido)
            val status = dbHandler.addNota(nota)
            if (status > -1) {
                Toast.makeText(applicationContext, "Nota agregada", Toast.LENGTH_LONG).show()
                clearEditTexts()
                viewDiscos()
            }
        } else {
            Toast.makeText(applicationContext, "Nombre y Año son requeridos", Toast.LENGTH_LONG).show()
        }
    }



    private fun viewDiscos() {
        val NotaLista = dbHandler.getAllNotas()
        val adapter = NotaAdapter(NotaLista)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }

    // Métodoo para limpiar los campos de texto.
    private fun clearEditTexts() {
        textID.text.clear()
        textTitulo.text.clear()
        textContenido.text.clear()
    }
}