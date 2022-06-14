package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Posts(var nombre:String?=null, var correo:String?=null, var texto:String?=null, var fecha:Long=System.currentTimeMillis()):
    Serializable {

}