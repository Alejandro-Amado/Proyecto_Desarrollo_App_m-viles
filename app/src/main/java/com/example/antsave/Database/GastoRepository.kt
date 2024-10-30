package com.example.antsave.Database
import com.example.antsave.Database.entities.GastoEntity
import com.example.antsave.Gasto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GastoRepository(
    private val gastoDao: GastoDao,
    private val categoriaDeGastoDao: Categoria_de_gastoDao
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
        for (categoria in categorias) {
            categoriaDeGastoDao.insertarCategoriaGasto(categoria)
        }
    }

    suspend fun insertarGastosPredeterminados() = withContext(Dispatchers.IO) {
        val gastos = listOf(
            GastoEntity(monto = 500.0, descripcion = "Alquiler de octubre", id_usuario = 1, id_categoria = 1),
            GastoEntity(monto = 150.0, descripcion = "Supermercado", id_usuario = 1, id_categoria = 2),
            GastoEntity(monto = 20.0, descripcion = "Transporte público", id_usuario = 1, id_categoria = 3),
            GastoEntity(monto = 75.0, descripcion = "Servicios de luz", id_usuario = 1, id_categoria = 4),
            GastoEntity(monto = 50.0, descripcion = "Cine", id_usuario = 1, id_categoria = 5),
            GastoEntity(monto = 100.0, descripcion = "Ropa de invierno", id_usuario = 1, id_categoria = 6),
            GastoEntity(monto = 200.0, descripcion = "Medicamentos", id_usuario = 1, id_categoria = 7),
            GastoEntity(monto = 300.0, descripcion = "Libros de texto", id_usuario = 1, id_categoria = 8),
            GastoEntity(monto = 120.0, descripcion = "Comida rápida", id_usuario = 1, id_categoria = 2),
            GastoEntity(monto = 60.0, descripcion = "Gasolina", id_usuario = 1, id_categoria = 3),
            GastoEntity(monto = 45.0, descripcion = "Suscripción a Netflix", id_usuario = 1, id_categoria = 5),
            GastoEntity(monto = 80.0, descripcion = "Compra de medicinas", id_usuario = 1, id_categoria = 7),
            GastoEntity(monto = 220.0, descripcion = "Mantenimiento del coche", id_usuario = 1, id_categoria = 3),
            GastoEntity(monto = 90.0, descripcion = "Cena en restaurante", id_usuario = 1, id_categoria = 5),
            GastoEntity(monto = 40.0, descripcion = "Clases particulares", id_usuario = 1, id_categoria = 8),
            GastoEntity(monto = 15.0, descripcion = "Café de la mañana", id_usuario = 1, id_categoria = 2),
            GastoEntity(monto = 500.0, descripcion = "Vacaciones en la playa", id_usuario = 1, id_categoria = 6)
        )

        for (gasto in gastos) {
            gastoDao.insertarGasto(gasto)
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
