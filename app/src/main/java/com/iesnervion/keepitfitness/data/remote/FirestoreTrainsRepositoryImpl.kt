package com.iesnervion.keepitfitness.data.remote

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.iesnervion.keepitfitness.data.util.FirebaseConstants
import com.iesnervion.keepitfitness.data.util.FirebaseConstants.TRAINS_COLLECTION
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.repository.TrainRepository
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreTrainsRepositoryImpl @Inject constructor(

) : TrainRepository {
    override suspend fun getAllTrains(): List<Entrenamiento> {
        /*
        return try {
            var trainings: MutableList<Entrenamiento> = mutableListOf()
            FirebaseFirestore.getInstance().collection(TRAINS_COLLECTION)
                .get()
                .addOnSuccessListener { train ->
                    train.documents.forEach {
                        trainings.add(
                            Entrenamiento(
                                id = it.id,
                                desc = it.getString("desc") ?: ""
                            )
                        )
                    }
                }
                .await()
            trainings.forEach {
                FirebaseFirestore.getInstance().collection(TRAINS_COLLECTION)
                    .document(it.id)

            }
            trainings
        } catch (e: Exception) {
            mutableListOf<Entrenamiento>()
        }
         */
        return paraPruebas()
    }


    private fun paraPruebas():List<Entrenamiento> {
        return listOf(
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            ),
            Entrenamiento(
                id = "básico",
                desc = "Para quién no haya tocado una pesa en la vida",
                listOf(
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "1", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/curl-biceps.jpg", name = "curl de biceps", type = "brazo"),
                        reps = 8,
                        weight = 8
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "2", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/pressbanca.png", name = "press de banca", type = "pecho"),
                        reps = 10,
                        weight = 20
                    ),
                    EjercicioEntrenamiento(
                        excersice = Ejercicio(id = "3", photo = "https://raw.githubusercontent.com/ManuelTirado/Fotos/main/sentadilla.jpg", name = "sentadilla", type = "pierna"),
                        reps = 15,
                        weight = 20
                    )
                )
            )
        )
    }
}