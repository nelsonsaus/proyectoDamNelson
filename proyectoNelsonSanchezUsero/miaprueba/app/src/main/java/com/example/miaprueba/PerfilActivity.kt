package com.example.miaprueba

import android.content.ContentResolver
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.webkit.MimeTypeMap
import androidx.room.Room
import com.example.miaprueba.databinding.ActivityPerfilBinding
import com.example.miaprueba.preferencias.Aplicacion.Companion.prefs
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import android.graphics.drawable.BitmapDrawable

import android.graphics.drawable.Drawable

import android.graphics.Bitmap
import android.util.Log

import com.bumptech.glide.request.target.SimpleTarget

import com.bumptech.glide.Glide
import com.bumptech.glide.request.transition.Transition
import android.R
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts

import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class PerfilActivity : AppCompatActivity() {


    private val responseLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode== RESULT_OK){
                if (it.data!=null){
                    //hemos seleccionado un archivo
                    val uri:Uri? = it.data!!.data
                    var imagenref=storageReferences.child("usuarios/"+correo+"/default.png")
                    //si quisiera guardar el documento con su nombre
                    //imagenref=storageReferences.child(correo+"/images/${uri?.lastPathSegment}")
                    subirImagenNube(imagenref, uri)
                }
            }
        }

    lateinit var binding: ActivityPerfilBinding
    lateinit var db: FirebaseDatabase
    lateinit var storageReferences: StorageReference
    lateinit var reference: StorageReference
    lateinit var reference2: DatabaseReference

    var nombre=""
    var apellidos = ""
    var correo=""
    var perfil = 0
    var imagen = ""

    lateinit var imageUri: Uri



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPerfilBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)
            getWindow().setStatusBarColor(Color.TRANSPARENT)

        }else{
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        }


        storageReferences = Firebase.storage.reference

        if(prefs.getPerfil()==1)
            binding.layoutPerfil?.setVisibility(View.VISIBLE)



        meterDatos()

        setListeners()

        cogerDatos()

        cargarFoto()

    }



    private fun cambiarFoto() {
        //ABRIMOS EN NAVEGADOR DE ARCHIVOS
        var i= Intent(Intent.ACTION_OPEN_DOCUMENT)
        i.addCategory(Intent.CATEGORY_OPENABLE)
        i.setType("image/*")
        responseLauncher.launch(i)
    }

    private fun subirImagenNube(imagenref: StorageReference, rutaArchivo: Uri?) {
        if (rutaArchivo!=null){
            imagenref.putFile(rutaArchivo)
                .addOnCompleteListener(){
                    if (it.isSuccessful){
                        cargarFoto()
                    }else{
                        Toast.makeText(this, "ERROR AL GUARDAR LA IMAGEN", Toast.LENGTH_LONG).show()
                    }
                }
        }
    }


    private fun cargarFoto() {
        var imagenRef = storageReferences.child("usuarios/"+correo+"/default.png")
        if (imagenRef!=null){
            imagenRef.downloadUrl
                .addOnSuccessListener {
                    //Existe la imagen de perfil la cargo
                    Picasso.get().load(it).resize(300,300).centerCrop().into(binding.imgFoto)
                }
                .addOnFailureListener {

                }
        }
    }




    private fun cogerDatos() {

        nombre = binding.txtNombrePerfil.text.toString()
        apellidos = binding.txtApellidosPerfil.text.toString()
        correo = binding.txtCorreoPerfil.text.toString()
        if(prefs.getPerfil()==1)
            perfil = binding.txtPerfilPerfil?.text.toString().toInt()

    }

    private fun meterDatos() {

        binding.txtNombrePerfil!!.setText(prefs.getNombre())
        binding.txtApellidosPerfil?.setText(prefs.getApellidos())
        binding.txtCorreoPerfil?.setText(prefs.getCorreo())

        binding.txtTituloPerfil?.setText(prefs.getNombre())

        if(prefs.getPerfil()==1){
            binding.txtPerfilPerfil?.setText(prefs.getPerfil())
        }
    }

    private fun setListeners() {


        binding.btnSalirPerfil?.setOnClickListener {
            var i = Intent(this, InicioActivity::class.java)
            startActivity(i)
        }


        binding.btnEditarFoto?.setOnClickListener {

            cambiarFoto()

        }


        binding.btnEditPerfil?.setOnClickListener {

            editar()

        }


    }

    private fun editar() {

        cogerDatos()

        prefs.guardarPerfil(perfil)
        prefs.guardarNombre(nombre)
        prefs.guardarApellidos(apellidos)
        prefs.guardarCorreo(correo)

        db = FirebaseDatabase.getInstance("https://mapas151121n-default-rtdb.europe-west1.firebasedatabase.app/")
        reference2 = db.getReference("usuarios/"+nombre)
        reference2.setValue(Users(nombre, apellidos, correo, perfil))
            .addOnSuccessListener {
                Toast.makeText(this, "EDITADO", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Log.d("ERROR => ", it.message.toString())
            }

    }




}