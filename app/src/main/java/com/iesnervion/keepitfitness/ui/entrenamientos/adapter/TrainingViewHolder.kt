package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.databinding.ItemTrainingBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class TrainingViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemTrainingBinding.bind(view)

    fun render(entrenamientoModel: Entrenamiento, onClickListener: (Entrenamiento) -> Unit) {
        binding.tvTrainingName.text = entrenamientoModel.id
        binding.tvTrainingDesc.text = entrenamientoModel.desc

        binding.ivEdit.setOnClickListener {
            onClickListener(entrenamientoModel)
        }
    }
}