package com.iesnervion.keepitfitness.data.remote

import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.EXERCISE_IMAGES_FOLDER_NAME
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.IMAGES_FOLDER_NAME
import com.iesnervion.keepitfitness.domain.repository.StorageRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseStorage: FirebaseStorage
): StorageRepository {

    override suspend fun uploadImage(uri: Uri): Boolean {
        var isSuccesfull = false

        val filename = firebaseAuth.currentUser?.uid

        val storageReference = firebaseStorage.getReference("$IMAGES_FOLDER_NAME/$filename")

        storageReference.putFile(uri)
            .addOnCompleteListener {

            }.continueWith {
                isSuccesfull = it.isSuccessful
            }.await()
        return isSuccesfull
    }

    override suspend fun uploadExerciseImage(uri: Uri, exerciseId: String): Boolean {
        var isSuccesfull = false

        val uid = firebaseAuth.currentUser?.uid

        val storageReference = firebaseStorage.getReference("$EXERCISE_IMAGES_FOLDER_NAME/$uid/$exerciseId")

        storageReference.putFile(uri)
            .addOnCompleteListener {

            }.continueWith {
                isSuccesfull = it.isSuccessful
            }.await()
        return isSuccesfull
    }

    override suspend fun getPhotoURL(): Uri? {
        val storageRef = firebaseStorage.reference.child(IMAGES_FOLDER_NAME).child(firebaseAuth.currentUser?.uid ?: "")
        var uri: Uri? = null

        storageRef.downloadUrl.addOnCompleteListener {

        }.continueWith {
            if (it.isSuccessful) {
                uri = it.result
            }
        }.await()
        return uri
    }

    override suspend fun getExercisePhotoURL(exerciseId: String): Uri? {
        val storageRef = firebaseStorage.reference.child(EXERCISE_IMAGES_FOLDER_NAME).child(firebaseAuth.currentUser?.uid ?: "").child(exerciseId)
        Log.i("IMAGE", storageRef.path)
        var uri: Uri? = null

        storageRef.downloadUrl.addOnCompleteListener {

        }.continueWith {
            if (it.isSuccessful) {
                uri = it.result
            }
        }.await()
        return uri
    }
}