package com.iesnervion.keepitfitness.domain.model

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable

data class Entrenamiento(
    var id:String = "",
    var desc:String = "",
    var time: String = "",
    var ejercicios: List<EjercicioEntrenamiento> = emptyList()
): Serializable

{
    companion object {
        fun fromDocument(document: DocumentSnapshot): Entrenamiento {
            val id = document.id
            val desc = document.getString("desc") ?: ""
            val time = document.getString("time") ?: ""
            val ejerciciosList = document.get("ejercicios") as List<Map<String, Any>>
            val ejercicios: List<EjercicioEntrenamiento> = ejerciciosList.map { ejercicioMap ->
                return@map EjercicioEntrenamiento.fromMap(ejercicioMap)
            }
            return Entrenamiento(id, desc, time, ejercicios)
        }
    }
}
