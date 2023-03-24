package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.databinding.ItemEntrenarBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class EntrenarViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemEntrenarBinding.bind(view)

    fun render(entrenamientoModel: Entrenamiento, onClickListener: (Entrenamiento) -> Unit) {
        binding.tvTrainingName.text = entrenamientoModel.id
        binding.tvTrainingDesc.text = entrenamientoModel.desc

        itemView.setOnClickListener {
            onClickListener(entrenamientoModel)
        }
    }
}