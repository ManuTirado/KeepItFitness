package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.repository.TrainRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseGetTrainingUseCase @Inject constructor(
    private val trainRepository: TrainRepository
) {
    suspend operator fun invoke(id: String): Flow<Resource<Entrenamiento>> = flow {

        try {
            emit(Resource.Loading)

            val training = trainRepository.getTraining(id)
            emit(Resource.Success(training))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}