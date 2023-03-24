package com.iesnervion.keepitfitness.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navArgs
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityRealizarEntrenamientoBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RealizarEntrenamientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRealizarEntrenamientoBinding

    private val args: RealizarEntrenamientoActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRealizarEntrenamientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Navigation", "Create -> RealizarEntrenamientoActivity")

        val bundle:Bundle = Bundle()
        bundle.putString("id", args.id)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainFragmentContainer) as NavHostFragment

        navHostFragment.navController.setGraph(R.navigation.realizar_entrenamiento_nav_graph, bundle)
    }
}