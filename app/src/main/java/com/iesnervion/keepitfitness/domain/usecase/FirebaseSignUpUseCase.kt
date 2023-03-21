package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) {

    /**
     * Este método se encarga de realizar el registro de un usuario en Firebase.
     * Si el registro es correcto, se crea un usuario en la base de datos de Firestore.
     * @param email Email del usuario.
     * @param password Contraseña del usuario.
     * @return Devuelve un Flow<Resource<Boolean>> que nos indica los estados de la operación.
     */
    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val userUID = authRepository.signup(email, password)
        if (userUID.isNotEmpty()) {
            userRepository.createUser(
                User(
                    uid = userUID,
                    email = email
                )
            )
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("SignUp error"))
        }
    }
}