package com.example.antsave.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UsuarioDao {
    @Insert
    suspend fun insertuser(usuarioEntity: UsuarioEntity)

    @Query("SELECT * FROM usuario")
    suspend fun getusers(): List<UsuarioEntity>
}
