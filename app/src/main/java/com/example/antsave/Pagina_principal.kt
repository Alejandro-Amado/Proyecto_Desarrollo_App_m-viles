package com.example.antsave

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.echo.holographlibrary.Bar
import com.echo.holographlibrary.BarGraph
import com.example.antsave.databinding.ActivityPaginaPrincipalBinding
import com.google.android.material.navigation.NavigationView

class Pagina_principal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var binding: ActivityPaginaPrincipalBinding
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inflate el binding para conectar la vista
        binding = ActivityPaginaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurar DrawerLayout y ActionBarToggle
        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        binding.navigationView.setNavigationItemSelectedListener(this)


        binding.espacioGastoMensual.text = "Gasto Mensual: 500000"
        binding.espacioGastoSemanal.text = "Gasto Semanal: 120000"
        binding.espacioGastoDiario.text = "Gasto Diario: 300000"


        binding.botonanadirgasto.setOnClickListener {
            val intent = Intent(this, Anadir_gasto::class.java)
            startActivity(intent)
        }


        val puntos = ArrayList<Bar>()
        graficar_diagrama_barras(puntos)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.nav_about -> {
                finishAffinity()
            }
            R.id.estadisticas_consumos -> {
                val intent = Intent(this, Estadisticas_de_consumos::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun graficar_diagrama_barras(puntos: ArrayList<Bar>) {
        val gastos = listOf("Alquiler", "Comida", "Transporte", "Servicios",
            "Entretenimiento", "Ropa", "Salud", "Educación")
        val costosGastos = listOf(10f, 30f, 150f, 200f, 100f, 75f, 50f, 200f)

        val contenedorLeyendas = binding.contenedorLeyendas
        contenedorLeyendas.removeAllViews()

        for (i in gastos.indices) {
            val colorBarra = Color.parseColor(generar_color_hex())
            val barra = Bar().apply {
                name = " "
                value = costosGastos[i]
                color = colorBarra
            }
            puntos.add(barra)


            val leyendaView = crearLeyendaView(gastos[i], colorBarra)
            contenedorLeyendas.addView(leyendaView)
        }

        binding.graphBar.bars = puntos
    }


    private fun crearLeyendaView(texto: String, color: Int): LinearLayout {
        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { setMargins(8, 8, 8, 8) }
        }

        val colorView = View(this).apply {
            layoutParams = LinearLayout.LayoutParams(50, 50).apply {
                setMargins(8, 0, 16, 0)
            }
            setBackgroundColor(color)
        }

        val textView = TextView(this).apply {
            text = texto
            textSize = 18f
            setTextColor(Color.BLACK)
        }

        layout.addView(colorView)
        layout.addView(textView)
        return layout
    }


    private fun generar_color_hex(): String {
        val hexChars = "0123456789ABCDEF"
        return "#" + (1..6).map { hexChars.random() }.joinToString("")
    }
}
