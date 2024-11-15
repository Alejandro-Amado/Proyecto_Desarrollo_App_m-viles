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
    private lateinit var categorias: List<Categoria_de_gastoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()
        binding = ActivityEstadisticasDeConsumosBinding.inflate(layoutInflater)
        setContentView(binding.root)


        pieChart = binding.pieChart
        setupActionBar()
        setupSpinners()
        val idUsuario = obtenerIdUsuario()
        setupPieChart(idUsuario)

        binding.regresar.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
            finish()
        }
    }


    private fun setupActionBar() {
        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener(this)
    }

    private fun setupSpinners() {
        CoroutineScope(Dispatchers.IO).launch {
            categorias = database.daocategoria.obtenerTodasLasCategorias()
            withContext(Dispatchers.Main) {
                setupCategoriaSpinner()
                setupTiempoSpinner()
            }
        }
    }

    private fun setupCategoriaSpinner() {
        val spinner: Spinner = binding.spinnerOpcionesgstos
        val adapterCategorias = ArrayAdapter(this, android.R.layout.simple_spinner_item, categorias.map { it.descripcion })
        adapterCategorias.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapterCategorias
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarGrafico()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun setupTiempoSpinner() {
        val spinnerOpcionesTiempo: Spinner = binding.spinnerOpcionestiempo
        val tiempoOpciones = arrayOf("Diario", "Semanal", "Mensual")
        val adapterTiempo = ArrayAdapter(this, android.R.layout.simple_spinner_item, tiempoOpciones)
        adapterTiempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerOpcionesTiempo.adapter = adapterTiempo
        spinnerOpcionesTiempo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                actualizarGrafico()
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun actualizarGrafico() {
        val categoriaSeleccionada = categorias[binding.spinnerOpcionesgstos.selectedItemPosition]
        val tiempoSeleccionado = binding.spinnerOpcionestiempo.selectedItem.toString()

        categoriaSeleccionada.id?.let { categoriaId ->
            actualizarPieChartPorCategoriaYTiempo(categoriaId, tiempoSeleccionado)
        } ?: run {
            Toast.makeText(this, "ID de categoría no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun obtenerIdUsuario(): Int {
        val sharedPreferences = getSharedPreferences("AntSavePrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1)
    }

    private fun actualizarPieChartPorCategoriaYTiempo(categoriaId: Int, tiempoSeleccionado: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosPorCategoria = when (tiempoSeleccionado) {
                    "Diario" -> database.daogasto.obtenerGastosDiariosPorCategoria(categoriaId)
                    "Semanal" -> database.daogasto.obtenerGastosSemanalesPorCategoria(categoriaId)
                    "Mensual" -> database.daogasto.obtenerGastosMensualesPorCategoria(categoriaId)
                    else -> emptyList()
                }

                withContext(Dispatchers.Main) {
                    val entries = gastosPorCategoria.map { PieEntry(it.monto.toFloat(), it.descripcion) }
                    val dataSet = PieDataSet(entries, "Gastos por Categoría").apply {
                        colors = ColorTemplate.COLORFUL_COLORS.toList()
                    }
                    pieChart.data = PieData(dataSet).apply {

                    }
                    pieChart.invalidate()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@Estadisticas_de_consumos, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupPieChart(idUsuario: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosPorCategoria = database.daogasto.obtenerGastosAgrupadosPorCategoria(idUsuario)
                withContext(Dispatchers.Main) {
                    val entries = gastosPorCategoria.map { PieEntry(it.total.toFloat(), it.categoria) }
                    val dataSet = PieDataSet(entries, "Categorías de Gastos").apply {
                        colors = ColorTemplate.COLORFUL_COLORS.toList()
                    }


                    pieChart.apply {
                        data = PieData(dataSet)
                        setUsePercentValues(true)
                        isDrawHoleEnabled = false
                        setHoleColor(Color.TRANSPARENT)
                        setTransparentCircleAlpha(0)
                        setCenterText("")
                        invalidate()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this@Estadisticas_de_consumos, "Error al cargar los datos", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            R.id.nav_about -> {
                finishAffinity()
            }
            R.id.estadisticas_consumos -> {
                // No action needed
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

}
