package com.iesnervion.keepitfitness.domain.model

/**
 * Clase de datos que captura la información del usuario
 * @param uid User ID
 * @param email User email
 */
data class User(
    val uid: String = "",
    val email: String = ""
)
