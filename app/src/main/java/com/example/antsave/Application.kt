package com.example.antsave

import android.app.Application
import androidx.room.Room
import com.example.antsave.Database.AppDatabase

class AntSaveApplication : Application() {
    lateinit var database: AppDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }
}
