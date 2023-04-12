package com.iesnervion.keepitfitness.domain.model

data class EjercicioEntrenamiento(
    var exercise: Ejercicio,
    var reps: Long,
    var weight: Long
)