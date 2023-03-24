package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class EntrenarAdapter(
    private var trainingList: List<Entrenamiento>,
    private val onClickListener: (Entrenamiento) -> Unit
) : RecyclerView.Adapter<EntrenarViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EntrenarViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return EntrenarViewHolder(layoutInflater.inflate(R.layout.item_entrenar, parent, false))
    }

    override fun onBindViewHolder(holder: EntrenarViewHolder, position: Int) {
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