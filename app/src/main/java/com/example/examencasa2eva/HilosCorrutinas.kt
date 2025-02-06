package com.example.examencasa2eva

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HilosCorrutinas : AppCompatActivity() {

    private lateinit var textoResultado: TextView
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.hilos_corrutinas)

        val botonCalcular: Button = findViewById(R.id.button2)
        val entradaNumero: EditText = findViewById(R.id.editTextPrimos)
        val botonCalcularCoroutines: Button = findViewById(R.id.button4)
        val botonSQLite: Button = findViewById(R.id.buttonSQLite)
        textoResultado = findViewById(R.id.textViewResultado)


        botonCalcular.setOnClickListener {
            val numero = entradaNumero.text.toString().toIntOrNull()
            numero?.let {
                calcularPrimosEnHiloSeparado(it)
            }
        }

        botonCalcularCoroutines.setOnClickListener {
            val numero = entradaNumero.text.toString().toIntOrNull()
            numero?.let {
                calcularPrimosConCoroutines(it)
            }
        }

        botonSQLite.setOnClickListener {
            val intent = Intent(this, SQLite::class.java)
            startActivity(intent)
        }




    }

    private fun calcularPrimosConCoroutines(n: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            val primos = withContext(Dispatchers.Default) {
                calcularPrimos(n)
            }
            textoResultado.text = primos.joinToString(", ")
        }
    }


    private fun calcularPrimosEnHiloSeparado(n: Int) {

        Thread {
            val primos = calcularPrimos(n)

            handler.post {
                textoResultado.text = primos.joinToString(", ")
            }
        }.start()
    }

    private fun calcularPrimos(n: Int): List<Int> {
        val primos = mutableListOf<Int>()
        var numero = 2

        while (primos.size < n) {
            if (esPrimo(numero)) {
                primos.add(numero)
            }
            numero++
        }
        return primos
    }

    private fun esPrimo(numero: Int): Boolean {
        if (numero < 2) return false
        for (i in 2 until numero) {
            if (numero % i == 0) {
                return false
            }
        }
        return true
    }
}