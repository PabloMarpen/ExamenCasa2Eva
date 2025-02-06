package com.example.examencasa2eva

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val nombre = findViewById<EditText>(R.id.editTextText)
        val contrasena = findViewById<EditText>(R.id.editTextText2)
        val boton = findViewById<Button>(R.id.button)


        boton.setOnClickListener{

            if (nombre.text.toString().trim() == "Pablo" && contrasena.text.toString().trim() == "12345"){
                intent = Intent(this, Principal::class.java)
                startActivity(intent)
                Toast.makeText(this, "Inicio de sesión", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this, "Error de inicio de sesión", Toast.LENGTH_SHORT).show()
            }
        }


    }
}