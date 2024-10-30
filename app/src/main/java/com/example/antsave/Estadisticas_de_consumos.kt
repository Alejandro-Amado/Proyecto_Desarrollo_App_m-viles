package com.example.antsave
import com.example.antsave.Database.AppDatabase
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.antsave.Database.Categoria_de_gastoEntity
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.example.antsave.databinding.ActivityEstadisticasDeConsumosBinding
import com.github.mikephil.charting.components.Legend
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Estadisticas_de_consumos : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityEstadisticasDeConsumosBinding
    private lateinit var pieChart: PieChart
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: AppDatabase
    private lateinit var Categorias: List<Categoria_de_gastoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

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
        val idUsuario = 1
        setupPieChart(idUsuario)
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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Categorias = database.daocategoria.obtenerTodasLasCategorias()

                withContext(Dispatchers.Main) {
                    val adapter = ArrayAdapter(
                        this@Estadisticas_de_consumos,
                        android.R.layout.simple_spinner_item,
                        Categorias.map { it.descripcion } // Mostrar solo las descripciones
                    )
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = adapter
                    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            parent: AdapterView<*>,
                            view: View?,
                            position: Int,
                            id: Long
                        ) {
                            val categoriaSeleccionada = Categorias[position]
                            println("Categoría seleccionada: ${categoriaSeleccionada.descripcion}, ID: ${categoriaSeleccionada.id}")
                        }

                        override fun onNothingSelected(parent: AdapterView<*>) {
                            // Lógica opcional si no se selecciona nada
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    private fun setupPieChart(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosPorCategoria = database.daogasto.obtenerGastosAgrupadosPorCategoria(idUsuario)

                withContext(Dispatchers.Main) {
                    val entries = ArrayList<PieEntry>()
                    gastosPorCategoria.forEach { gasto ->
                        entries.add(PieEntry(gasto.total.toFloat(), gasto.categoria))
                    }

                    val dataSet = PieDataSet(entries, "Categorías de Gastos")
                    dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

                    val pieData = PieData(dataSet)
                    pieChart.data = pieData
                    pieChart.setUsePercentValues(true) // Muestra los porcentajes

                    pieChart.invalidate() // Refresca el gráfico
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}
