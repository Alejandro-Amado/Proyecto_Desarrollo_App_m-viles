package com.example.antsave.Database

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int?=null,
    val correo: String,
    val contrasena: String
)


