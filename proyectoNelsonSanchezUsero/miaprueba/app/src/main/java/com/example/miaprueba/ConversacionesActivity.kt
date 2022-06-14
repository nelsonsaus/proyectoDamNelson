package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miaprueba.databinding.ActivityConversacionesBinding
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class ConversacionesActivity : AppCompatActivity() {

    lateinit var binding: ActivityConversacionesBinding
    lateinit var lista:MutableList<Conversaciones>
    lateinit var db: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var prefs: Prefs
    lateinit var storageReferences: StorageReference

    var nombreUser=""
    var correo=""
    var texto=""

    var eselchat = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConversacionesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        storageReferences = Firebase.storage.reference

        prefs = Prefs(this)

        lista = mutableListOf<Conversaciones>()


        cogerDatos()

        cargarFoto()

        setRecycle()

        db()

        setListeners()

    }

    private fun cargarFoto() {
        var imagenRef = storageReferences.child("usuarios/"+correo+"/default.png")
        if (imagenRef!=null){
            imagenRef.downloadUrl
                .addOnSuccessListener {
                    //Existe la imagen de perfil la cargo
                    Picasso.get().load(it).resize(300,300).centerCrop().into(binding.imgUserChat)
                }
                .addOnFailureListener {

                }
        }
    }

    private fun db() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("conversaciones")
        reference.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {

                        if(item.child("correoEmisor").getValue().toString() == correo || item.child("correoReceptor").getValue().toString() == correo){
                            if(item.child("correoEmisor").getValue().toString() == prefs.getCorreo() || item.child("correoReceptor").getValue().toString() == prefs.getCorreo()){

                                eselchat=true
                                var conver = Conversaciones(item.child("correoEmisor").getValue().toString(), item.child("correoReceptor").getValue().toString(), item.child("nombreEmisor").getValue().toString(), item.child("mensaje").getValue().toString())

                                lista.add(conver)

                            }
                        }



                    }

                    if(eselchat)
                        lista.sortBy { postt -> postt.fecha }


                    binding.recyclerViewChatt.adapter = ConversacionesAdapter(lista, correo)

                }

            }


            override fun onCancelled(error: DatabaseError) {

            }


        })





    }

    private fun cogerDatos() {

        val bundle = intent.extras
        nombreUser = bundle?.getString("nombreUser").toString()
        correo = bundle?.getString("correo").toString()

        binding.txtNombreChat.text=nombreUser

    }

    private fun setRecycle() {
        binding.recyclerViewChatt.layoutManager= LinearLayoutManager(this)
        binding.recyclerViewChatt.setHasFixedSize(true)
        binding.recyclerViewChatt.adapter=ConversacionesAdapter(lista, correo)
    }

    private fun setListeners() {

        binding.btnSalirChat.setOnClickListener {

            var i = Intent(this, InicioActivity::class.java)
            startActivity(i)

        }


        binding.btnEnviarChat.setOnClickListener {

            insertarMensaje()
            binding.txtTextoChatt.setText("")

        }

    }

    private fun insertarMensaje() {

        val time = System.currentTimeMillis()
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        texto=binding.txtTextoChatt.text.toString()
        reference = db.getReference("conversaciones")
        reference.child(""+time).setValue(Conversaciones(prefs.getCorreo(), correo, prefs.getNombre(), texto))
            .addOnSuccessListener {

                Log.d("LLEGA AQUI", "!")
                db()

            }
            .addOnFailureListener {
                Log.d("ERROR => ", it.message.toString())
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
                    or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)       }
}