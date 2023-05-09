package com.iesnervion.keepitfitness

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase que extiende de Application y que se encarga de inicializar Hilt.
 */
@HiltAndroidApp
class KeepItFitnessApp : Application() {
}