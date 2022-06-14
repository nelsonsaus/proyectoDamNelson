package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import com.example.miaprueba.databinding.ActivityStartPlanBinding
import com.example.miaprueba.preferencias.Aplicacion.Companion.prefs
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase

import com.google.firebase.database.DatabaseReference
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle


class StartPlanActivity : AppCompatActivity() {

    lateinit var binding: ActivityStartPlanBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var reference3: DatabaseReference
    private lateinit var lista: MutableList<Ejercicios>
    lateinit var prefs: Prefs
    var nivel=""
    var encontrado=false
    var correo=""
    var nombreUser = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = Prefs(this@StartPlanActivity)
        correo=prefs.getCorreo()
        nombreUser=prefs.getNombre()

        lista = mutableListOf<Ejercicios>()


        cogerDatos()


        setListener()

    }

    private fun db() {


        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference=db.getReference("userPlanes")
        reference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild(""+nombreUser+"/"+nivel)) {
                    encontrado=false

                    reference2=db.getReference("ejercicios")
                    reference2.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            lista.clear()
                            if (snapshot.exists()) {
                                for (item in snapshot.children) {
                                    val ejer = item.getValue(Ejercicios::class.java)
                                    if (ejer != null) {
                                        lista.add(ejer)

                                        reference3=db.getReference("userEjercicios")
                                        reference3.child(nombreUser).child(nivel).child(ejer.nombre.toString())
                                            .setValue(UserEjercicios(correo, ejer.nombre.toString()))
                                            .addOnSuccessListener {

                                            }
                                            .addOnFailureListener {
                                                Log.d("ERROR => ", it.message.toString())
                                            }
                                    }


                                }


                            }


                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })




                    reference.child(nombreUser).child(nivel)
                        .setValue(UserPlanes(correo, nivel))
                        .addOnSuccessListener {

                        }
                        .addOnFailureListener {
                            Log.d("ERROR => ", it.message.toString())
                        }


                }
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })


        var i = Intent(this, PlanActivity::class.java).apply {
            putExtra("nivel", nivel)
        }
        startActivity(i)


    }

    private fun setListener() {

        val current = LocalDateTime.now()

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
        val formatted = current.format(formatter)

        binding.txtFecha.text=formatted

        binding.btnCenter.setOnClickListener {


            db()

        }


        binding.btnSalirStart.setOnClickListener {
            var i = Intent(this, MenuplanActivity::class.java)
            startActivity(i)
        }

        binding.btnHome.setOnClickListener {

            var i = Intent(this, InicioActivity::class.java)
            startActivity(i)

        }

        binding.btnPerfilStart.setOnClickListener {

            var i = Intent(this, PerfilActivity::class.java)
            startActivity(i)

        }

    }

    private fun cogerDatos() {

        val bundle = intent.extras
        nivel = bundle?.getString("nivelPlan").toString()

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
}