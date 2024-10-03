package com.example.antsave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_iniciar_sesion = findViewById<Button>(R.id.button1)
        button_iniciar_sesion.setOnClickListener {
            val intent = Intent(this, inicio_de_sesion::class.java)
            startActivity(intent)
        }
        val registrarse = findViewById<Button>(R.id.button2)
        registrarse.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
    }
}
