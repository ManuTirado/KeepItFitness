package com.iesnervion.keepitfitness.ui.configuration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityUserConfigurationBinding
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserConfigurationBinding

    private val viewModel: UserConfigurationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserConfigurationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.i("Navigation", "Create -> UserConfigurationActivity")

        initObservers()
        initListeners()

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
                    binding.bSaveChanges.isEnabled = false
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
        viewModel.updatingState.observe(this) {state ->
            when (state) {
                is Resource.Success -> {    // Si se actualiza el usuario correctamente
                    handleUserUpdating(isLoading = false)
                    Toast.makeText(
                        this,
                        "Usuario actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {      // Si ocurre un error al actualizar el usuario
                    handleUserUpdating(isLoading = false)
                    Toast.makeText(
                        this,
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleUserUpdating(isLoading = true)  // Si está cargando, se muestra el ProgressBar
                else -> Unit    // Si no se hace nada
            }
        }
    }

    private fun displayUserData(user: User) {
        with(binding) {
            Glide.with(ivUserPhoto.context).load("https://goo.gl/gEgYUd").into(binding.ivUserPhoto)
            etUsername.setText(user.username)
            etEmail.setText(user.email)
        }
    }

    /**
     * Inicializa los listeners de la vista.
     */
    private fun initListeners() {
        with(binding) {
            bBack.setOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }
            bSaveChanges.setOnClickListener {
                viewModel.updateUser(
                    User(
                        username = etUsername.text.toString(),
                        email = etEmail.text.toString()
                    )
                )
            }
        }
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleUserLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bSaveChanges.text = ""
                bSaveChanges.isEnabled = false
                pbLoadingUser.visibility = View.VISIBLE
            } else {
                pbLoadingUser.visibility = View.GONE
                bSaveChanges.text = getString(R.string.home__save_changes_button)
                bSaveChanges.isEnabled = true
            }
        }
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleUserUpdating(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bSaveChanges.text = ""
                bSaveChanges.isEnabled = false
                pbUpdatingUser.visibility = View.VISIBLE
            } else {
                pbUpdatingUser.visibility = View.GONE
                bSaveChanges.text = getString(R.string.home__save_changes_button)
                bSaveChanges.isEnabled = true
            }
        }
    }
}