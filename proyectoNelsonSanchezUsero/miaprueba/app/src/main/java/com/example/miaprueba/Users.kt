package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Users(var nombre:String?=null, var apellidos:String?=null, var correo:String?=null, var perfil:Int?=null, var imagen:String?=null, var fecha:Long=System.currentTimeMillis()):
    Serializable {

}