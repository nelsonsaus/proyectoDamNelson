package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Ejercicios(var nombre:String?=null, var duracion:Int?=null, var descanso:Int?=null, var imagen:String?=null, var completado:Boolean = false):
    Serializable {

}