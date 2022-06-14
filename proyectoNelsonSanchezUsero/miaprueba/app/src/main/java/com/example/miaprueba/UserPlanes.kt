package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class UserPlanes(var correo:String?=null, var nombre:String?=null, var semana:Int=1):
    Serializable {

}