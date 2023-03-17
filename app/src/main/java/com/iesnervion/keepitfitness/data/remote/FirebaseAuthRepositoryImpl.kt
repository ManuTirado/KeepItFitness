package com.iesnervion.keepitfitness.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.repository.AuthRepository
import com.iesnervion.keepitfitness.util.Resource
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    override suspend fun login(email: String, password: String): Boolean {
        return try {
            var isSuccesfull: Boolean = false
            firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnSuccessListener { isSuccesfull = true }
                .addOnFailureListener { isSuccesfull = false }
                .await()
            isSuccesfull
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun signup(email: String, password: String): Boolean {
        return try {
            var isSuccesfull: Boolean = false
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { isSuccesfull = it.isSuccessful }
                .await()
            isSuccesfull
        } catch (e: Exception) {
            false
        }
    }

}