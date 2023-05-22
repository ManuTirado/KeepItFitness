package com.iesnervion.keepitfitness.ui.tabProgreso.progreso.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado

class ProgresoAdapter(
    private var trainingList: List<EntrenamientoRealizado>,
    private val onClickListener: (EntrenamientoRealizado) -> Unit
) : RecyclerView.Adapter<ProgresoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProgresoViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ProgresoViewHolder(layoutInflater.inflate(R.layout.item_entrenar, parent, false))
    }

    override fun onBindViewHolder(holder: ProgresoViewHolder, position: Int) {
        val item = trainingList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return trainingList.size
    }

    fun updateData(data: List<EntrenamientoRealizado>) {
        trainingList = data
        notifyDataSetChanged()
    }
}