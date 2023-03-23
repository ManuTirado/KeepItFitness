package com.iesnervion.keepitfitness.domain.model

data class Entrenamiento(
    var id:String = "",
    var desc:String = "",
    var list: List<EjercicioEntrenamiento> = emptyList()
)
