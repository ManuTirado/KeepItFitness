package com.iesnervion.keepitfitness.ui.tabEntrenar.pre_realizar_entrenamiento.recyclerview

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ItemEjercicioBinding
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento

class PreRealizarEntrenamientoViewHolder(view: View) :
    RecyclerView.ViewHolder(view) {

    val binding = ItemEjercicioBinding.bind(view)

    fun render(ejercicioModel: EjercicioEntrenamiento, onClickListener: (EjercicioEntrenamiento) -> Unit) {
        with(binding) {
            Glide.with(ivExercisePhoto.context).load(ejercicioModel.exercise.photo).placeholder(R.drawable.ic_dumbbell)
                .into(binding.ivExercisePhoto)
            tvExerciseName.text = ejercicioModel.exercise.name
            tvExerciseWeight.text = ejercicioModel.weight.toString().plus("kg")
            tvExerciseReps.text = ejercicioModel.reps.toString()

            itemView.setOnClickListener {
                onClickListener(ejercicioModel)
            }
        }
    }
}