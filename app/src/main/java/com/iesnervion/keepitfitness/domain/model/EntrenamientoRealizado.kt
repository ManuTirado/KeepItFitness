package com.iesnervion.keepitfitness.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot

data class EntrenamientoRealizado(
    var id: String = "",
    var desc: String = "",
    var ejercicios: List<EjercicioEntrenamiento> = emptyList(),
    var fecha: Timestamp = Timestamp.now(),
    var time: String = ""
) {
    companion object {
        fun fromDocument(document: DocumentSnapshot): EntrenamientoRealizado {
            val id = document.id
            val desc = document.getString("desc") ?: ""
            val time = document.getString("time") ?: ""
            val fecha = document.getTimestamp("fecha") ?: Timestamp.now()
            val ejerciciosList = document.get("ejercicios") as List<Map<String, Any>>
            val ejercicios: List<EjercicioEntrenamiento> = ejerciciosList.map { ejercicioMap ->
                return@map EjercicioEntrenamiento.fromMap(ejercicioMap)
            }
            return EntrenamientoRealizado(id, desc, ejercicios, fecha, time)
        }
    }
}

