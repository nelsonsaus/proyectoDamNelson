package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class UserEjercicios(var correo:String?=null, var nombre:String?=null, var completado:Boolean=false):
    Serializable {

}