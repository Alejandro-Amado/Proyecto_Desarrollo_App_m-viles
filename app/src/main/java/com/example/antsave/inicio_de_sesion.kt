package com.example.antsave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class inicio_de_sesion : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion)
        val button = findViewById<Button>(R.id.iniciosesion)
        button.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
        val buttonsalir = findViewById<Button>(R.id.button2)
        buttonsalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}