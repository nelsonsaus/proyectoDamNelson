package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.miaprueba.preferencias.Prefs
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.miaprueba.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import java.util.*

class MainActivity : AppCompatActivity() {


    lateinit var binding: ActivityMainBinding
    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var reference2: DatabaseReference
    lateinit var prefs: Prefs
    lateinit var lis: MutableList<Usuarios>

    var correo = ""
    var pass = ""
    var nombre = ""
    var apellidos = ""





    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val task =
                    GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    val cuenta =
                        task.getResult(ApiException::class.java)
                    if (cuenta != null) {
                        val credenciales = GoogleAuthProvider.getCredential(
                            cuenta.idToken,
                            null
                        )
                        FirebaseAuth.getInstance().signInWithCredential(credenciales)
                            .addOnCompleteListener {
                                if (it.isSuccessful) {

                                    val database = Room.databaseBuilder(
                                        this, UsuariosDatabase::class.java, "usuarios_database"
                                    )
                                        .allowMainThreadQueries()
                                        .build()



                                    lis = database.usuariosDao().cogerUsuario(cuenta.email!!)
                                    if(lis.isEmpty()){

                                        Log.d("zzz", "zzz")


                                        database.usuariosDao().insertUsuario((Usuarios(nombre = cuenta.displayName!!, apellidos = cuenta.familyName!!, correo = cuenta.email!!, pass = "", perfil=0, imagen = null)))

                                    }




                                    //***********************************************************************
                                    //***********************************************************************
                                    //***********************************************************************

                                    //CREAR USUARAIOS ENTRENADORES(MAS PERMISOS)

                                    //database.usuariosDao().insertUsuario((Usuarios(nombre = "admin1", apellidos = "apellidos admin", correo = "admin1@gmail.com", pass = "1234", perfil=1, imagen = null)))
                                    //database.usuariosDao().insertUsuario((Usuarios(nombre = "admin2", apellidos = "apellidos admin2", correo = "admin2@gmail.com", pass = "1234", perfil=1, imagen = null)))

                                    //***********************************************************************
                                    //***********************************************************************
                                    //***********************************************************************





                                    val usuarios = database.usuariosDao().getUsuarios()

                                    db()



                                    usuarios.forEach{
                                        if(it.correo == cuenta.email!!){

                                            prefs.guardarNombre(it.nombre)
                                            prefs.guardarCorreo(it.correo)
                                            prefs.guardarApellidos(it.apellidos)
                                            prefs.guardarId(it.uid)
                                            prefs.guardarPass(it.pass)
                                            prefs.guardarPerfil(it.perfil)


                                        }
                                    }


                                    val i = Intent(this, InicioActivity::class.java)
                                    startActivity(i)

                                } else {
                                    Log.d("bbbb", "b")
                                }
                            }
                    }
                } catch (e: ApiException) {
                    Log.d("Error al validar con Google", e.localizedMessage.toString())
                }
            } else {
                Log.d("ccc", "cc")
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prefs = Prefs(this)
        seguir()
        setListeners()
//        var i = Intent(this, MenuplanActivity::class.java)
//        startActivity(i)

    }




    private fun db() {
        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference=db.getReference("usuarios")
        reference.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.hasChild("" + prefs.getNombre())) {
                    reference.child(prefs.getNombre()).setValue(Users(prefs.getNombre(), prefs.getApellidos(), prefs.getCorreo(), prefs.getPerfil()))
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



    }



    private fun seguir() {
        if(prefs.getCorreo().isNotEmpty()){

            var i = Intent(this, InicioActivity::class.java)

            startActivity(i)

        }
    }


    private fun google() {
        val gso = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken("287988369606-lbkgcmphtv3cmus0u81v018qfl388jf8.apps.googleusercontent.com")
            .requestEmail()
            .build()

        val googleClient = GoogleSignIn.getClient(this, gso)
        googleClient.signOut()
        responseLauncher.launch(googleClient.signInIntent)
    }



    private fun comprobar(): Boolean {

        correo =  binding.txtCorreoLogin!!.text.toString().trim()
        pass = binding.txtPassLogin!!.text.toString().trim()




        if(correo.isNullOrEmpty()){

            binding.txtCorreo?.setError("Rellene el campo correo")
            return false

        }

        if(pass.isNullOrEmpty()){

            binding.txtPass?.setError("Rellene el campo password")
            return false

        }


        var passenc = encript(pass)
        pass = passenc.toString()


        return true

    }



    private fun errorshow() {

        val alerta= AlertDialog.Builder(this)
            .setTitle("ERROR")
            .setMessage("Esa cuenta no existe...")
            .setNegativeButton("Okay", null)
            .create()

        alerta.show()

    }


    private fun registrarse() {


        var i = Intent(this, RegisterActivity::class.java)

        startActivity(i)

    }



    private fun loguearte() {

        if(comprobar()) {


            val database = Room.databaseBuilder(
                this, UsuariosDatabase::class.java, "usuarios_database"
            )
                .allowMainThreadQueries()
                .build()









            //conseguir todos los usuarios:
            val usuarios = database.usuariosDao().getUsuarios()


            var encontrado = false


            usuarios.forEach{

                Log.d("correo introducido => ", correo)
                Log.d("correo db => ", it.correo)

                if(it.correo == correo){

                    db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
                    reference2 = db.getReference("usuarios/"+it.nombre)
                    reference2.addValueEventListener(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {

                                prefs.guardarNombre(snapshot.child("nombre").getValue().toString())
                                prefs.guardarCorreo(snapshot.child("correo").getValue().toString())
                                prefs.guardarApellidos(snapshot.child("apellidos").getValue().toString())
                                prefs.guardarPerfil(snapshot.child("perfil").getValue().toString().toInt())


                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })

                    encontrado=true



                    var i = Intent(this, InicioActivity::class.java)


                    startActivity(i)
                }
            }


            if(!encontrado){
                errorshow()
                binding.txtCorreo?.setText("")
                binding.txtPass?.setText("")
                binding.txtCorreo?.requestFocus()
            }




        }

    }




    private fun setListeners() {

        binding.btnLogin?.setOnClickListener {

            loguearte()

        }

        binding.btnRegister?.setOnClickListener {

            registrarse()

        }



        binding.btnGoogle.setOnClickListener{

            google()

        }

    }



    private fun encript(text: String): String? {
        val data = text.toByteArray()
        return Base64.getEncoder().encodeToString(data)
    }









}