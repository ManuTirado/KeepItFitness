package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Inicia sesión en la aplicación con el email y la contraseña del usuario.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @return Devuelve un Flow<Resource<User>> que indica los estados de la operación.
     */
    suspend operator fun invoke(email: String, password: String): Flow<Resource<User>> = flow {

        emit(Resource.Loading)
        val userUID = authRepository.login(email, password)
        if (userUID.isNotEmpty()) {
            val user = userRepository.getUser(userUID)

            emit(Resource.Success(user))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Login error"))
            emit(Resource.Finished)
        }
    }
}