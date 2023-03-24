package com.iesnervion.keepitfitness.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.iesnervion.keepitfitness.databinding.ActivityMainBinding
import com.iesnervion.keepitfitness.databinding.ActivityRealizarEntrenamientoBinding
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
}