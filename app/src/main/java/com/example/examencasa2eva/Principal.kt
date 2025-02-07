package com.example.examencasa2eva

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class Principal : AppCompatActivity(), ComunicadorFragments {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.principal)




        val button3 = findViewById<Button>(R.id.button3)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutBoton, FragmentoBoton())
                .commit()
        }


        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayoutMapa, FragmentoMapa())
                .commit()
        }

        button3.setOnClickListener {
            val intent = Intent(this, HilosCorrutinas::class.java)
            startActivity(intent)
        }
    }

    override fun enviarDatos(datos1: Double, datos2: Double) {
        val fragmentoB = supportFragmentManager.findFragmentById(R.id.frameLayoutMapa) as? FragmentoMapa
        fragmentoB?.actualizarTexto(datos1, datos2)
    }

}
