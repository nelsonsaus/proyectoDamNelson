package com.example.miaprueba

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.usuarios_layout.view.*

class UsuariosAdapter(private val usuarios: List<Usuarios>) : RecyclerView.Adapter<UsuariosAdapter.ViewHolder>(){

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UsuariosAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.usuarios_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UsuariosAdapter.ViewHolder, position: Int) {

        holder.view.tvNombre.text = usuarios[position].nombre
        holder.view.tvCorreo.text = usuarios[position].nombre

        if(usuarios[position].imagen!=null)
            Picasso.get().load(usuarios[position].imagen).into(holder.view.imageView)
    }

    override fun getItemCount(): Int = usuarios.size

}