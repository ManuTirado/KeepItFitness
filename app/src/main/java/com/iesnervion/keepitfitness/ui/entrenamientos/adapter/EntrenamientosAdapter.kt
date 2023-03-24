package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class EntrenamientosAdapter(
    private var trainingList: List<Entrenamiento>,
    private val onClickListener: (Entrenamiento) -> Unit
) : RecyclerView.Adapter<EntrenamientosViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenamientosViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EntrenamientosViewHolder(layoutInflater.inflate(R.layout.item_entrenamientos, parent, false))
    }

    override fun onBindViewHolder(holder: EntrenamientosViewHolder, position: Int) {
        val item = trainingList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return trainingList.size
    }

    fun updateData(data: List<Entrenamiento>) {
        trainingList = data
        notifyDataSetChanged()
    }
}