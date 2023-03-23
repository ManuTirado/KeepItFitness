package com.iesnervion.keepitfitness.ui.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityHomeBinding
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.ui.configuration.UserConfigurationViewModel
import com.iesnervion.keepitfitness.ui.login.LoginFragmentDirections
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    private val viewModel: HomeActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Navigation", "Create -> HomeActivity")

        configureNavigation()

        initObservers()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getUser()
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.userState.observe(this) {state ->
            when (state) {
                is Resource.Success -> {    // Si se recibe el usuario correctamente
                    handleUserLoading(isLoading = false)
                    displayUserData(state.data)
                }
                is Resource.Error -> {      // Si ocurre un error al recibir el usuario
                    handleUserLoading(isLoading = false)
                    Toast.makeText(
                        this,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleUserLoading(isLoading = true)  // Si está cargando, se muestra el ProgressBar
                else -> Unit    // Si no se hace nada
            }
        }
    }

    /**
     * Inicializa los listeners de la vista.
     */
    private fun initListeners() {
        with(binding) {

        }
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleUserLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoadingUserPhoto.visibility = View.VISIBLE
                pbLoadingUserData.visibility = View.VISIBLE
            } else {
                pbLoadingUserPhoto.visibility = View.GONE
                pbLoadingUserData.visibility = View.GONE
            }
        }
    }

    private fun displayUserData(user: User) {
        with(binding) {
            Glide.with(ivUserPhoto.context).load(user.photo).placeholder(R.drawable.ic_uknown_user).into(binding.ivUserPhoto)
            tvUserName.setText(user.username)
            tvUserEmail.setText(user.email)
        }
    }

    private fun configureNavigation() {
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

        binding.headerLayout.setOnClickListener {
            navController?.navigate(R.id.action_to_userConfigurationActivity)
        }
    }
}