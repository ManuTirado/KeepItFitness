package com.iesnervion.keepitfitness.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable
import java.util.*

data class EntrenamientoRealizado(
    var id: String = "",
    var desc: String = "",
    var ejercicios: List<EjercicioEntrenamiento> = emptyList(),
    var fecha: Date = Timestamp.now().toDate(),
    var time: String = ""
) : Serializable

{
    companion object {
        fun fromDocument(document: DocumentSnapshot): EntrenamientoRealizado {
            val id = document.id
            val desc = document.getString("desc") ?: ""
            val time = document.getString("time") ?: ""
            val fecha = document.getTimestamp("fecha")?.toDate() ?: Timestamp.now().toDate()
            val ejerciciosList = document.get("ejercicios") as List<Map<String, Any>>
            val ejercicios: List<EjercicioEntrenamiento> = ejerciciosList.map { ejercicioMap ->
                return@map EjercicioEntrenamiento.fromMap(ejercicioMap)
            }
            return EntrenamientoRealizado(id, desc, ejercicios, fecha, time)
        }
    }
}

