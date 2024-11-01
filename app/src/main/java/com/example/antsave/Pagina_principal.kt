package com.example.antsave

import MyValueFormatter
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.Categoria_de_gastoEntity
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.google.android.material.navigation.NavigationView
import com.example.antsave.databinding.ActivityPaginaPrincipalBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Pagina_principal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityPaginaPrincipalBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: AppDatabase
    private lateinit var barChart: BarChart
    private lateinit var categorias: List<Categoria_de_gastoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaginaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val idusuario: Int = obtenerIdUsuario()
        if (idusuario == -1) {

            val intent = Intent(this, inicio_de_sesion::class.java)
            startActivity(intent)

            return
        }

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener(this)

        barChart = binding.barChart
        setupBarChart(idusuario)
    }

    private fun obtenerIdUsuario(): Int {
        val sharedPreferences = getSharedPreferences("AntSavePrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1)
    }

    private fun setupBarChart(iduser:Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosPorCategoria = database.daogasto.obtenerGastosAgrupadosPorCategoria(iduser)

                val entries = ArrayList<BarEntry>()
                val labels = ArrayList<String>()

                gastosPorCategoria.forEachIndexed { index, gasto ->
                    entries.add(BarEntry(index.toFloat(), gasto.total.toFloat()))
                    labels.add(gasto.categoria)
                }

                val dataSet = BarDataSet(entries, "Gastos por Categoría")
                dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()
                val barData = BarData(dataSet)
                runOnUiThread {
                    barChart.data = barData
                    barChart.invalidate()
                    barChart.xAxis.valueFormatter = MyValueFormatter(labels)
                    barChart.xAxis.granularity = 1f


                    binding.contenedorLeyendas.removeAllViews()
                    labels.forEachIndexed { index, label ->

                        val legendLayout = LinearLayout(this@Pagina_principal).apply {
                            orientation = LinearLayout.HORIZONTAL
                            gravity = Gravity.CENTER_VERTICAL
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            )
                        }


                        val colorBox = View(this@Pagina_principal).apply {
                            setBackgroundColor(dataSet.colors[index % dataSet.colors.size])
                            layoutParams = LinearLayout.LayoutParams(20, 20)
                        }


                        val legendText = TextView(this@Pagina_principal).apply {
                            text = "$label: ${gastosPorCategoria[index].total}"
                            setTextColor(Color.BLACK)
                            textSize = 16f
                            layoutParams = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                marginStart = 8
                            }
                        }


                        legendLayout.addView(colorBox)
                        legendLayout.addView(legendText)


                        binding.contenedorLeyendas.addView(legendLayout)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
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
                val intent = Intent(this, Estadisticas_de_consumos::class.java)
                startActivity(intent)
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
