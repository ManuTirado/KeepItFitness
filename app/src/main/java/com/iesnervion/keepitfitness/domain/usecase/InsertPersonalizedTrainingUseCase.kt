package com.iesnervion.keepitfitness.domain.usecase

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class InsertPersonalizedTrainingUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val auth: FirebaseAuth
) {

    suspend operator fun invoke(entrenamiento: Entrenamiento): Flow<Resource<Boolean>> = flow {

        try {
            emit(Resource.Loading)

            val isSuccess = userRepository.insertPersonalizedTrainingToUserDocument(entrenamiento, auth.uid.toString())
            emit(Resource.Success(isSuccess))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}