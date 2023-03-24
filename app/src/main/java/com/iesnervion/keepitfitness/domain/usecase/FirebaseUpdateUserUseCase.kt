package com.iesnervion.keepitfitness.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseUpdateUserUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) {
    /**
     *
     */
    suspend operator fun invoke(user: User): Flow<Resource<Boolean>> = flow {

        emit(Resource.Loading)
        val userUID = firebaseAuth.uid
        user.uid = userUID.toString()
        if (!userUID.isNullOrEmpty()) {
            userRepository.createUser(user)

            emit(Resource.Success(true))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Error actualizando el usuario"))
            emit(Resource.Finished)
        }
    }
}