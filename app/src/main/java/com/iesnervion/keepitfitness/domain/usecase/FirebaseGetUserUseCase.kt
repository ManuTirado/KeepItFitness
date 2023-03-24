package com.iesnervion.keepitfitness.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseGetUserUseCase @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val userRepository: UserRepository
) {
    /**
     *
     */
    suspend operator fun invoke(): Flow<Resource<User>> = flow {

        emit(Resource.Loading)
        val userUID = firebaseAuth.uid
        if (!userUID.isNullOrEmpty()) {
            val user =  userRepository.getUser(userUID)

            emit(Resource.Success(user))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Error obteniendo el usuario"))
            emit(Resource.Finished)
        }
    }
}