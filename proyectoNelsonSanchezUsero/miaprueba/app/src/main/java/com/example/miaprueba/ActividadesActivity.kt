package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.miaprueba.databinding.ActivityActividadesBinding


class ActividadesActivity : AppCompatActivity() {

    lateinit var binding: ActivityActividadesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActividadesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

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

    private fun setListeners() {

        binding.btnSalir.setOnClickListener {
            var i = Intent(this, InicioActivity::class.java)
            startActivity(i)
        }

        binding.cardBodyCombat.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "bodycombat")
            }
            startActivity(i)

        }

        binding.cardCardio.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "cardio")
            }
            startActivity(i)

        }

        binding.cardCrossfit.setOnClickListener {


            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "crossfit")
            }
            startActivity(i)

        }

        binding.cardCycling.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "cycling")
            }
            startActivity(i)

        }


        binding.cardFuerza.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "fuerza")
            }
            startActivity(i)

        }

        binding.cardHiit.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "hiit")
            }
            startActivity(i)

        }

        binding.cardLibre.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "libre")
            }
            startActivity(i)

        }

        binding.cardYoga.setOnClickListener {

            var i = Intent(this, InfoactiActivity::class.java).apply {
                putExtra("nombre", "yoga")
            }
            startActivity(i)

        }


    }


}