package com.example.miaprueba

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.room.Room
import com.example.miaprueba.databinding.ActivityRegisterBinding
import com.example.miaprueba.preferencias.Prefs
import com.google.firebase.database.*
import java.util.*

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    lateinit var lis: MutableList<Usuarios>
    private lateinit var db:FirebaseDatabase
    private lateinit var reference: DatabaseReference
    lateinit var prefs: Prefs

    var nombre = ""
    var apellidos = ""
    var correo = ""
    var pass = ""




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prefs = Prefs(this)


        setListeners()
    }




    private fun comprobar(): Boolean {


        nombre = binding.txtNombre.text.toString().trim()
        apellidos = binding.txtApellidos.text.toString().trim()
        correo = binding.txtCorreo2.text.toString().trim()
        pass = binding.txtPass2.text.toString().trim()


        if(nombre.isNullOrEmpty()){

            binding.txtNombre.setError("Rellene el campo nombre")
            return false

        }

        if(apellidos.isNullOrEmpty()){

            binding.txtApellidos.setError("Rellene el campo apellidos")
            return false

        }


        if(correo.isNullOrEmpty()){

            binding.txtCorreo2.setError("Rellene el campo correo")
            return false

        }

        if(pass.isNullOrEmpty()){

            binding.txtPass2.setError("Rellene el campo password")
            return false

        }


        var passenc = encript(pass)
        pass = passenc.toString()

        return true

    }



    private fun errorshow() {

        val alerta= AlertDialog.Builder(this)
            .setTitle("ERROR")
            .setMessage("Esa cuenta ya existe !!!")
            .setNegativeButton("Okay", null)
            .create()

        alerta.show()

    }





    private fun registrarse() {

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
                if(it.correo == correo){
                    encontrado=true

                    errorshow()
                    binding.txtCorreo2.setText("")
                    binding.txtPass2.setText("")
                    binding.txtCorreo2.requestFocus()



                }
            }


            if(!encontrado){

                //Insertar:
                database.usuariosDao().insertUsuario((Usuarios(nombre = nombre, apellidos = apellidos, correo = correo, pass = pass, perfil=0, imagen = null)))

                lis = database.usuariosDao().cogerUsuario(correo)
                if(!lis.isEmpty()){
                    Log.d("ada", "a")
                    val usuario=Users(nombre, apellidos, correo, lis[0].perfil)
                    db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
                    reference=db.getReference("usuarios")
                    reference.child(""+nombre).setValue(usuario)

                    prefs.guardarApellidos(apellidos)
                    prefs.guardarCorreo(correo)
                    prefs.guardarNombre(nombre)
                    prefs.guardarPerfil(lis[0].perfil)
                }


                var i = Intent(this, InicioActivity::class.java)


                startActivity(i)

            }




        }

    }




    private fun setListeners() {

        binding.btnLogin2.setOnClickListener {

            var i = Intent(this, MainActivity::class.java)

            startActivity(i)

        }

        binding.btnRegister2.setOnClickListener {

            registrarse()

        }


    }



    private fun encript(text: String): String? {
        val data = text.toByteArray()
        return Base64.getEncoder().encodeToString(data)
    }






}