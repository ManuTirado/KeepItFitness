package com.iesnervion.keepitfitness.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.USERS_COLLECTION
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserRepositoryImpl @Inject constructor(

) : UserRepository {

    /**
     * Crea un usuario en la base de datos, en caso de que ya exista, se actualiza con los nuevos datos del usuario.
     * @param user Usuario a crear
     * @return true si se ha creado correctamente, false en caso contrario
     */
    override suspend fun createUser(user: User): Boolean {
        return try {
            var isSuccesfull = false
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(user.uid)
                .set(user, SetOptions.merge())
                .addOnCompleteListener { isSuccesfull = it.isSuccessful }
                .await()
            isSuccesfull
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Devuelve el usuario con el uid pasado, en caso de no existir u ocurrir un error,
     * se devuelve un objeto usuario vacío
     * @param uid uid del usuario a buscar
     * @return Usuario con el uid pasado o un objeto vacío en caso de no existir
     */
    override suspend fun getUser(uid: String): User {
        return try {
            var loggedUser = User()
            FirebaseFirestore.getInstance().collection(USERS_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener {
                    loggedUser = it.toObject(User::class.java)!!
                }
                .await()
            loggedUser
        } catch (e: Exception) {
            User()
        }
    }

    override suspend fun insertTrainingToUserDocument(
        training: EntrenamientoRealizado,
        userId: String
    ): Boolean {
        var isSuccesful = false
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(USERS_COLLECTION).document(userId)
        val trainingDocRef = userDocRef.collection("entrenamientos")

        db.runTransaction { transaction ->
            // Obtenemos el documento del usuario y verificamos que exista
            val userDoc = transaction.get(userDocRef)
            if (!userDoc.exists()) {
                throw Exception("El usuario no existe")
            }

            // Añadimos el entrenamiento a la lista de entrenamientos del usuario
            val newTrainingDocRef = trainingDocRef.document()
            val newTrainingId = newTrainingDocRef.id
            transaction.update(
                userDocRef,
                "entrenamientos",
                FieldValue.arrayUnion(newTrainingId)
            )

            // Insertamos el entrenamiento en su colección correspondiente
            transaction.set(newTrainingDocRef, training)

            // Devolvemos null, ya que no necesitamos retornar nada de la transacción
            null
        }
            .addOnSuccessListener {
                // El entrenamiento se ha insertado correctamente
                isSuccesful = true
            }
            .addOnFailureListener { exception ->
                // Ha ocurrido un error al insertar el entrenamiento
                isSuccesful = false
            }
            .await()
        return  isSuccesful
    }
}
