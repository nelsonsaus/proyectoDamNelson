package com.example.miaprueba

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuarios(
    @PrimaryKey(autoGenerate = true) val uid: Int=0,
    val fecha: Long = System.currentTimeMillis(),
    val nombre: String,
    val apellidos: String,
    val correo: String,
    val pass: String,
    val perfil: Int,
    val imagen: String?
)

