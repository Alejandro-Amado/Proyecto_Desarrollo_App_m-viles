package com.example.antsave.Database
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.antsave.Database.*

import com.example.antsave.Database.entities.GastoEntity

@Database(entities = [UsuarioEntity::class, GastoEntity::class, Categoria_de_gastoEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
 abstract val daousuario: UsuarioDao
 abstract val daogasto: GastoDao
 abstract val daocategoria: Categoria_de_gastoDao

 companion object {
  @Volatile
  private var INSTANCE: AppDatabase? = null

  fun getInstance(context: Context): AppDatabase {
   return INSTANCE ?: synchronized(this) {
    val instance = Room.databaseBuilder(
     context.applicationContext,
     AppDatabase::class.java,
     "antSave_database"
    ).build()
    INSTANCE = instance
    instance
   }
  }
 }
}


