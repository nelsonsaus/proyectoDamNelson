package com.example.miaprueba

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.miaprueba.databinding.ActivityPlanBinding
import com.example.miaprueba.databinding.EjerciciosLayoutBinding
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import java.io.Serializable
import android.R
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater

import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import kotlinx.android.synthetic.main.ejercicios_layout.view.*


class PlanActivity : AppCompatActivity() {

    lateinit var binding: ActivityPlanBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    private lateinit var reference3: DatabaseReference
    private lateinit var lista:MutableList<Ejercicios>
    lateinit var prefs: Prefs
    var correo=""
    var nombreUser=""

    lateinit var ejercicios: MutableList<Ejercicios>
    lateinit var ejerCompletados: MutableList<Int>

    var cont=1
    var todoCompletado = true


    var semana=0
    var semanaEncontrado=0


    var nivel=""




    val listNombres: ArrayList<String> = ArrayList()
    val listDuracion: ArrayList<Int> = ArrayList()
    val listDescanso: ArrayList<Int> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = Prefs(this@PlanActivity)

        correo=prefs.getCorreo()
        nombreUser=prefs.getNombre()

        ejerCompletados = mutableListOf<Int>()

        lista = mutableListOf<Ejercicios>()

        cogerDatos()

        setRecycle()

        db()

        setListeners()

        setUpSwipe()

    }


    private fun setUpSwipe() {
        val itemTouch = object: ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT){

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder

            ): Boolean {
                return false
                //ponemos eso para q no de error pero no irve para nada
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding.planRecyclerView.adapter as EjerciciosAdapter
                val nombree = adapter.getItem(viewHolder.adapterPosition)?.nombre.toString()
                Log.d("EL NOMBRE, ", nombree)
                var i = Intent(this@PlanActivity, EjerActivity::class.java).apply {
                    Log.d("POSICION", viewHolder.adapterPosition.toString())
                    putStringArrayListExtra("nombres", listNombres)
                    putIntegerArrayListExtra("descansos", listDescanso)
                    putIntegerArrayListExtra("duraciones", listDuracion)
                    putExtra("posicion", viewHolder.adapterPosition)
                    putExtra("correo", correo)
                    putExtra("nombreUser", nombreUser)
                    putExtra("nivel", nivel)
                }
                startActivity(i)
            }
        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.planRecyclerView)
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

    private fun setListeners() {

        binding.btnSalirPlan.setOnClickListener {
            var i = Intent(this, StartPlanActivity::class.java)
            startActivity(i)
        }

        binding.btnDone.setOnClickListener {

            cambiarPlan()

        }

    }

    private fun cambiarPlan() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference=db.getReference("ejercicios")
        reference.limitToLast(6).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()){
                    for(item in snapshot.children){
                        val ejer = item.getValue(Ejercicios::class.java)
                        if (ejer!=null){


                            lista.add(ejer)


                            if(lista.count()==6){
                                listNombres.clear()
                                listDescanso.clear()
                                listDuracion.clear()
                                lista.shuffle()
                                for(elem in lista){

                                    elem.nombre?.let { listNombres.add(it) }
                                    elem.descanso?.let { listDescanso.add(it) }
                                    elem.duracion?.let { listDuracion.add(it) }

                                }

                            }

                        }
                    }


                    ejercicios = lista


                    cont++
                    binding.txtSemana.text="Semana " + cont

                    binding.planRecyclerView.adapter=EjerciciosAdapter(ejercicios)
                    db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
                    reference3=db.getReference("userPlanes/"+nombreUser).child(nivel).child("semana")
                    reference3.addValueEventListener(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            semana = snapshot.getValue().toString().toInt()
                            if(semanaEncontrado==0) {
                                reference3.setValue(semana+1)
                                binding.txtSemana.text="Semana "+(semana+1)
                                semanaEncontrado=1
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }

                    })


                    semana += 1
                    reference3.setValue(semana)
                    binding.txtSemana.text="Semana "+semana






                }
            }


            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    private fun setRecycle() {
        binding.planRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.planRecyclerView.setHasFixedSize(true)
        binding.planRecyclerView.adapter=EjerciciosAdapter(lista)
    }

    private fun db() {
        db =
            FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("ejercicios")
        reference.limitToLast(6).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {
                        val ejer = item.getValue(Ejercicios::class.java)
                        if (ejer != null) {
                            lista.add(ejer)
                            ejer.nombre?.let { listNombres.add(it) }
                            ejer.descanso?.let { listDescanso.add(it) }
                            ejer.duracion?.let { listDuracion.add(it) }

                        }
                    }
                    ejercicios = lista


                    binding.planRecyclerView.adapter = EjerciciosAdapter(ejercicios)


                }
            }


            override fun onCancelled(error: DatabaseError) {

            }


        })

        reference3 = db.getReference("userPlanes/" + nombreUser).child(nivel).child("semana")
        reference3.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.txtSemana.text = "Semana " + snapshot.getValue().toString()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })



    }



    private fun cogerDatos() {

        val bundle = intent.extras
        nivel = bundle?.getString("nivel").toString()

    }


}