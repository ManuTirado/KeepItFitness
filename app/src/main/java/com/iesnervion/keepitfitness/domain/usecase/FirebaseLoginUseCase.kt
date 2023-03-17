package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseLoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {

    suspend operator fun invoke(email: String, password: String): Flow<Resource<Boolean>> = flow {
        emit(Resource.Loading)
        val isLoggedSuccessfully = authRepository.login(email, password)
        if (isLoggedSuccessfully) {
            emit(Resource.Success(true))
            emit(Resource.Finished)
        } else {
            emit(Resource.Error("Login error"))
            emit(Resource.Finished)
        }
    }
}