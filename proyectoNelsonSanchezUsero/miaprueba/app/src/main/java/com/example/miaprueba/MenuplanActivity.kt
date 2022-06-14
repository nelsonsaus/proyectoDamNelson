package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.miaprueba.databinding.ActivityInicioBinding
import com.example.miaprueba.databinding.ActivityMenuplanBinding

class MenuplanActivity : AppCompatActivity() {

    lateinit var binding: ActivityMenuplanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuplanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        setListeners()

    }

    private fun setListeners() {

        binding.btnSalirMenu.setOnClickListener {
            var i = Intent(this, ActiActivity2::class.java)
            startActivity(i)
        }

        binding.cardNovato.setOnClickListener {

            var i = Intent(this, StartPlanActivity::class.java).apply {
                putExtra("nivelPlan", "novato")
            }
            startActivity(i)

        }

        binding.cardMedio.setOnClickListener {

            var i = Intent(this, StartPlanActivity::class.java).apply {
                putExtra("nivelPlan", "medio")
            }
            startActivity(i)

        }

        binding.cardExperto.setOnClickListener {

            var i = Intent(this, StartPlanActivity::class.java).apply {
                putExtra("nivelPlan", "experto")
            }
            startActivity(i)

        }

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
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
    }
}