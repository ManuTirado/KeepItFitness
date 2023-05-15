package com.iesnervion.keepitfitness.domain.model

import com.google.firebase.firestore.DocumentSnapshot
import java.io.Serializable

data class Ejercicio(
    var id: String = "",
    var photo: String = "",
    var name: String = "",
    var type:String = ""
) : Serializable
{
    companion object {
        fun fromMap(map: Map<String, Any>): Ejercicio {
            val id = map["id"] as? String ?: ""
            val photo = map["photo"] as? String ?: ""
            val name = map["name"] as? String ?: ""
            val type = map["type"] as? String ?: ""
            return Ejercicio(id, photo, name, type)
        }
    }
}
