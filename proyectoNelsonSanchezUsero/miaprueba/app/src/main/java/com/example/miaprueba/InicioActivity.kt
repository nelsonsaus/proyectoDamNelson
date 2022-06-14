package com.example.miaprueba

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import com.example.miaprueba.databinding.ActivityInicioBinding
import com.example.miaprueba.preferencias.Aplicacion.Companion.prefs
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import com.synnapps.carouselview.CarouselView
import com.synnapps.carouselview.ImageListener


enum class Providers {
    GOOGLE
}

class InicioActivity : AppCompatActivity() {

    lateinit var binding: ActivityInicioBinding
    lateinit var mediaController: MediaController

    var sinimagen = false


    val frag = arrayOf<Fragment>(
        SobreFragment(),
        PorqueFragment(),
    )

    var elboton = 0


    var minuto = 0

    var imagenes: ArrayList<Int> = ArrayList()
    var carouselView: CarouselView? = null


    override fun onCreate(savedInstanceState: Bundle?){

        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }



        prefs = Prefs(this)

        Log.d("correo => ", prefs.getCorreo())


        imagenes.add(R.drawable.novedad12)
        imagenes.add(R.drawable.novedad22)

        binding.carousel!!.pageCount = imagenes.size

        binding.carousel!!.setImageListener(imagenesListener)


        mediaController = MediaController(this)



        setListener()


    }




    var imagenesListener = ImageListener{ position, imageView -> imageView.setImageResource(imagenes[position]) }


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




    //-----------------------------------------------------
    private fun setListener() {




        binding.btnPagina.setOnClickListener {

            val i = Intent(this, SearchActivity::class.java)
            startActivity(i)

        }

        binding.imageMenu.setOnClickListener {



            val i = Intent(this, ActiActivity2::class.java)
            startActivity(i)




        }

        binding.imageUser.setOnClickListener {



            val i = Intent(this, PerfilActivity::class.java)
            startActivity(i)




        }


        binding.cardSobre?.setOnClickListener {

            elboton = 0
            clickeadoBoton(elboton)

        }


        binding.cardPorque?.setOnClickListener {

            elboton = 1
            clickeadoBoton(elboton)

        }



    }



    //-----------------------------------------------------

    private fun reproducir() {


        var ruta = "android.resource://"+packageName+"/"+R.raw.video


        var uri = Uri.parse(ruta)


        mediaController.setAnchorView(binding.videoView)






    }




    private fun clickeadoBoton(btn: Int) {


        val fragmentManager = supportFragmentManager
        val transaction = fragmentManager.beginTransaction()

        transaction.replace(R.id.contenedor, frag[btn])

        transaction.addToBackStack(null)
        transaction.commit()



    }
}