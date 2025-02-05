package com.example.examencasa2eva

import android.os.Bundle
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        var nombre = findViewById<EditText>(R.id.editTextText)
        var contrasena = findViewById<EditText>(R.id.editTextText2)

        if (nombre.text.equals("pablo") || contrasena.text.equals("12345")){

        }

    }
}