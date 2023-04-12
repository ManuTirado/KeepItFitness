package com.iesnervion.keepitfitness.domain.model

import com.google.firebase.Timestamp

data class EntrenamientoRealizado(
    var id:String = "",
    var desc:String = "",
    var time: String = "",
    var fecha: Timestamp = Timestamp.now(),
    var ejercicios: List<EjercicioEntrenamiento> = emptyList()
)