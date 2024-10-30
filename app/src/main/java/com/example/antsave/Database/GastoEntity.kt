package com.example.antsave.Database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.antsave.Database.UsuarioEntity
import com.example.antsave.Database.Categoria_de_gastoEntity
import java.time.LocalDate

@Entity(
    tableName = "gasto",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_usuario"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Categoria_de_gastoEntity::class,
            parentColumns = ["id"],
            childColumns = ["id_categoria"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["id_usuario"]),
        Index(value = ["id_categoria"])
    ]
)
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val monto: Double,
    val descripcion: String,
    val id_usuario: Int,
    val id_categoria: Int,
    val fecha: String = LocalDate.now().toString()
)
