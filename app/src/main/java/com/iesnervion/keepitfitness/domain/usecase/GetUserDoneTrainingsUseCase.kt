package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserDoneTrainingsUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    suspend operator fun invoke(userId: String): Flow<Resource<List<EntrenamientoRealizado>>> = flow {

        try {
            emit(Resource.Loading)

            val trainings = userRepository.getUserTrains(userId)
            emit(Resource.Success(trainings))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}