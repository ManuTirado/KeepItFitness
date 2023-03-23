package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class TrainingAdapter(
    private var trainingList: List<Entrenamiento>,
    private val onClickListener: (Entrenamiento) -> Unit
) : RecyclerView.Adapter<TrainingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrainingViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TrainingViewHolder(layoutInflater.inflate(R.layout.item_training, parent, false))
    }

    override fun onBindViewHolder(holder: TrainingViewHolder, position: Int) {
        val item = trainingList[position]
        holder.render(item, onClickListener)
    }

    override fun getItemCount(): Int {
        return trainingList.size
    }

    fun updateData(data: List<Entrenamiento>) {
        Log.i("Navigation","Function 'updateData' executed")
        trainingList = data
        notifyDataSetChanged()
    }
}