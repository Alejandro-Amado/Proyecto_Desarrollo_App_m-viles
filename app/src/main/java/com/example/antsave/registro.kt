package com.example.antsave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class registro : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)
        val buttonsalir = findViewById<Button>(R.id.button2)
        buttonsalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val buttonregistro = findViewById<Button>(R.id.button1)
        buttonregistro.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
    }
}