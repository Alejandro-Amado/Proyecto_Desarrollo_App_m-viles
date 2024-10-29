package com.example.antsave.Database

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


}
