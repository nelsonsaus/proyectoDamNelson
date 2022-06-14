package com.example.miaprueba.preferencias

import android.content.Context

class Prefs (val context: Context) {


    val SHARED = "Nelson Sanchez Usero"
    val pref = context.getSharedPreferences(SHARED, 0)


    val ID = "id"
    val CORREO = "correo"
    val NOMBRE = "nombre"
    val APELLIDOS = "apellidos"
    val PASS = "pass"
    val PERFIL = "perfil"


    fun guardarId(id: Int){
        pref.edit().putInt(ID, id).apply()
    }

    fun guardarPerfil(perfil: Int){
        pref.edit().putInt(PERFIL, perfil).apply()
    }

    fun guardarCorreo(correo: String){
        pref.edit().putString(CORREO, correo).apply()
    }


    fun guardarPass(pass: String){
        pref.edit().putString(PASS, pass).apply()
    }

    fun guardarNombre(nombre: String){
        pref.edit().putString(NOMBRE, nombre).apply()
    }

    fun guardarApellidos(apellidos: String){
        pref.edit().putString(APELLIDOS, apellidos).apply()
    }





    fun getId(): Int{
        return pref.getInt(ID, 0)
    }

    fun getPass(): String{
        return pref.getString(PASS, "")!!
    }

    fun getNombre(): String{
        return pref.getString(NOMBRE, "")!!
    }

    fun getApellidos(): String{
        return pref.getString(APELLIDOS, "")!!
    }

    fun getCorreo(): String{
        return pref.getString(CORREO, "")!!
    }


    fun getPerfil(): Int{
        return pref.getInt(PERFIL, 0)
    }



    fun empty(){

        pref.edit().clear().apply()

    }



}