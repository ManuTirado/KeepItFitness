package com.iesnervion.keepitfitness.ui.entrenar.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.databinding.ItemEntrenarBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento

class EntrenarViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemEntrenarBinding.bind(view)

    fun render(entrenamientoModel: Entrenamiento, onClickListener: (Entrenamiento) -> Unit) {
        with(binding) {
            tvTrainingName.text = entrenamientoModel.id
            tvTrainingDesc.text = entrenamientoModel.desc
            tvTime.text = entrenamientoModel.time
        }
        itemView.setOnClickListener {
            onClickListener(entrenamientoModel)
        }
    }
}