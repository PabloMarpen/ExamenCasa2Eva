package com.example.examencasa2eva

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HilosCorrutinas : AppCompatActivity() {

    private lateinit var textoResultado: TextView
    private lateinit var progresoCalculo: ProgressBar
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.hilos_corrutinas)

         progresoCalculo = findViewById(R.id.progresoCalculo)
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
            numero?.let { n ->
                calcularPrimosConCoroutines(n)
            }
        }

        botonSQLite.setOnClickListener {
            val intent = Intent(this, SQLite::class.java)
            startActivity(intent)
        }




    }

    private fun calcularPrimosConCoroutines(n: Int) {
        progresoCalculo.visibility = android.view.View.VISIBLE
        progresoCalculo.max = n //Se fija el máximo en la progress bar


        CoroutineScope(Dispatchers.Main).launch {
            val primos = withContext(Dispatchers.Default) {
                calcularPrimosCarga(n) { progreso ->
                    launch(Dispatchers.Main) {
                        progresoCalculo.progress = progreso
                    }
                }
            }
            textoResultado.text = primos.joinToString(", ")
            progresoCalculo.visibility = android.view.View.INVISIBLE
        }
    }

    private fun calcularPrimosEnHiloSeparado(n: Int) {

        Thread {
            val primos = calcularPrimosNormal(n)

            handler.post {
                textoResultado.text = primos.joinToString(", ")
            }
        }.start()
    }

    private fun calcularPrimosCarga(n: Int, actualizarProgreso: (Int) -> Unit): List<Int> {
        val primos = mutableListOf<Int>()
        var numero = 2
        while (primos.size < n) {
            if (esPrimo(numero)) {
                primos.add(numero)
                actualizarProgreso(primos.size)
            }
            numero++
        }
        return primos
    }

    private fun calcularPrimosNormal(n: Int): List<Int> {
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