package com.example.examencasa2eva

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "NotasDatabase"
        private const val DATABASE_VERSION = 1
        private const val TABLE_NOTAS = "notas"
        private const val KEY_ID = "id"
        private const val KEY_TITULO = "titulo"
        private const val KEY_CONTENIDO = "contenido"
    }


    override fun onCreate(db: SQLiteDatabase) {
        val createDiscosTable = ("CREATE TABLE " + TABLE_NOTAS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITULO + " TEXT,"
                + KEY_CONTENIDO + " TEXT" + ")")
        db.execSQL(createDiscosTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NOTAS")
        onCreate(db)
    }

    fun getAllNotas(): ArrayList<Nota> {
        val notaLista = ArrayList<Nota>()
        val selectQuery = "SELECT  * FROM $TABLE_NOTAS"

        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }

        var id: Int
        var titulo: String
        var contenido: String


        if (cursor.moveToFirst()) {
            do {
                val idIndex = cursor.getColumnIndex(KEY_ID)
                val tituloIndex = cursor.getColumnIndex(KEY_TITULO)
                val contenidoIndex = cursor.getColumnIndex(KEY_CONTENIDO)

                if (idIndex != -1 && tituloIndex != -1 && contenidoIndex != -1) {
                    id = cursor.getInt(idIndex)

                    // Verificar si los valores son NULL antes de asignarlos
                    titulo = cursor.getString(tituloIndex) ?: "Sin título"
                    contenido = cursor.getString(contenidoIndex) ?: "Sin contenido"

                    val nota = Nota(id = id, titulo = titulo, contenido = contenido)
                    notaLista.add(nota)
                }
            } while (cursor.moveToNext())
        }


        cursor.close()
        return notaLista
    }

    fun updateNota(nota: Nota): Int {
        val db = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(KEY_TITULO, nota.titulo)
        contentValues.put(KEY_CONTENIDO, nota.contenido)


        return db.update(TABLE_NOTAS, contentValues, "$KEY_ID = ?", arrayOf(nota.id.toString()))
    }

    fun deleteNota(nota: Nota): Int {
        val db = this.writableDatabase
        val success = db.delete(TABLE_NOTAS, "$KEY_ID = ?", arrayOf(nota.id.toString()))
        db.close()
        return success
    }

    fun addNota(nota: Nota): Long {
        try {
            val db = this.writableDatabase
            val contentValues = ContentValues()
            contentValues.put(KEY_TITULO, nota.titulo)
            contentValues.put(KEY_CONTENIDO, nota.contenido)

            val success = db.insert(TABLE_NOTAS, null, contentValues)
            db.close()
            return success
        } catch (e: Exception) {
            Log.e("Error", "Error al agregar la nota", e)
            return -1
        }
    }
}