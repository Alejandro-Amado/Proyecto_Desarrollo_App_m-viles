package com.example.antsave

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.entities.GastoEntity
import com.example.antsave.databinding.ActivityAnadirGastoBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import java.time.LocalDate

class Anadir_gasto : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityAnadirGastoBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnadirGastoBinding.inflate(layoutInflater)
        setContentView(binding.root)


        toggle = ActionBarDrawerToggle(
            this, binding.drawerLayout, R.string.open_drawer, R.string.close_drawer
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.navigationView.setNavigationItemSelectedListener(this)

        // Initialize the database
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()

        // Set up the spinner with categories
        setupSpinner()

        // Handle "Add expense" button click
        binding.anadirgasto.setOnClickListener {
            agregarGasto()
        }

        // Handle "Back" button click
        binding.atras.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
    }

    private fun setupSpinner() {
        lifecycleScope.launch {
            try {
                val categorias = database.daocategoria.obtenerTodasLasCategorias()

                if (categorias.isNotEmpty()) {
                    val nombresCategorias = categorias.map { it.descripcion }
                    val adapter = ArrayAdapter(this@Anadir_gasto, android.R.layout.simple_spinner_item, nombresCategorias)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    binding.spinnerOpciones.adapter = adapter
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Anadir_gasto, "No hay categorías disponibles", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@Anadir_gasto, "Error al cargar las categorías", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun agregarGasto() {
        val nombreGasto = binding.espacionombredelgasto.text.toString()
        val montoGasto = binding.espaciodinerogastado.text.toString().toDoubleOrNull()
        val categoriaSeleccionada = binding.spinnerOpciones.selectedItem?.toString()

        if (nombreGasto.isBlank() || montoGasto == null || categoriaSeleccionada.isNullOrBlank()) {
            Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val categoriaId = database.daocategoria.obtenerTodasLasCategorias()
                    .firstOrNull { it.descripcion == categoriaSeleccionada }?.id

                if (categoriaId != null) {
                    val gasto = GastoEntity(
                        monto = montoGasto,
                        descripcion = nombreGasto,
                        id_usuario = obtenerIdUsuario(),
                        id_categoria = categoriaId,
                        fecha = LocalDate.now().toString()
                    )

                    database.daogasto.insertarGasto(gasto)
                    runOnUiThread {
                        Toast.makeText(this@Anadir_gasto, "Gasto añadido exitosamente.", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@Anadir_gasto, Pagina_principal::class.java))
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(this@Anadir_gasto, "Categoría no encontrada", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@Anadir_gasto, "Error al agregar el gasto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun obtenerIdUsuario(): Int {
        val sharedPreferences = getSharedPreferences("AntSavePrefs", MODE_PRIVATE)
        return sharedPreferences.getInt("userId", -1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) true
        else super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                startActivity(Intent(this, Pagina_principal::class.java))
                finish()
            }
            R.id.nav_about -> {
                finishAffinity()
            }
            R.id.estadisticas_consumos -> {
                startActivity(Intent(this, Estadisticas_de_consumos::class.java))
                finish()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
