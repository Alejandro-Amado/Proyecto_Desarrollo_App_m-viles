package com.example.antsave.Database

import com.example.antsave.Gasto
import com.example.antsave.Usuario
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifBase(
    private val usuarioRepository: UsuarioRepository,
    private val gastoRepository: GastoRepository,
    private val categoriaRepository: CategoriaDeGastoRepository
) {


    fun guardarUsuario(usuario: UsuarioEntity) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                usuarioRepository.insertUser(usuario)
                withContext(Dispatchers.Main) {
                    println("Usuario guardado con éxito")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    println("Error al guardar usuario: ${e.message}")
                }
            }
        }
    }


    fun guardarGasto(gasto: Gasto) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                gastoRepository.anadirGasto(gasto)
                withContext(Dispatchers.Main) {
                    println("Gasto guardado con éxito")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    println("Error al guardar gasto: ${e.message}")
                }
            }
        }
    }
}
