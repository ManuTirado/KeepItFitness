package com.iesnervion.keepitfitness.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth  // Instancia de tipo singleton de FirebaseAuth
) : AuthRepository {

    /**
     * Realiza el login en Firebase con el email y la contraseña pasados como parámetros.
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return UID si está logueado, o un String vacío en caso contrario o si ocurre algún error.
     */
    override suspend fun login(email: String, password: String): String {
        return try {
            var userUID = ""
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                }.continueWith {
                    if (it.isSuccessful) {
                        userUID = it.result.user?.uid ?: ""
                    }
                }.await()
            userUID
        } catch (e: Exception) {
            ""
        }
    }

    /**
     * Realiza el registro en Firebase con el email y la contraseña pasados como parámetros.
     * @param email Email del usuario
     * @param password Contraseña del usuario
     * @return UID si está logueado, o un String vacío en caso contrario o si ocurre algún error.
     */
    override suspend fun signup(email: String, password: String): String {
        return try {
            var userUID = ""
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener {

                }.continueWith {
                    if (it.isSuccessful) {
                        userUID = it.result.user?.uid ?: ""
                    }
                }.await()
            userUID
        } catch (e: Exception) {
            ""
        }
    }

}