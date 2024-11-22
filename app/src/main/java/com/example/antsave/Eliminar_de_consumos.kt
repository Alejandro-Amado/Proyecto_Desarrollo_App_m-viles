package com.example.antsave

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import androidx.appcompat.app.AlertDialog
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.entities.GastoEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log

class Eliminar_de_consumos : AppCompatActivity() {

    private lateinit var listView: ListView
    private lateinit var database: AppDatabase
    private lateinit var gastoAdapter: ArrayAdapter<String>
    private val gastos = mutableListOf<GastoEntity>()
    private val gastosSeleccionados = mutableSetOf<GastoEntity>() // Set para almacenar los gastos seleccionados

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_eliminar_de_consumos)

        database = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "app_database").build()

        listView = findViewById(R.id.listViewGastos)
        val btnEliminarSeleccionados = findViewById<Button>(R.id.btnEliminarSeleccionados)


        cargarGastos()


        btnEliminarSeleccionados.setOnClickListener {
            eliminarGastosSeleccionados()
        }
    }

    private fun cargarGastos() {
        val idUsuario = obtenerIdUsuario()

        Log.d("Eliminar_de_consumos", "ID de usuario obtenido: $idUsuario")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val gastosCargados = database.daogasto.obtenerGastosPorUsuario(idUsuario)
                Log.d("Eliminar_de_consumos", "Gastos cargados: ${gastosCargados.size}")


                for (gasto in gastosCargados) {
                    Log.d("Eliminar_de_consumos", "Gasto: ${gasto.descripcion} - $${gasto.monto}")
                }

                withContext(Dispatchers.Main) {
                    if (gastosCargados.isEmpty()) {
                        Toast.makeText(this@Eliminar_de_consumos, "No tienes gastos registrados.", Toast.LENGTH_SHORT).show()
                    }
                    gastos.clear() // Limpiar la lista antes de cargar nuevos elementos
                    gastos.addAll(gastosCargados)

                    if (gastos.isEmpty()) {
                        Log.d("Eliminar_de_consumos", "La lista de gastos está vacía.")
                    }


                    val descripcionGastos = gastos.map { "${it.descripcion} - $${it.monto}" }


                    Log.d("Eliminar_de_consumos", "Descripción de gastos: $descripcionGastos")

                    gastoAdapter = ArrayAdapter(this@Eliminar_de_consumos, android.R.layout.simple_list_item_multiple_choice, descripcionGastos)
                    listView.adapter = gastoAdapter
                    listView.choiceMode = ListView.CHOICE_MODE_MULTIPLE


                    listView.setOnItemClickListener { _, _, position, _ ->
                        val gasto = gastos[position]
                        toggleSeleccion(gasto)
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

        val builder = AlertDialog.Builder(this)
        builder.setMessage("¿Estás seguro de que deseas eliminar los gastos seleccionados?")
            .setCancelable(false)
            .setPositiveButton("Sí") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        for (gasto in gastosSeleccionados) {
                            gasto.id?.let {
                                database.daogasto.eliminarGasto(it)
                            }
                        }

                        withContext(Dispatchers.Main) {
                            gastos.removeAll(gastosSeleccionados)
                            gastosSeleccionados.clear()
                            actualizarLista()
                            Toast.makeText(this@Eliminar_de_consumos, "Gastos eliminados exitosamente.", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@Eliminar_de_consumos, "Error al eliminar los gastos.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
        val alert = builder.create()
        alert.show()
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
}
