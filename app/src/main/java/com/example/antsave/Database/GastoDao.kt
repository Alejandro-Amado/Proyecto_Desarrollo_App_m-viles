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



}
