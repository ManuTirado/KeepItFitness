package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetPersonalizedTrainingsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: String): Flow<Resource<List<Entrenamiento>>> = flow {

        try {
            emit(Resource.Loading)

            val trainings = userRepository.getUserPersonalizedTrains(userId)
            emit(Resource.Success(trainings))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}