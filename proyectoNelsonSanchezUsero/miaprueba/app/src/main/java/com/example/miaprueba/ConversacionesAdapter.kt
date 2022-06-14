package com.example.miaprueba

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miaprueba.databinding.ChatLayoutBinding
import com.example.miaprueba.databinding.PostLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso

class ConversacionesAdapter(private val lista: MutableList<Conversaciones>, private val correo: String): RecyclerView.Adapter<ConversacionesAdapter.Holder>() {
    lateinit var context: Context

    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    var storageReferences: StorageReference = Firebase.storage.reference

    class Holder(v: View) : RecyclerView.ViewHolder(v) {
        val binding = ChatLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context = parent.context
        val layoutInflater = LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.chat_layout, parent, false)
        return Holder(v)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val elemento = lista[position]
        if(correo==elemento.correoEmisor.toString()){
            holder.binding.cardMensajeChat.setBackgroundColor(R.color.verde)

        }
        holder.binding.txtNombreChatLayout.text = elemento.nombreEmisor.toString()
        holder.binding.txtTextoChatLayout.text = elemento.mensaje.toString()
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

}