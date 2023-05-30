package com.iesnervion.keepitfitness.domain.usecase

import android.net.Uri
import com.iesnervion.keepitfitness.domain.repository.StorageRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebseGetImageUrlUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(): Flow<Resource<Uri>> = flow {

        try {
            emit(Resource.Loading)

            val uri: Uri? = storageRepository.getPhotoURL()
            if (uri != null) {
                emit(Resource.Success(uri))
            } else {
                emit(Resource.Error("Error obtaining image URL"))
            }
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}

class FirebseGetExerciseImageUrlUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(exerciseId: String): Flow<Resource<Uri>> = flow {

        try {
            emit(Resource.Loading)

            val uri: Uri? = storageRepository.getExercisePhotoURL(exerciseId)
            if (uri != null) {
                emit(Resource.Success(uri))
            } else {
                emit(Resource.Error("Error obtaining image URL"))
            }
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}