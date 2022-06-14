package com.example.miaprueba

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.miaprueba.databinding.EjerciciosLayoutBinding
import com.example.miaprueba.databinding.UsuariosLayoutBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class EjerciciosAdapter(private val lista: MutableList<Ejercicios>): RecyclerView.Adapter<EjerciciosAdapter.Holder>() {
    lateinit var context : Context

    private lateinit var db: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    class Holder(v: View): RecyclerView.ViewHolder(v) {
        val binding = EjerciciosLayoutBinding.bind(v)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        context=parent.context
        val layoutInflater= LayoutInflater.from(parent.context)
        val v = layoutInflater.inflate(R.layout.ejercicios_layout,parent,false)
        return Holder(v)
    }

    override fun onBindViewHolder(holder: EjerciciosAdapter.Holder, position: Int) {
        val elemento= lista[position]
        holder.binding.txtNombreEjer.text=elemento.nombre
        holder.binding.txtDescanso.text= elemento.descanso.toString()+" seg. descanso"
        holder.binding.txtDuracion.text= elemento.duracion.toString()+" segundos"
    }

    override fun getItemCount(): Int {
        return lista.count()
    }

    fun getItem(position: Int): Ejercicios? {
        return lista.get(position)
    }

    fun changeColor(posiciones: MutableList<Int>){



    }
}