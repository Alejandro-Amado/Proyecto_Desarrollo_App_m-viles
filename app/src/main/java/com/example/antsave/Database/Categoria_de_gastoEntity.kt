package com.example.antsave.Database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Categoriagasto")
data class Categoria_de_gastoEntity(
@PrimaryKey(autoGenerate = true)
val id:Int?= null,
val descripcion: String
)
