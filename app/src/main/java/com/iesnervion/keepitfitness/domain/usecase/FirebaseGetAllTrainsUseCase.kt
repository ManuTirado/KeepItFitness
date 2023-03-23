package com.iesnervion.keepitfitness.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.TrainRepository
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirebaseGetAllTrainsUseCase @Inject constructor(
    private val trainRepository: TrainRepository
) {
    suspend operator fun invoke(): Flow<Resource<List<Entrenamiento>>> = flow {

        try {
            emit(Resource.Loading)

            val trainings = withContext(Dispatchers.IO) {
                trainRepository.getAllTrains()
            }

            emit(Resource.Success(trainings))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}