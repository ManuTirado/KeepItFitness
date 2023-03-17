package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseSignUpUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val isSignedUpSuccessfully = authRepository.signup(email, password)
        if (isSignedUpSuccessfully) {
            emit(Resource.Success(true))
        } else {
            emit(Resource.Error("SignUp error"))
        }
    }
}