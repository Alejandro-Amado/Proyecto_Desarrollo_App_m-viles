package com.example.antsave.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.antsave.Database.entities.GastoEntity


@Database (entities = [UsuarioEntity::class,GastoEntity::class,Categoria_de_gastoEntity::class], version =2)
abstract class AppDatabase: RoomDatabase() {
 abstract val daousuario: UsuarioDao
 abstract val daogasto: GastoDao
 abstract val daocategoria: Categoria_de_gastoDao
}