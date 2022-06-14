package com.example.miaprueba

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.miaprueba.databinding.ActivityActi2Binding
import com.example.miaprueba.preferencias.Aplicacion.Companion.prefs
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class ActiActivity2 : AppCompatActivity() {

    lateinit var binding: ActivityActi2Binding

    lateinit var storageReferences: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActi2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        storageReferences = Firebase.storage.reference

        val animacion = binding.foto.background as AnimationDrawable

        animacion.setEnterFadeDuration(10)
        animacion.setExitFadeDuration(1000)
        animacion.start()



        cargarFoto()
        setListeners()


    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI() }


    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)      }


    private fun showSystemUI() {
        window.decorView.systemUiVisibility =
            (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)       }

    private fun cargarFoto() {
        var imagenRef = storageReferences.child("usuarios/"+prefs.getCorreo()+"/default.png")
        if (imagenRef!=null){
            imagenRef.downloadUrl
                .addOnSuccessListener {
                    //Existe la imagen de perfil la cargo
                    Picasso.get().load(it).resize(300,300).centerCrop().into(binding.imgMenu)
                }
                .addOnFailureListener {

                }
        }
    }

    private fun setListeners() {
        binding.cardActi.setOnClickListener {
            var i = Intent(this, ActividadesActivity::class.java)

            startActivity(i)
        }

        binding.cardInicio.setOnClickListener {
            var i = Intent(this, InicioActivity::class.java)

            startActivity(i)
        }


        binding.cardLocation.setOnClickListener {

            var i = Intent(this, MenuplanActivity::class.java)

            startActivity(i)

        }

        binding.cardPerfil.setOnClickListener {

            var i = Intent(this, PerfilActivity::class.java)

            startActivity(i)

        }

        binding.cardSalir.setOnClickListener {

            prefs.empty()

            finish()

            val i = Intent(this, MainActivity::class.java)
            startActivity(i)

        }


        binding.cardUsuarios.setOnClickListener {

            var i = Intent(this, UsuariosActivity::class.java)

            startActivity(i)

        }


    }


}