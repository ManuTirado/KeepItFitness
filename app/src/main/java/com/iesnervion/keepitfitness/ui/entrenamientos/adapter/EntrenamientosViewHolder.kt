package com.iesnervion.keepitfitness.ui.entrenamientos.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.databinding.ItemEntrenamientosBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class EntrenamientosViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemEntrenamientosBinding.bind(view)

    fun render(entrenamientoModel: Entrenamiento, onClickListener: (Entrenamiento) -> Unit) {
        binding.tvTrainingName.text = entrenamientoModel.id
        binding.tvTrainingDesc.text = entrenamientoModel.desc

        binding.ivEdit.setOnClickListener {
            onClickListener(entrenamientoModel)
        }
    }
}