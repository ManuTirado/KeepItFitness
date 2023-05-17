package com.iesnervion.keepitfitness.ui.insertar_editar_entrenamiento.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.ui.entrenamientos.adapter.EntrenamientosViewHolder

class InsertarEditarAdapter(
    private var exerciseList: List<EjercicioEntrenamiento>,
    private val onClickListener: (EjercicioEntrenamiento) -> Unit
) : RecyclerView.Adapter<InsertarEditarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InsertarEditarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return InsertarEditarViewHolder(
            layoutInflater.inflate(
                R.layout.item_insertar_editar_ejercicio,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: InsertarEditarViewHolder, position: Int) {
        val item = exerciseList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }

    fun updateData(data: List<EjercicioEntrenamiento>) {
        exerciseList = data
        notifyDataSetChanged()
    }
}