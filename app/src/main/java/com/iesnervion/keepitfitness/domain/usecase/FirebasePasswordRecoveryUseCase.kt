package com.iesnervion.keepitfitness.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebasePasswordRecoveryUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    /**
     * Envia un correo de recuperación de contraseña a la dirección de correo electrónico del usuario.
     * @param email Email del usuario.
     * @return Devuelve un Flow<Resource<Boolean>> que indica los estados de la operación.
     */
    suspend operator fun invoke(email: String): Flow<Resource<Boolean>> = flow {
        try {
            emit(Resource.Loading)
            var isSuccessful = false
            firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener { isSuccessful = it.isSuccessful }
                .await()
            emit(Resource.Success(isSuccessful))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}