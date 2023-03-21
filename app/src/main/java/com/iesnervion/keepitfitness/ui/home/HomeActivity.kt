package com.iesnervion.keepitfitness.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityHomeBinding
import com.iesnervion.keepitfitness.ui.login.LoginFragmentDirections
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configurate the navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fHome) as NavHostFragment?
        val navController = navHostFragment?.navController

        binding.bottomNavigationView.apply {
            navController?.let { navController ->
                NavigationUI.setupWithNavController(
                    this,
                    navController
                )
                setOnItemSelectedListener { item ->
                    NavigationUI.onNavDestinationSelected(item, navController)
                    true
                }
                setOnItemReselectedListener {item ->
                    val selectedMenuItemNacGraph =
                        navController.graph.findNode(item.itemId) as? NavGraph
                    selectedMenuItemNacGraph.let {menuGraph ->
                        menuGraph?.let { navController.popBackStack(it.startDestinationId, false) }
                    }
                }
            }
        }

        // Prueba
        binding.headerLayout.setOnClickListener {
            navController?.navigate(R.id.action_to_userConfigurationActivity)
        }

        Glide.with(this).load("https://goo.gl/gEgYUd").into(binding.ivUserPhoto)
    }
}