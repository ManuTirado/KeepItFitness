package com.iesnervion.keepitfitness.data.remote

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.EXERCISES_COLLECTION
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.TRAININGS_COLLECTION
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.repository.TrainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FirestoreTrainsRepositoryImpl @Inject constructor(

) : TrainRepository {
    override suspend fun getAllTrains(): List<Entrenamiento> {
        val db = FirebaseFirestore.getInstance()

        val querySnapshot = withContext(Dispatchers.IO) {
            db.collection(TRAININGS_COLLECTION)
                .get()
                .await()
        }

        return querySnapshot.documents.map { document ->
            val ejerciciosList = document.get(EXERCISES_COLLECTION) as List<HashMap<String, Any>>

            val ejercicios = ejerciciosList.map { ejercicio ->
                val ejerciceMap = ejercicio["excersice"] as HashMap<String, String>

                EjercicioEntrenamiento(
                    excersice = Ejercicio(
                        id = ejerciceMap["id"] as String,
                        photo = ejerciceMap["photo"] as String,
                        name = ejerciceMap["name"] as String,
                        type = ejerciceMap["type"] as String
                    ),
                    reps = ejercicio["reps"] as Long,
                    weight = ejercicio["weight"] as Long
                )
            }

            Entrenamiento(
                id = document.get("id") as String,
                desc = document.get("desc") as String,
                ejercicios = ejercicios
            )
        }

    }

    override suspend fun getTraining(id: String): Entrenamiento {
        val db = FirebaseFirestore.getInstance()

        val querySnapshot = withContext(Dispatchers.IO) {
            db.collection(TRAININGS_COLLECTION)
                .whereEqualTo("id", id)
                .get()
                .await()
        }
        val entrenamiento = querySnapshot.documents.firstOrNull()?.let { document ->
            val ejerciciosList = document.get(EXERCISES_COLLECTION) as List<HashMap<String, Any>>

            val ejercicios = ejerciciosList.map { ejercicio ->
                val ejerciceMap = ejercicio["excersice"] as HashMap<String, String>

                EjercicioEntrenamiento(
                    excersice = Ejercicio(
                        id = ejerciceMap["id"] as String,
                        photo = ejerciceMap["photo"] as String,
                        name = ejerciceMap["name"] as String,
                        type = ejerciceMap["type"] as String
                    ),
                    reps = ejercicio["reps"] as Long,
                    weight = ejercicio["weight"] as Long
                )
            }

            Entrenamiento(
                id = document.get("id") as String,
                desc = document.get("desc") as String,
                ejercicios = ejercicios
            )
        }
        return entrenamiento ?: Entrenamiento()
    }

    override suspend fun insertTraining(entrenamiento: Entrenamiento): Boolean {
        val db = FirebaseFirestore.getInstance()

        val ejercicios = entrenamiento.ejercicios.map { ejercicioEntrenamiento ->
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

        val entrenamientoMap = hashMapOf(
            "id" to entrenamiento.id,
            "desc" to entrenamiento.desc,
            "ejercicios" to ejercicios
        )
        var isSucces: Boolean = false
        db.collection(TRAININGS_COLLECTION)
            .add(entrenamientoMap)
            .addOnSuccessListener { documentReference ->
                isSucces = true
                Log.d(
                    "FirestoreTrainsRepositoryImpl",
                    "DocumentSnapshot written with ID: ${documentReference.id}"
                )
            }
            .addOnFailureListener { e ->
                isSucces = false
                Log.w("FirestoreTrainsRepositoryImpl", "Error adding document", e)
            }.await()
        return isSucces
    }
}