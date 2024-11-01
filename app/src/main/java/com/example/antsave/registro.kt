package com.example.antsave

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.UsuarioDao
import com.example.antsave.Database.UsuarioEntity
import com.example.antsave.Database.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class registro : AppCompatActivity() {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var repository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro)


        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        usuarioDao = database.daousuario
        repository = UsuarioRepository(usuarioDao)


        val espacioCuenta = findViewById<EditText>(R.id.espacio_cuenta)
        val recuadroContrasena = findViewById<EditText>(R.id.recuadro_contrasena)
        val buttonRegistro = findViewById<Button>(R.id.button1)
        val buttonSalir = findViewById<Button>(R.id.button2)

        buttonSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        buttonRegistro.setOnClickListener {
            val correo = espacioCuenta.text.toString()
            val contrasena = recuadroContrasena.text.toString()


            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                val usuario = Usuario(correo = correo, contrasena = contrasena)
                guardarUsuario(usuario)
            } else {

            }
        }
    }

    private fun guardarUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {

            val usuarioEntity = UsuarioEntity(correo = usuario.correo, contrasena = usuario.contrasena)

            repository.insertUser(usuarioEntity)

            val intent = Intent(this@registro, Pagina_principal::class.java)
            startActivity(intent)
            finish()
        }
    }

}

