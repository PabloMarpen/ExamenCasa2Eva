package com.example.examencasa2eva

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class SQLite : AppCompatActivity() {

    private lateinit var textID: EditText
    private lateinit var textTitulo: EditText
    private lateinit var textContenido: EditText
    private lateinit var buttonInsertar: Button
    private lateinit var buttonBorrar: Button
    private lateinit var buttonEditar: Button
    private lateinit var dbHandler: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private val REQUEST_CODE_PERMISSIONS = 101

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
        val BotonNoti = findViewById<Button>(R.id.button9)

        recyclerView.layoutManager = LinearLayoutManager(this) // 🚀 Configurar layoutManager
        viewDiscos()

        buttonInsertar.setOnClickListener { addDisco() }
        buttonBorrar.setOnClickListener { borrarDisco() }
        buttonEditar.setOnClickListener { actualizarDisco() }
        BotonNoti.setOnClickListener {
            if (checkPermissions()) {
                createNotificationChannel()
                // Si ya tiene permisos, mostrar notificación
                showNotification("¡Hola!", "Esta es una notificación instantánea")
                scheduleNotification() // Programar la segunda notificación
            } else {
                // Si no tiene permisos, pedirlos
                requestPermissions()
            }

        }


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


    // Función para mostrar una notificación inmediata
    private fun showNotification(title: String, message: String) {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val builder = NotificationCompat.Builder(this, "canal_notificaciones")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Icono de la notificación
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // La notificación desaparece al tocarla

        notificationManager.notify(1, builder.build()) // ID de la notificación
    }

    // Función para programar una notificación con WorkManager
    private fun scheduleNotification() {
        val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
            .setInitialDelay(1, TimeUnit.MINUTES) // Espera 1 minuto
            .build()

        WorkManager.getInstance(this).enqueue(workRequest)
    }

    // Crear el canal de notificación (obligatorio en Android 8.0+)
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "canal_notificaciones",
                "Canal de Notificaciones",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Canal para mostrar notificaciones"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Verifica si los permisos necesarios están concedidos
    private fun checkPermissions(): Boolean {
        val notificationPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        } else {
            true // No es necesario pedir permiso en versiones anteriores
        }
        return notificationPermission
    }

    // Solicita permisos en tiempo de ejecución
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    // Maneja la respuesta del usuario cuando acepta o niega permisos
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permiso concedido, mostrar notificación
                showNotification("¡Permiso concedido!", "Ahora recibirás notificaciones.")
                scheduleNotification() // Programar la segunda notificación
            } else {
                // Permiso denegado, mostrar mensaje
                showNotification("Permiso denegado", "No puedes recibir notificaciones.")
            }
        }
    }
}