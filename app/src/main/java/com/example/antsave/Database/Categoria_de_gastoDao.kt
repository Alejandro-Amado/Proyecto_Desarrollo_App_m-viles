package com.example.antsave.Database

import androidx.room.*
import com.example.antsave.Database.Categoria_de_gastoEntity

@Dao
interface Categoria_de_gastoDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarCategoriaGasto(categoria: Categoria_de_gastoEntity)


    @Query("SELECT * FROM Categoriagasto")
    suspend fun obtenerTodasLasCategorias(): List<Categoria_de_gastoEntity>

    // Eliminar una categoría por su ID
    @Query("DELETE FROM Categoriagasto WHERE id = :id")
    suspend fun eliminarCategoriaPorId(id: Int)


}
