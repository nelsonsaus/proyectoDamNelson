package com.example.miaprueba.preferencias

import android.app.Application

class Aplicacion : Application() {

    companion object{
        lateinit var prefs: Prefs
    }

    override fun onCreate(){
        super.onCreate()
        prefs = Prefs(applicationContext)
    }

}