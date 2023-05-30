package com.iesnervion.keepitfitness.data.remote

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.USERS_COLLECTION
import com.iesnervion.keepitfitness.domain.model.*
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
                .addOnCompleteListener {

                }.continueWith {
                    isSuccesfull = it.isSuccessful
                }.await()
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

                }.continueWith {
                    loggedUser = it.result.toObject(User::class.java)!!
                }.await()
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
            .addOnCompleteListener {

            }.continueWith {
                isSuccesful = it.isSuccessful
            }.await()
        return  isSuccesful
    }

    override suspend fun getUserTrains(userId: String): List<EntrenamientoRealizado> {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(USERS_COLLECTION).document(userId)
        val trainingsCollectionRef = userDocRef.collection("entrenamientos")

        var trainings: List<EntrenamientoRealizado> = emptyList()

        trainingsCollectionRef.get()
            .addOnCompleteListener {

            }.continueWith {
                if (it.isSuccessful) {
                    val trainingsList = mutableListOf<EntrenamientoRealizado>()
                    for (document in it.result) {
                        val entrenamiento = EntrenamientoRealizado.fromDocument(document)
                        trainingsList.add(entrenamiento)
                    }
                    trainings = trainingsList.toList()
                }
            }.await()
        return  trainings
    }

    override suspend fun insertPersonalizedTrainingToUserDocument(
        training: Entrenamiento,
        userId: String
    ): Boolean {
        var isSuccesful = false
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(USERS_COLLECTION).document(userId)
        val trainingDocRef = userDocRef.collection("entrenamientosPersonalizados")

        db.runTransaction { transaction ->
            // Obtenemos el documento del usuario y verificamos que exista
            val userDoc = transaction.get(userDocRef)
            if (!userDoc.exists()) {
                throw Exception("El usuario no existe")
            }

            // Añadimos el entrenamiento a la lista de entrenamientos del usuario
            val newTrainingDocRef = trainingDocRef.document(training.id)
            val newTrainingId = training.id
            transaction.update(
                userDocRef,
                "entrenamientosPersonalizados",
                FieldValue.arrayUnion(newTrainingId)
            )

            // Insertamos el entrenamiento en su colección correspondiente
            transaction.set(newTrainingDocRef, training)

            // Devolvemos null, ya que no necesitamos retornar nada de la transacción
            null
        }
            .addOnCompleteListener {

            }.continueWith {
                isSuccesful = it.isSuccessful
            }.await()
        return  isSuccesful
    }

    override suspend fun getUserPersonalizedTrains(userId: String): List<Entrenamiento> {
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(USERS_COLLECTION).document(userId)
        val trainingsCollectionRef = userDocRef.collection("entrenamientosPersonalizados")

        var trainings: List<Entrenamiento> = emptyList()

        trainingsCollectionRef.get()
            .addOnCompleteListener {

            }.continueWith {
                if (it.isSuccessful) {
                    val trainingsList = mutableListOf<Entrenamiento>()
                    for (document in it.result) {
                        val entrenamiento = Entrenamiento.fromDocument(document)
                        trainingsList.add(entrenamiento)
                    }
                    trainings = trainingsList.toList()
                }
            }.await()
        return trainings
    }

    override suspend fun deletePersonalizedTrainingFromUserDocument(trainingId: String, userId: String): Boolean {
        var isSuccessful = false
        val db = FirebaseFirestore.getInstance()
        val userDocRef = db.collection(USERS_COLLECTION).document(userId)
        val trainingDocRef =
            userDocRef.collection("entrenamientosPersonalizados").document(trainingId)

        db.runTransaction { transaction ->
            // Obtenemos el documento del usuario y verificamos que exista
            val userDoc = transaction.get(userDocRef)
            if (!userDoc.exists()) {
                throw Exception("El usuario no existe")
            }

            // Eliminamos el entrenamiento de la lista de entrenamientos del usuario
            transaction.update(
                userDocRef,
                "entrenamientosPersonalizados",
                FieldValue.arrayRemove(trainingId)
            )

            // Eliminamos el entrenamiento de su colección correspondiente
            transaction.delete(trainingDocRef)

            // Devolvemos null, ya que no necesitamos retornar nada de la transacción
            null
        }
            .addOnCompleteListener {

            }.continueWith {
                isSuccessful = it.isSuccessful
            }.await()
        return isSuccessful
    }
}
