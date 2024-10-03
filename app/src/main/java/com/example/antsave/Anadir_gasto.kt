package com.example.antsave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner

class Anadir_gasto : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_anadir_gasto)
        setupSpinner()
        val anadir = findViewById<Button>(R.id.anadirgasto)
        anadir.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
        val back = findViewById<Button>(R.id.atras)
        back.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }


    }
    private fun setupSpinner() {
        val spinner: Spinner = findViewById(R.id.spinnerOpciones)
        val opciones = arrayOf("Alquiler",
            "Comida",
            "Transporte",
            "Servicios",
            "Entretenimiento",
            "Ropa",
            "Salud",
            "Educación")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }
}