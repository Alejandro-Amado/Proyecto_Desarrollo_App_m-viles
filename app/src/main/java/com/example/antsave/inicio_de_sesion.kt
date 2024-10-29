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
import kotlinx.coroutines.withContext

class inicio_de_sesion : AppCompatActivity() {

    private lateinit var usuarioDao: UsuarioDao
    private lateinit var repository: UsuarioRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio_de_sesion)

        // Inicializar Room Database
        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "app_database"
        ).build()

        usuarioDao = database.daousuario
        repository = UsuarioRepository(usuarioDao)

        // Encuentra los EditTexts y botones
        val espacioCuenta = findViewById<EditText>(R.id.espacio_cuenta) // Asegúrate de que este EditText esté en el XML
        val recuadroContrasena = findViewById<EditText>(R.id.recuadro_contrasena) // Asegúrate de que este EditText esté en el XML
        val button = findViewById<Button>(R.id.iniciosesion)
        val buttonsalir = findViewById<Button>(R.id.button2)

        // Configurar el botón de iniciar sesión
        button.setOnClickListener {
            val correo = espacioCuenta.text.toString()
            val contrasena = recuadroContrasena.text.toString()

            if (correo.isNotEmpty() && contrasena.isNotEmpty()) {
                verificarUsuario(correo, contrasena)
            } else {
                // Mostrar mensaje de error si los campos están vacíos
                // Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

        // Configurar el botón de salir
        buttonsalir.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verificarUsuario(correo: String, contrasena: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val usuarios = repository.getUsers()
            val usuarioEncontrado = usuarios.find { it.correo == correo && it.contrasena == contrasena }

            withContext(Dispatchers.Main) {
                if (usuarioEncontrado != null) {
                    // Credenciales correctas, navegar a la página principal
                    val intent = Intent(this@inicio_de_sesion, Pagina_principal::class.java)
                    startActivity(intent)
                    finish() // Cerrar esta actividad
                } else {
                    // Mostrar mensaje de error si las credenciales son incorrectas
                    // Toast.makeText(this@inicio_de_sesion, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
