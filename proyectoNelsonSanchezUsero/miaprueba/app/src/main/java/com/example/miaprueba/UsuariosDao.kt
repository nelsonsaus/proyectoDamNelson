package com.example.miaprueba

import androidx.room.*

@Dao
interface  UsuariosDao{

    @Query("SELECT * FROM usuarios")
    fun getUsuarios(): List<Usuarios>

    @Insert
    fun insertUsuario(usuario: Usuarios)

    @Delete
    fun deleteUsuario(usuario: Usuarios)

    @Query("SELECT * FROM usuarios WHERE correo = :correo")
    fun cogerUsuario(correo: String): MutableList<Usuarios>


}