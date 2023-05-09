package com.iesnervion.keepitfitness.domain.usecase

import android.net.Uri
import com.iesnervion.keepitfitness.domain.repository.StorageRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class FirebaseUploadImageUseCase @Inject constructor(
    private val storageRepository: StorageRepository
) {
    suspend operator fun invoke(uri: Uri): Flow<Resource<Boolean>> = flow {

        try {
            emit(Resource.Loading)

            val isSucces: Boolean = storageRepository.uploadImage(uri)
            emit(Resource.Success(isSucces))
            emit(Resource.Finished)
        } catch (e: Exception) {
            emit(Resource.Error(e.message.toString()))
            emit(Resource.Finished)
        }
    }
}
