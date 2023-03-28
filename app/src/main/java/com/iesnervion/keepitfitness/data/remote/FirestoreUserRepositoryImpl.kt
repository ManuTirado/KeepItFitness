package com.iesnervion.keepitfitness.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.USERS_COLLECTION
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
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
        training: Entrenamiento,
        userId: String
    ): Boolean {
        return try {
            val db = Firebase.firestore
            var inserted = false

            val userDocRef = db.collection(USERS_COLLECTION).document(userId)

            db.runTransaction { transaction ->
                val userData = transaction.get(userDocRef)

                if (!userData.exists() || userData["entrenamientos"] == null) {
                    // Si el documento no existe, o el campo "entrenamientos" es nulo, creamos una lista vacía
                    transaction.set(
                        userDocRef,
                        hashMapOf("entrenamientos" to arrayListOf<Entrenamiento>())
                    )
                }

                val entrenamientos = userData["entrenamientos"] as ArrayList<HashMap<String, Any>>

                // Agregamos el objeto Entrenamiento a la lista existente, o a la lista creada anteriormente
                entrenamientos.add(hashMapOf(
                    "id" to training.id,
                    "desc" to training.desc,
                    "ejercicios" to training.ejercicios.map { ejercicioEntrenamiento ->
                        hashMapOf(
                            "excersice" to hashMapOf(
                                "id" to ejercicioEntrenamiento.excersice.id,
                                "photo" to ejercicioEntrenamiento.excersice.photo,
                                "name" to ejercicioEntrenamiento.excersice.name,
                                "type" to ejercicioEntrenamiento.excersice.type
                            ),
                            "reps" to ejercicioEntrenamiento.reps,
                            "weight" to ejercicioEntrenamiento.weight
                        )
                    }
                ))

                // Actualizamos la lista de entrenamientos
                transaction.update(userDocRef, "entrenamientos", entrenamientos)
                return@runTransaction
            }.addOnSuccessListener {
                inserted = true
            }.addOnFailureListener {
                inserted = false
            }.await()
            inserted
        } catch (e: Exception) {
            false
        }
//        return try {
//            val db = Firebase.firestore
//            var inserted = false
//
//            val trainingMap = hashMapOf(
//                "id" to training.id,
//                "desc" to training.desc,
//                "ejercicios" to training.ejercicios.mapIndexed { index, ejercicio ->
//                    index.toString() to hashMapOf(
//                        "excersice" to hashMapOf(
//                            "id" to ejercicio.excersice.id,
//                            "photo" to ejercicio.excersice.photo,
//                            "name" to ejercicio.excersice.name,
//                            "type" to ejercicio.excersice.type
//                        ),
//                        "reps" to ejercicio.reps,
//                        "weight" to ejercicio.weight
//                    )
//                }.toMap()
//            )
//
//            val userData = db.collection(USERS_COLLECTION).document(userId).get().await()
//            if (!userData.exists() || userData["entrenamientos"] == null) {
//                // Si el documento no existe, o el campo "entrenamientos" es nulo, creamos una lista vacía
//                db.collection(USERS_COLLECTION).document(userId).set(
//                    hashMapOf("entrenamientos" to arrayListOf<Entrenamiento>()), SetOptions.merge()
//                ).addOnSuccessListener {
//                    inserted = true
//                }.addOnFailureListener {
//                    inserted = false
//                }.await()
//            }
//            db.collection(USERS_COLLECTION)
//                .document(userId)
//                .update("entrenamientos",trainingMap)
////                .set(trainingMap, SetOptions.merge())
//                .addOnSuccessListener {
//                    inserted = true
//                }
//                .addOnFailureListener {
//                    inserted = false
//                }.await()
//            inserted
//        } catch (e: Exception) {
//            false
//        }
    }
}