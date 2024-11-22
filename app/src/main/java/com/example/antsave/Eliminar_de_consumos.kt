package com.example.antsave

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.entities.GastoEntity
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Eliminar_de_consumos : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var listView: ListView
    private lateinit var database: AppDatabase
    private lateinit var gastoAdapter: ArrayAdapter<String>
    private val gastos = mutableListOf<GastoEntity>()
    private val gastosSeleccionados = mutableSetOf<GastoEntity>()

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_de_consumos)


        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()


        listView = findViewById(R.id.listViewGastos)
        drawerLayout = findViewById(R.id.drawer_layout)
        val btnEliminarSeleccionados = findViewById<Button>(R.id.btnEliminarSeleccionados)
        val btnSalir = findViewById<Button>(R.id.btnSalir)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener(this)

        cargarGastos()

        btnEliminarSeleccionados.setOnClickListener { eliminarGastosSeleccionados() }
        btnSalir.setOnClickListener {
            val intent = Intent(this, Pagina_principal::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun cargarGastos() {
        val idUsuario = obtenerIdUsuario()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosCargados = database.daogasto.obtenerGastosPorUsuario(idUsuario)
                withContext(Dispatchers.Main) {
                    gastos.clear()
                    gastos.addAll(gastosCargados)
                    val descripcionGastos = gastos.map { "${it.descripcion} - $${it.monto}" }
                    gastoAdapter = ArrayAdapter(this@Eliminar_de_consumos, android.R.layout.simple_list_item_multiple_choice, descripcionGastos)
                    listView.adapter = gastoAdapter
                    listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

                    listView.setOnItemClickListener { _, _, position, _ ->
                        toggleSeleccion(gastos[position])
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@Eliminar_de_consumos, "Error al cargar los gastos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun toggleSeleccion(gasto: GastoEntity) {
        if (gastosSeleccionados.contains(gasto)) {
            gastosSeleccionados.remove(gasto)
        } else {
            gastosSeleccionados.add(gasto)
        }
    }

    private fun eliminarGastosSeleccionados() {
        if (gastosSeleccionados.isEmpty()) {
            Toast.makeText(this, "No has seleccionado ningún gasto", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setMessage("¿Estás seguro de que deseas eliminar los gastos seleccionados?")
            .setPositiveButton("Sí") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        gastosSeleccionados.forEach { gasto ->
                            gasto.id?.let { database.daogasto.eliminarGasto(it) }
                        }
                        withContext(Dispatchers.Main) {
                            gastos.removeAll(gastosSeleccionados)
                            gastosSeleccionados.clear()
                            actualizarLista()
                            Toast.makeText(this@Eliminar_de_consumos, "Gastos eliminados exitosamente", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Eliminar_de_consumos, "Error al eliminar los gastos", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun actualizarLista() {
        val descripcionGastos = gastos.map { "${it.descripcion} - $${it.monto}" }
        gastoAdapter.clear()
        gastoAdapter.addAll(descripcionGastos)
        gastoAdapter.notifyDataSetChanged()
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
            R.id.nav_home -> startActivity(Intent(this, Pagina_principal::class.java))
            R.id.estadisticas_consumos -> startActivity(Intent(this, Estadisticas_de_consumos::class.java))
            R.id.nav_about -> finishAffinity()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
