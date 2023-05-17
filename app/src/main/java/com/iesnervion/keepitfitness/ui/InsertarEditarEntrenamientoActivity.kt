package com.iesnervion.keepitfitness.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.navigation.fragment.NavHostFragment
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityInsertarEditarEntrenamientoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertarEditarEntrenamientoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInsertarEditarEntrenamientoBinding

//    private val args: RealizarEntrenamientoActivityArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInsertarEditarEntrenamientoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Navigation", "Create -> InsertarEditarEntrenamientoActivity")

        val bundle:Bundle = Bundle()
//        bundle.putString("id", args.id)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.insertarEditarFragmentContainer) as NavHostFragment

        navHostFragment.navController.setGraph(R.navigation.insertar_editar_entrenamiento_nav_graph, bundle)
    }
}