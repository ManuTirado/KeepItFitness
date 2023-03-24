package com.iesnervion.keepitfitness.domain.repository

import com.iesnervion.keepitfitness.domain.model.Entrenamiento

interface TrainRepository {

    suspend fun getAllTrains():List<Entrenamiento>

    suspend fun getTraining(id:String): Entrenamiento

    suspend fun insertTraining(entrenamiento: Entrenamiento): Boolean
}