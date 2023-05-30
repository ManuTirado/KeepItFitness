package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeletePersonalizedTrainingUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(userId: String, trainingId: String): Flow<Resource<Boolean>> = flow {

        try {
            emit(Resource.Loading)

            val isSuccess = userRepository.deletePersonalizedTrainingFromUserDocument(trainingId, userId)
            emit(Resource.Success(isSuccess))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}