package com.example.miaprueba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miaprueba.databinding.PostLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso


class PostsAdapter(private val lista: MutableList<Posts>, private val correo: String): RecyclerView.Adapter<PostsAdapter.Holder>() {
    lateinit var context: Context

    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    var storageReferences: StorageReference = Firebase.storage.reference

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = PostLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.post_layout, parent, false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val elemento = lista[position]
        var imagenRef = storageReferences.child("usuarios/"+correo+"/default.png")
        if (imagenRef!=null){
            imagenRef.downloadUrl
                .addOnSuccessListener {
                    Picasso.get().load(it).resize(300,300).centerCrop().into(holder.binding.imgUser)
                }
                .addOnFailureListener {

                }
        }
        holder.binding.txtNombrePost.text = elemento.nombre
        holder.binding.txtTextoPost.text = elemento.texto
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

}