package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.miaprueba.databinding.ActivityUserPostBinding
import com.example.miaprueba.preferencias.Aplicacion
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class UserPostActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserPostBinding
    lateinit var lista:MutableList<Posts>
    lateinit var db: FirebaseDatabase
    lateinit var reference: DatabaseReference
    lateinit var storageReferences: StorageReference
    lateinit var prefs: Prefs


    var nombreUser = ""
    var texto = ""
    var correo = ""
    var posicion = 0

    var listNombres: ArrayList<String> = ArrayList()
    var listCorreos: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserPostBinding.inflate(layoutInflater)
        setContentView(binding.root)


        hideSystemUI();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        storageReferences = Firebase.storage.reference

        prefs = Prefs(this)



        lista = mutableListOf<Posts>()



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
                    Picasso.get().load(it).resize(300,300).centerCrop().into(binding.imgUser)
                    Picasso.get().load(it).resize(300,300).centerCrop().into(binding.imgUserHeader)
                }
                .addOnFailureListener {

                }
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

    private fun cogerDatos() {

        val bundle = intent.extras
        posicion = bundle?.getInt("posicion")!!
        listNombres = bundle?.getStringArrayList("nombres") as ArrayList<String>
        listCorreos = bundle?.getStringArrayList("correos") as ArrayList<String>

        nombreUser = listNombres[posicion]
        correo = listCorreos[posicion]


        if(correo==prefs.getCorreo()){
            binding.btnAdd.setVisibility(View.VISIBLE)
            binding.btnChatear.setVisibility(View.INVISIBLE)
        }

    }

    private fun setListeners() {

        binding.btnChatear.setOnClickListener {

            var i = Intent(this@UserPostActivity, ConversacionesActivity::class.java).apply {
                putExtra("nombreUser", nombreUser)
                putExtra("correo", correo)
            }
            startActivity(i)

        }

        binding.btnSalirPost.setOnClickListener {
            var i = Intent(this@UserPostActivity, UsuariosActivity::class.java)
            startActivity(i)
        }

        binding.btnAdd.setOnClickListener {

            binding.cardAdd.setVisibility(View.VISIBLE)
            binding.btnAdd.setVisibility(View.INVISIBLE)

        }

        binding.btnAnnadirPost.setOnClickListener {

            insertarPost()
            binding.btnAdd.setVisibility(View.VISIBLE)
            binding.cardAdd.setVisibility(View.INVISIBLE)
            binding.txtTextoNewPost.setText("")

        }

        binding.txtNombreHeader.text = nombreUser
        binding.txtNombrePost.text = nombreUser
        binding.txtNombreUser.text = nombreUser

        binding.txtCorreoUser.text = correo

    }

    private fun insertarPost() {

        val time = System.currentTimeMillis()
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        texto=binding.txtTextoNewPost.text.toString()
        reference = db.getReference("posts/"+nombreUser)
        reference.child(""+time).setValue(Posts(nombreUser, correo, texto))
            .addOnSuccessListener {

            }
            .addOnFailureListener {
                Log.d("ERROR => ", it.message.toString())
            }

    }

    private fun setRecycle() {
        binding.recyclerPost.layoutManager= LinearLayoutManager(this)
        binding.recyclerPost.setHasFixedSize(true)
        binding.recyclerPost.adapter=PostsAdapter(lista, correo)
    }


    private fun db() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference = db.getReference("posts/"+nombreUser)
        reference.orderByChild("fecha").addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                lista.clear()
                if (snapshot.exists()) {
                    for (item in snapshot.children) {


                        var post = Posts(item.child("nombre").getValue().toString(), item.child("correo").getValue().toString(), item.child("texto").getValue().toString())

                        lista.add(post)


                    }

                    lista.sortBy { postt -> postt.fecha }

                    binding.recyclerPost.adapter = PostsAdapter(lista, correo)
//
//
                }
//                if (snapshot.exists()) {
//                    for (item in snapshot.children) {
//                        val post = item.getValue(Posts::class.java)
//                        if (post != null) {
//                            lista.add(post)
//                        }
//                    }
//
//
//                    binding.recyclerPost.adapter = PostsAdapter(lista)
//
//
//                }
            }


            override fun onCancelled(error: DatabaseError) {

            }


        })





    }
}