package com.example.antsave.Database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.antsave.Database.UsuarioEntity
import com.example.antsave.Database.Categoria_de_gastoEntity // Asegúrate de importar la entidad correcta

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
    ]
)
data class GastoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val monto: Double,
    val descripcion: String,
    val id_usuario: Int,
    val id_categoria: Int,
    val fecha: String = ""
)
