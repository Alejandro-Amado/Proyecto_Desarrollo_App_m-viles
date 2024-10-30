package com.example.antsave

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.CategoriaDeGastoRepository
import com.example.antsave.Database.GastoRepository
import com.example.antsave.Database.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private lateinit var db: AppDatabase
private lateinit var usuarioRepository: UsuarioRepository
private lateinit var gastoRepository: GastoRepository
private lateinit var categoriaRepository: CategoriaDeGastoRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val button_iniciar_sesion = findViewById<Button>(R.id.button1)
        button_iniciar_sesion.setOnClickListener {
            val intent = Intent(this, inicio_de_sesion::class.java)
            startActivity(intent)
        }
        val registrarse = findViewById<Button>(R.id.button2)
        registrarse.setOnClickListener {
            val intent = Intent(this, registro::class.java)
            startActivity(intent)
        }
        val app = application as AntSaveApplication
        usuarioRepository = UsuarioRepository(app.database.daousuario)
        categoriaRepository= CategoriaDeGastoRepository(app.database.daocategoria)
        gastoRepository = GastoRepository(app.database.daogasto,app.database.daocategoria)

        CoroutineScope(Dispatchers.IO).launch {
            usuarioRepository.crearUsuarioPredeterminado()
            gastoRepository.insertarCategoriasPredeterminadas()
            gastoRepository.insertarGastosPredeterminados()
        }
    }
}
