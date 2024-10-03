package com.example.antsave

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.antsave.databinding.ActivityPaginaPrincipalBinding
import kotlin.math.roundToInt

class Pagina_principal : AppCompatActivity() {
    private lateinit var binding: ActivityPaginaPrincipalBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.espacioGastoMensual.text = "Gasto Mensual: 500000"
        binding.espacioGastoSemanal.text = "Gasto Semanal: 120000"
        binding.espacioGastoDiario.text = "Gasto Diario: 300000"

        val button = findViewById<Button>(R.id.botonanadirgasto)
        button.setOnClickListener {
            val intent = Intent(this, Anadir_gasto::class.java)
            startActivity(intent)
        }

        val puntos = ArrayList<Bar>()
        graficar_diagrama_barras(puntos)
    }

    fun graficar_diagrama_barras(puntos: ArrayList<Bar>) {
        val gastos = arrayListOf(
            "Alquiler",
            "Comida",
            "Transporte",
            "Servicios",
            "Entretenimiento",
            "Ropa",
            "Salud",
            "Educación"
        )
        val costosGastos = arrayListOf(
            10f,
            30f,
            150f,
            200f,
            100f,
            75f,
            50f,
            200f
        )

        for (gasto in gastos.indices) {
            val barras = Bar().apply {
                name = gastos[gasto]
                value = costosGastos[gasto]
                color = Color.parseColor(generar_color_hex())
            }
            puntos.add(barras)
        }

        val graficar = findViewById<BarGraph>(R.id.graphBar)
        graficar?.bars = puntos
    }

    fun generar_color_hex(): String {
        val hexChars = "0123456789ABCDEF"
        return "#" + (1..6).map { hexChars.random() }.joinToString("")
    }
}
