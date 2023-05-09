package com.iesnervion.keepitfitness.domain.model

data class EjercicioEntrenamiento(
    var exercise: Ejercicio = Ejercicio(),
    var reps: Long = 0,
    var weight: Long = 0
) {
    companion object {
        fun fromMap(map: Map<String, Any>): EjercicioEntrenamiento {
            val reps = map["reps"] as? Long ?: 0
            val weight = map["weight"] as? Long ?: 0
            val exercise = Ejercicio.fromMap(map["exercise"] as Map<String, Any>)
            return EjercicioEntrenamiento(exercise, reps, weight)
        }
    }
}