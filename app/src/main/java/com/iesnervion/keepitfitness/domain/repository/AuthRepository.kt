package com.iesnervion.keepitfitness.domain.repository

import com.iesnervion.keepitfitness.util.Resource

interface AuthRepository {

    /**
     * Loguea a un usuario con email y contraseña y devuelve un String con el UID
     * @param email String con el email
     * @param password String con la contraseña
     * @return String con el UID
     */
    suspend fun login (email:String, password:String) : String

    /**
     * Registra a un usuario con email y contraseña y devuelve un String con el UID
     * @param email String con el email
     * @param password String con la contraseña
     * @return String con el UID
     */
    suspend fun signup (email:String, password:String) : String

}