package com.example.antsave.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertuser(usuario: UsuarioEntity)

    @Query("SELECT * FROM usuario")
    suspend fun getusers(): List<UsuarioEntity>


}
