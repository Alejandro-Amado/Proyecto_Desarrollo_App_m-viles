package com.example.antsave.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.antsave.Database.entities.GastoEntity
import com.example.antsave.GastoPorCategoria


@Dao
interface GastoDao {

    @Insert
    suspend fun insertarGasto(gasto: GastoEntity)

    @Query("DELETE FROM gasto WHERE id = :gastoId")
    suspend fun eliminarGasto(gastoId: Int)

    @Query("""
        INSERT INTO gasto (monto, descripcion, id_usuario, fecha)
        VALUES (:monto, :descripcion, :idUsuario, datetime('now'))
    """)
    suspend fun insertarGastoConFecha(monto: Double, descripcion: String, idUsuario: Int)

    @Query("SELECT * FROM gasto WHERE id_usuario = :idUsuario ORDER BY fecha DESC")
    suspend fun obtenerGastosPorUsuario(idUsuario: Int): List<GastoEntity>

    @Query("""
        SELECT c.descripcion AS categoria, SUM(g.monto) AS total
        FROM gasto AS g
        INNER JOIN Categoriagasto AS c ON g.id_categoria = c.id
        WHERE g.id_usuario = :idUsuario
        GROUP BY c.descripcion
    """)
    suspend fun obtenerGastosAgrupadosPorCategoria(idUsuario: Int): List<GastoPorCategoria>


    @Query("SELECT * FROM gasto WHERE id_categoria = :categoriaId AND fecha >= date('now', 'start of day')")
    suspend fun obtenerGastosDiariosPorCategoria(categoriaId: Int): List<GastoEntity>

    @Query("SELECT * FROM gasto WHERE id_categoria = :categoriaId AND fecha >= date('now', '-7 days')")
    suspend fun obtenerGastosSemanalesPorCategoria(categoriaId: Int): List<GastoEntity>

    @Query("SELECT * FROM gasto WHERE id_categoria = :categoriaId AND fecha >= date('now', 'start of month')")
    suspend fun obtenerGastosMensualesPorCategoria(categoriaId: Int): List<GastoEntity>


    @Query("SELECT SUM(monto) FROM gasto WHERE id_usuario = :idUsuario AND fecha >= date('now', 'start of day')")
    suspend fun obtenerTotalGastosDiarios(idUsuario: Int): Double

    @Query("SELECT SUM(monto) FROM gasto WHERE id_usuario = :idUsuario AND fecha >= date('now', '-7 days')")
    suspend fun obtenerTotalGastosSemanales(idUsuario: Int): Double

    @Query("SELECT SUM(monto) FROM gasto WHERE id_usuario = :idUsuario AND fecha >= date('now', 'start of month')")
    suspend fun obtenerTotalGastosMensuales(idUsuario: Int): Double
}
