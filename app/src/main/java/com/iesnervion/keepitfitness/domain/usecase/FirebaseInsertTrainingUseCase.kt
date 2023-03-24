package com.iesnervion.keepitfitness.domain.usecase

import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.repository.TrainRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseInsertTrainingUseCase @Inject constructor(
    private val trainRepository: TrainRepository
) {
    suspend operator fun invoke(entrenamiento: Entrenamiento): Flow<Resource<Boolean>> = flow {

        try {
            emit(Resource.Loading)

            val isSucces: Boolean = trainRepository.insertTraining(entrenamiento)
            emit(Resource.Success(isSucces))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}