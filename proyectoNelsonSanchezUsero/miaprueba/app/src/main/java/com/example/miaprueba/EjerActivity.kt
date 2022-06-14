package com.example.miaprueba

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import com.bumptech.glide.Glide
import com.example.miaprueba.databinding.ActivityEjerBinding
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import androidx.annotation.NonNull

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task

import com.google.firebase.auth.AuthResult
import android.widget.TextView
import java.time.temporal.TemporalAdjusters.next


class EjerActivity : AppCompatActivity() {

    lateinit var binding: ActivityEjerBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: StorageReference
    private lateinit var reference2: DatabaseReference
    private lateinit var filereference: StorageReference
    var mAuth = FirebaseAuth.getInstance()

    lateinit var downloadUri: Task<Uri>
    lateinit var generatedFilePath: String


    var CurrentProgress = 0
    var CurrentProgress2 = 0
    //aqui va la duracion y en seg igual
    var seg = 0
    var textseg=""
    var duracion=0
    var descansoTime=0

    var segDescanso=0


    var micro = 0
    var startdisable = false

    var c = 0


    //HAY 6 ELEMENTOS EN EL RECYCLER OSEA SON 6 EJERCICIOS
    //DEL 0 AL 5 => RECORREREMOS LOS 6 EJERCICIOS
    var ejer = 0


    var listNombres: ArrayList<String> = ArrayList()
    var listDuracion: ArrayList<Integer> = ArrayList()
    var listDescanso: ArrayList<Integer> = ArrayList()


    lateinit var countDownTimer: CountDownTimer
    lateinit var countDownTimer2: CountDownTimer

    var nombre = ""

    var correo=""
    var nombreUser=""
    var nivel=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEjerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        cogerDatos()

        val user = mAuth.currentUser
        Log.d("imagen => ", "a")
        if (user != null) {
            db()
        } else {
            signInAnonymously()
        }


        setcount()

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

    private fun setcount() {

        countDownTimer = object: CountDownTimer((duracion*1000).toLong(), 1000){
            override fun onTick(p0: Long) {
                //20segundos seria 20*20 = 400 de max
                binding.progressBar.max=duracion*duracion
            }

            override fun onFinish() {

            }

        }
    }

    private fun setListeners() {

        binding.btnSalirEjer.setOnClickListener {

            var i = Intent(this@EjerActivity, PlanActivity::class.java)
            startActivity(i)

        }

        binding.startProgress.setOnClickListener {
            countDownTimer.start()
            cronoThread()
            startdisable = false
        }

    }

    private fun cronoThread() {

        Thread{
            try {
                while(!startdisable) {
                    runOnUiThread(object : Runnable {
                        override fun run() {

                            textseg = "" + seg
                            binding.txtContador.setText(textseg)

                            micro++




                            if (micro == 100) {


                                micro = 0

                                seg--
                                if(seg<=0)
                                    textseg = "" + seg

                                CurrentProgress = CurrentProgress + duracion
                                binding.progressBar.setProgress(CurrentProgress)


                            }



                            if(seg==0)
                                startdisable = true

                        }
                    })
                    Thread.sleep(10)

                }

                if(seg==0) {
                    startdisable = true
                    Thread.interrupted()
                    descanso()
                }





            }catch (e: InterruptedException){
                e.printStackTrace()
            }

        }.start()







    }

    private fun descanso() {

        binding.txtDescansoCont.text=segDescanso.toString()
        micro=0
        CurrentProgress2 = 0
        startdisable = false
//        binding.progressBarDescanso.setProgress(0)
        c=0


        reference2=db.getReference("userEjercicios/"+nombreUser)
        reference2.child(nivel).child(nombre).setValue(UserEjercicios(correo, nombre, true))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d("ERROR => ", it.message.toString())
            }

        cronoThread2()


    }


    private fun setcount2() {

        countDownTimer2 = object: CountDownTimer((descansoTime*1000).toLong(), 1000){
            override fun onTick(p0: Long) {
                Log.d("descansotime => ", descansoTime.toString())
                binding.progressBarDescanso.max=descansoTime*descansoTime
            }

            override fun onFinish() {

            }

        }
    }


    private fun cronoThread2() {

        Thread{
            try {
                while(!startdisable) {
                    runOnUiThread(object : Runnable {
                        override fun run() {
                            if(c==0){
                                c++
                                setcount2()
                                countDownTimer2.start()
                                binding.cardDescanso.setVisibility(View.VISIBLE)
                            }

                            textseg = "" + segDescanso
                            binding.txtDescansoCont.setText(textseg)

                            micro++




                            if (micro == 100) {


                                micro = 0

                                segDescanso--
                                if(segDescanso<=0)
                                    textseg = "" + segDescanso

                                CurrentProgress2 = CurrentProgress2 + descansoTime
                                binding.progressBarDescanso.setProgress(CurrentProgress2)


                            }


                            if(segDescanso==0)
                                startdisable = true

                        }
                    })
                    Thread.sleep(10)

                }

                if(segDescanso==0) {
                    startdisable = true
                    Thread.interrupted()
                    nextt()
                }




            }catch (e: InterruptedException){
                e.printStackTrace()
            }

        }.start()

    }


    private fun nextt(){

        if(ejer!=5) {
            ejer = ejer + 1
            Log.d("Nueva posicion", ejer.toString())
            binding.txtNombreEjercicio.text = listNombres[ejer]
            nombre = listNombres[ejer]
            seg = listDuracion[ejer].toInt()
            textseg = "" + seg
            binding.txtContador.setText(textseg)

            duracion = listDuracion[ejer].toInt()
            descansoTime = listDescanso[ejer].toInt()
            segDescanso = listDescanso[ejer].toInt()
            startdisable = false
            binding.progressBar.setProgress(0)
            binding.progressBarDescanso.setProgress(0)
            CurrentProgress = 0
            CurrentProgress2 = 0
            db()
        }else {
            var i = Intent(this@EjerActivity, PlanActivity::class.java)
            startActivity(i)
        }

    }




    private fun signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this) {
            Log.d("imagen => ", "b")
            db()
        }
            .addOnFailureListener(
                this
            ) { exception -> Log.e(TAG, "signInAnonymously:FAILURE", exception) }
    }

    private fun db() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = FirebaseStorage.getInstance().getReference()
        Log.d("a => ", reference.toString())
        filereference=reference.child("ejercicios/"+nombre+".gif")
        filereference.getDownloadUrl().addOnSuccessListener(object: OnSuccessListener<Uri> {
            override fun onSuccess(uri: Uri) {
                // Got the download URL for 'users/me/profile.png'

                downloadUri = filereference.getDownloadUrl()
                generatedFilePath = downloadUri.toString(); /// The string(file link) that you need
                Log.d("imagen => ", generatedFilePath)
                Glide.with(this@EjerActivity)
                    .load(uri)
                    .into(binding.gifPrueba)

                binding.cardDescanso.setVisibility(View.INVISIBLE)
            }



            })
    }


    private fun cogerDatos() {
        val bundle= intent.extras
        listNombres= bundle?.getStringArrayList("nombres") as ArrayList<String>
        listDuracion= bundle.getIntegerArrayList("duraciones") as ArrayList<Integer>
        listDescanso = bundle.getIntegerArrayList("descansos") as ArrayList<Integer>
        ejer=bundle.getInt("posicion")
        Log.d("POSICION EJERCICIO", ejer.toString())
        binding.txtNombreEjercicio.text=listNombres[ejer]
        nombre=listNombres[ejer]
        seg = listDuracion[ejer].toInt()
        textseg=""+seg
        binding.txtContador.setText(textseg)

        duracion=listDuracion[ejer].toInt()
        descansoTime=listDescanso[ejer].toInt()
        segDescanso=listDescanso[ejer].toInt()

        correo = bundle?.getString("correo").toString()
        nombreUser = bundle?.getString("nombreUser").toString()
        nivel = bundle?.getString("nivel").toString()
    }
}