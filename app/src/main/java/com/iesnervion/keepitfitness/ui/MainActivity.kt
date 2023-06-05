package com.iesnervion.keepitfitness.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.findNavController
import com.iesnervion.keepitfitness.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Navigation", "Create -> ActivityMainBinding")
    }
    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        val prueba = this.findNavController(binding.mainMainFragmentContainer.id)
        if (doubleBackToExitPressedOnce || prueba.backQueue.count() > 2) {
            super.getOnBackPressedDispatcher().onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Pulsa ATRÁS otra vez para salir", Toast.LENGTH_SHORT).show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }

}