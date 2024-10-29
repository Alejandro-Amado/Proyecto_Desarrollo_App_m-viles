package com.example.antsave.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.antsave.Database.entities.GastoEntity

@Dao
interface GastoDao {

    @Insert
    fun insertarGasto(gasto: GastoEntity)


    @Query("""
        INSERT INTO gasto (monto, descripcion, id_usuario, fecha)
        VALUES (:monto, :descripcion, :idUsuario, datetime('now'))
    """)
    fun insertarGastoConFecha(monto: Double, descripcion: String, idUsuario: Int)

    @Query("SELECT * FROM gasto WHERE id_usuario = :idUsuario ORDER BY fecha DESC")
    fun obtenerGastosPorUsuario(idUsuario: Int): List<GastoEntity>
}
