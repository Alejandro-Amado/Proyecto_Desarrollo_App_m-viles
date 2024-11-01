package com.example.antsave

import com.example.antsave.Database.AppDatabase
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.antsave.Database.UsuarioDao
import com.example.antsave.Database.UsuarioRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class inicio_de_sesion : AppCompatActivity() {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var repository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        usuarioDao = database.daousuario
        repository = UsuarioRepository(usuarioDao)


        val espacioCuenta =
            findViewById<EditText>(R.id.espacio_cuenta)
        val recuadroContrasena =
            findViewById<EditText>(R.id.recuadro_contrasena)
        val button = findViewById<Button>(R.id.iniciosesion)
        val buttonsalir = findViewById<Button>(R.id.button2)


        button.setOnClickListener {
            val correo = espacioCuenta.text.toString()
            val contrasena = recuadroContrasena.text.toString()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                verificarUsuario(correo, contrasena)
            } else {

               Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }


        buttonsalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verificarUsuario(correo: String, contrasena: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val usuarios = repository.getUsers()
            val usuarioEncontrado =
                usuarios.find { it.correo == correo && it.contrasena == contrasena }

            withContext(Dispatchers.Main) {
                if (usuarioEncontrado != null) {

                    guardarIdUsuario(
                        usuarioEncontrado.id ?: 0
                    )
                    val intent = Intent(this@inicio_de_sesion, Pagina_principal::class.java)
                    startActivity(intent)

                } else {

                    Toast.makeText(this@inicio_de_sesion, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun guardarIdUsuario(id: Int) {
        val sharedPreferences = getSharedPreferences("AntSavePrefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt("userId", id)
        editor.apply()
    }
}
