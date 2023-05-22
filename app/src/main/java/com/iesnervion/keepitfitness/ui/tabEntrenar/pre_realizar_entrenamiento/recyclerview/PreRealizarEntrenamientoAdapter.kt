package com.iesnervion.keepitfitness.ui.tabEntrenar.pre_realizar_entrenamiento.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento

class PreRealizarEntrenamientoAdapter(
    private var exerciseList: List<EjercicioEntrenamiento>,
    private val onClickListener: (EjercicioEntrenamiento) -> Unit
) : RecyclerView.Adapter<PreRealizarEntrenamientoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreRealizarEntrenamientoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return PreRealizarEntrenamientoViewHolder(layoutInflater.inflate(R.layout.item_ejercicio, parent, false))
    }

    override fun onBindViewHolder(holder: PreRealizarEntrenamientoViewHolder, position: Int) {
        val item = exerciseList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return exerciseList.size
    }
}