package com.example.antsave.Database

import android.util.Log
import com.example.antsave.Usuario

class UsuarioRepository(private val usuarioDao: UsuarioDao) {

    suspend fun getUsers(): List<Usuario> {
        val entities = usuarioDao.getusers()
        return entities.map { Usuario(correo = it.correo, contrasena = it.contrasena) }
    }

    suspend fun insertUser(user: Usuario) {
        val entity = UsuarioEntity(correo = user.correo, contrasena = user.contrasena)
        usuarioDao.insertuser(entity)
    }
    suspend fun crearUsuarioPredeterminado() {
        try {
            val usuarios = usuarioDao.getusers()
            if (usuarios.isEmpty()) {
                val usuarioPredeterminado = UsuarioEntity(
                    correo = "usuario@ejemplo.com",
                    contrasena = "contrasena123"
                )
                usuarioDao.insertuser(usuarioPredeterminado)
                Log.d("UsuarioRepository", "Usuario predeterminado insertado.")
            } else {
                Log.d("UsuarioRepository", "Usuarios ya existentes: ${usuarios.size}")
            }
        } catch (e: Exception) {
            Log.e("UsuarioRepository", "Error al insertar usuario: ${e.message}", e)
        }
    }

}


