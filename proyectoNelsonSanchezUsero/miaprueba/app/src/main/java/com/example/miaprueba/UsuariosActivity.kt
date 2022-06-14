package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.miaprueba.databinding.ActivityUsuariosBinding
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_usuarios.*

class UsuariosActivity : AppCompatActivity() {

    lateinit var binding: ActivityUsuariosBinding
    lateinit var entrenadores: MutableList<Usuarios>
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    lateinit var prefs: Prefs
    private lateinit var lista:MutableList<Users>

    lateinit var user: Users


    val listNombres: ArrayList<String> = ArrayList()
    val listCorreos: ArrayList<String> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsuariosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        prefs = Prefs(this)

        lista = mutableListOf<Users>()

        entrenadores = mutableListOf<Usuarios>()


        setListeners()

        setRecycle()

        db()

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
                var i = Intent(this@UsuariosActivity, UserPostActivity::class.java).apply {
                    Log.d("POSICION", viewHolder.adapterPosition.toString())
                    putStringArrayListExtra("nombres", listNombres)
                    putStringArrayListExtra("correos", listCorreos)
                    putExtra("posicion", viewHolder.adapterPosition)
                }
                startActivity(i)
            }
        }
        val ith = ItemTouchHelper(itemTouch)
        ith.attachToRecyclerView(binding.usuariosRecyclerView)
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


    private fun setRecycle() {
        binding.usuariosRecyclerView.layoutManager= LinearLayoutManager(this)
        binding.usuariosRecyclerView.setHasFixedSize(true)
        binding.usuariosRecyclerView.adapter=UsersAdapter(lista)
    }


    private fun db() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference=db.getReference("usuarios")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {


                        listNombres.add(item.child("nombre").getValue().toString())
                        listCorreos.add(item.child("correo").getValue().toString())

                        user = Users(item.child("nombre").getValue().toString(), "", item.child("correo").getValue().toString())

                        lista.add(user)

                    }

                    binding.usuariosRecyclerView.adapter = UsersAdapter(lista)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })






    }

    private fun setListeners() {

        binding.btnSalirUsuarios?.setOnClickListener {
            var i = Intent(this, ActiActivity2::class.java)
            startActivity(i)
        }

        binding.btnUsuarios.setOnClickListener {

            db()

        }


        binding.btnTrainers.setOnClickListener {

            cogerEntrenadores()

        }

    }

    private fun cogerEntrenadores() {

        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference=db.getReference("usuarios")
        reference.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {

                        if(item.child("perfil").getValue().toString().toInt() == 1){
                            user = Users(item.child("nombre").getValue().toString(), "", item.child("correo").getValue().toString())

                            lista.add(user)
                        }

                    }

                    binding.usuariosRecyclerView.adapter = UsersAdapter(lista)

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }
}