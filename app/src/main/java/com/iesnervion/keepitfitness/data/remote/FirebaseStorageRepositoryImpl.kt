package com.iesnervion.keepitfitness.data.remote

import android.net.Uri
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.IMAGES_FOLDER_NAME
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.domain.repository.StorageRepository
import kotlinx.coroutines.tasks.await
import java.net.URI
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
                isSuccesfull = it.isSuccessful
            }.await()
        return isSuccesfull
    }

    override suspend fun getPhotoURL(): Uri? {
        val storageRef = firebaseStorage.reference.child(IMAGES_FOLDER_NAME).child(firebaseAuth.currentUser?.uid ?: "")
        var uri: Uri? = null

        storageRef.downloadUrl.addOnSuccessListener {
            uri = it
        }.await()

        return uri
    }

}