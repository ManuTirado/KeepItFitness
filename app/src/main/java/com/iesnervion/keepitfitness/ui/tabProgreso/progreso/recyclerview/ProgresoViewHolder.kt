package com.iesnervion.keepitfitness.ui.tabProgreso.progreso.recyclerview

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.iesnervion.keepitfitness.databinding.ItemEntrenarBinding
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado

class ProgresoViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemEntrenarBinding.bind(view)

    fun render(entrenamientoModel: EntrenamientoRealizado, onClickListener: (EntrenamientoRealizado) -> Unit) {
        with(binding) {
            tvTrainingName.text = "${entrenamientoModel.ejercicios.size} ejercicios"
            tvTrainingDesc.text = entrenamientoModel.desc
            tvTime.text = entrenamientoModel.time
        }
        itemView.setOnClickListener {
            onClickListener(entrenamientoModel)
        }
    }
}