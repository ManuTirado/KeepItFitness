package com.iesnervion.keepitfitness.domain.repository

import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.model.User

interface UserRepository {

    /**
     * Crea un nuevo usuario en la base de datos y devuelve true si se ha creado correctamente
     * @param user Usuario a crear
     * @return Boolean que indica si se ha creado correctamente
     */
    suspend fun createUser(user: User): Boolean

    /**
     * Devuelve un usuario de la base de datos a partir de su uid
     * @param uid uid del usuario
     * @return Usuario con el uid indicado
     */
    suspend fun getUser(uid:String): User

    suspend fun insertTrainingToUserDocument(training: EntrenamientoRealizado, userId: String): Boolean

    suspend fun getUserTrains(userId: String): List<EntrenamientoRealizado>

    suspend fun insertPersonalizedTrainingToUserDocument(training: Entrenamiento, userId: String): Boolean

    suspend fun getUserPersonalizedTrains(userId: String): List<Entrenamiento>
}