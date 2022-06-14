package com.example.miaprueba

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class Conversaciones(var correoEmisor:String?=null, var correoReceptor:String?=null, var nombreEmisor:String?=null, var mensaje:String?=null, var fecha:Long=System.currentTimeMillis()):
    Serializable {

}