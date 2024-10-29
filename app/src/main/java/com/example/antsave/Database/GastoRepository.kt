package com.example.antsave.Database
import com.example.antsave.Database.entities.GastoEntity
import com.example.antsave.Gasto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GastoRepository(
    private val gastoDao: GastoDao,
    private val categoriaDeGastoDao: Categoria_de_gastoDao // Agregar esta línea
) {

    suspend fun getGastosPorUsuario(idUsuario: Int): List<Gasto> = withContext(Dispatchers.IO) {
        val entities = gastoDao.obtenerGastosPorUsuario(idUsuario)
        entities.map {
            Gasto(
                monto = it.monto,
                descripcion = it.descripcion,
                id_usuario = it.id_usuario,
                id_categoria = it.id_categoria,
                fecha = it.fecha

            )
        }
    }

    suspend fun insertarCategoriasPredeterminadas() = withContext(Dispatchers.IO) {
        val categorias = listOf(
            Categoria_de_gastoEntity(descripcion = "Alquiler"),
            Categoria_de_gastoEntity(descripcion = "Comida"),
            Categoria_de_gastoEntity(descripcion = "Transporte"),
            Categoria_de_gastoEntity(descripcion = "Servicios"),
            Categoria_de_gastoEntity(descripcion = "Entretenimiento"),
            Categoria_de_gastoEntity(descripcion = "Ropa"),
            Categoria_de_gastoEntity(descripcion = "Salud"),
            Categoria_de_gastoEntity(descripcion = "Educación")
        )
        // Asegúrate de que el DAO tenga un método para insertar múltiples categorías
        for (categoria in categorias) {
            categoriaDeGastoDao.insertarCategoriaGasto(categoria)
        }
    }

    suspend fun anadirGasto(gasto: Gasto) = withContext(Dispatchers.IO) {
        val entity = GastoEntity(
            monto = gasto.monto,
            descripcion = gasto.descripcion,
            id_usuario = gasto.id_usuario,
            id_categoria = gasto.id_categoria
        )
        gastoDao.insertarGasto(entity)
    }
}
