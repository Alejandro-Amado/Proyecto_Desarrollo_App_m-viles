package com.example.antsave

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.antsave.Database.AppDatabase
import com.example.antsave.Database.UsuarioDao
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

        // Inicializar Room Database
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        usuarioDao = database.daousuario
        repository = UsuarioRepository(usuarioDao)

        // Encuentra los EditTexts y botones
        val espacioCuenta = findViewById<EditText>(R.id.espacio_cuenta)
        val recuadroContrasena = findViewById<EditText>(R.id.recuadro_contrasena)
        val buttonRegistro = findViewById<Button>(R.id.button1)
        val buttonSalir = findViewById<Button>(R.id.button2)

        // Configurar el botón de salir
        buttonSalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Configurar el botón de registro
        buttonRegistro.setOnClickListener {
            val correo = espacioCuenta.text.toString()
            val contrasena = recuadroContrasena.text.toString()

            // Validar que el usuario no esté vacío
            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                val usuario = Usuario(correo = correo, contrasena = contrasena)
                guardarUsuario(usuario)
            } else {

            }
        }
    }

    private fun guardarUsuario(usuario: Usuario) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.insertUser(usuario)
            // Aquí puedes navegar a otra Activity o mostrar un mensaje de éxito
            val intent = Intent(this@registro, Pagina_principal::class.java)
            startActivity(intent)
            finish() // Cierra la actividad actual
        }
    }
}
