package com.example.antsave.Database

import com.example.antsave.Categoria
import com.example.antsave.Database.Categoria_de_gastoEntity

class CategoriaDeGastoRepository(private val categoriaDeGastoDao: Categoria_de_gastoDao) {

    suspend fun getCategorias(): List<Categoria> {
        val entities = categoriaDeGastoDao.obtenerTodasLasCategorias()
        return entities.map { Categoria(descripcion = it.descripcion) }
    }

    suspend fun insertCategoria(categoria: Categoria) {
        val entity = Categoria_de_gastoEntity(descripcion = categoria.descripcion)
        categoriaDeGastoDao.insertarCategoriaGasto(entity)
    }

    suspend fun deleteCategoriaById(id: Int) {
        categoriaDeGastoDao.eliminarCategoriaPorId(id)
    }

}