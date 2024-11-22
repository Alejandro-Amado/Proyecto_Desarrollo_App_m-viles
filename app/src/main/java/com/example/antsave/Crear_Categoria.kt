package com.example.antsave

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.Categoria_de_gastoEntity
import com.example.antsave.databinding.ActivityCrearCategoriaBinding
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.launch
import androidx.lifecycle.lifecycleScope

class Crear_Categoria : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityCrearCategoriaBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var database: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearCategoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


        binding.anadircategoria.setOnClickListener() {
            crearCategoria()
        }


        binding.atras.setOnClickListener() {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
        }
    }

    private fun crearCategoria() {
        val nombreCategoria = binding.espacionombrecategoria.text.toString()

        if (nombreCategoria.isBlank()) {
            Toast.makeText(this, "Por favor, ingresa el nombre de la categoría.", Toast.LENGTH_SHORT).show()
            return
        }

        lifecycleScope.launch {
            try {
                val categoria = Categoria_de_gastoEntity(descripcion = nombreCategoria)
                database.daocategoria.insertarCategoriaGasto(categoria)

                runOnUiThread {
                    Toast.makeText(this@Crear_Categoria, "Categoría creada exitosamente.", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Crear_Categoria, Pagina_principal::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    Toast.makeText(this@Crear_Categoria, "Error al crear la categoría.", Toast.LENGTH_SHORT).show()
                }
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