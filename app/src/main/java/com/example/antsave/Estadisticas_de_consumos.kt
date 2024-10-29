package com.example.antsave

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.antsave.databinding.ActivityEstadisticasDeConsumosBinding
import com.github.mikephil.charting.components.Legend
import com.google.android.material.navigation.NavigationView

class Estadisticas_de_consumos : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityEstadisticasDeConsumosBinding
    private lateinit var pieChart: PieChart
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEstadisticasDeConsumosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener(this)
        pieChart = findViewById(R.id.pieChart)
        setupPieChart()
        setupSpinnergastos()
        val anadir = findViewById<Button>(R.id.regresar)
        anadir.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
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

            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun setupSpinnergastos() {
        val spinner: Spinner = findViewById(R.id.spinnerOpcionesgstos)
        val opciones = arrayOf(
            "Alquiler",
            "Comida",
            "Transporte",
            "Servicios",
            "Entretenimiento",
            "Ropa",
            "Salud",
            "Educación"
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    private fun setupPieChart() {

        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(10f, "Alquiler"))
        entries.add(PieEntry(30f, "Comida"))
        entries.add(PieEntry(150f, "Transporte"))
        entries.add(PieEntry(200f, "Servicios"))
        entries.add(PieEntry(100f, "Entretenimiento"))
        entries.add(PieEntry(75f, "Ropa"))
        entries.add(PieEntry(50f, "Salud"))
        entries.add(PieEntry(200f, "Educación"))

        val dataSet = PieDataSet(entries, "Categorías de Gastos")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList() // Colores predeterminados

        val pieData = PieData(dataSet)
        pieChart.data = pieData
        pieChart.setUsePercentValues(true) // Muestra los porcentajes si se desea


        // Ajustar tamaño del gráfico
        pieChart.layoutParams.height = 600 // Ajusta según sea necesario
        pieChart.invalidate()

    }
}
