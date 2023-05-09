package com.iesnervion.keepitfitness.ui.configuration

import android.content.DialogInterface
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
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

    private lateinit var user: User
    private var newImageUri: Uri? = null

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
        viewModel.userState.observe(this) { state ->
            when (state) {
                is Resource.Success -> {    // Si se recibe el usuario correctamente
                    Log.i("Prueba", "Usuario recibido correctamente")
                    handleUserLoading(isLoading = false)
                    user = state.data
                    displayUserData(user)
                }
                is Resource.Error -> {      // Si ocurre un error al recibir el usuario
                    Log.i("Prueba", "Error obteniendo al usuario")
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
        viewModel.updatingState.observe(this) { state ->
            when (state) {
                is Resource.Success -> {    // Si se actualiza el usuario correctamente
                    Log.i("Prueba", "Usuario actualizado correctamente")
                    //viewModel.getUser()
                    handleUserUpdating(isLoading = false)
                    onBackPressedDispatcher.onBackPressed()
                    Toast.makeText(
                        this,
                        "Usuario actualizado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {      // Si ocurre un error al actualizar el usuario
                    Log.i("Prueba", "Error actualizando al usuario")
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
        viewModel.uploadingState.observe(this) { state ->
            when (state) {
                is Resource.Success -> {    // Si se sube correctamente
                    Log.i("Prueba", "Foto subida correctamente")
                    handleUserUpdating(isLoading = false)
                    viewModel.getImageURL()
                }
                is Resource.Error -> {      // Si ocurre un error al subir la foto
                    Log.i("Prueba", "Error subiendo la foto")
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
        viewModel.imageUrlState.observe(this) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene correctamente
                    Log.i("Prueba", "URL obtenida correctamente")
                    handleUserUpdating(isLoading = false)
                    user.photo = state.data.toString()
                    Log.i("Foto", user.photo)
                    viewModel.updateUser(user)
                    Toast.makeText(
                        this,
                        "Foto subida correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {      // Si ocurre un error al obtener la URL de la foto
                    Log.i("Prueba", "Error obteniendo la URL")
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
        Log.i("Prueba", "displayUserData")
        with(binding) {
            Glide.with(ivUserPhoto.context).load(user.photo).placeholder(R.drawable.ic_uknown_user)
                .into(binding.ivUserPhoto)
            etUsername.setText(user.username)
            etPhotoUrl.setText(user.photo)
        }

        if (user.uid.isEmpty()) {
            Log.i("Prueba", "isEmpty")
            with(binding) {
                etUsername.isEnabled = false
                etPhotoUrl.isEnabled = false
                bSaveChanges.isEnabled = false
            }
            showErrorAlert()
        } else {
            with(binding) {
                etUsername.isEnabled = true
                etPhotoUrl.isEnabled = true
                bSaveChanges.isEnabled = true
            }
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
                //user.photo = etPhotoUrl.text.toString()
                user.username = etUsername.text.toString()
                if (newImageUri != null) {
                    viewModel.uploadImage(newImageUri!!)
                } else {
                    viewModel.updateUser(user)
                }
            }
            ivUserPhoto.setOnClickListener {
                photoPickerLauncher.launch("image/*")
            }
        }
    }

    private var photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.ivUserPhoto.setImageURI(it)
                newImageUri = it
            } else {
                Toast.makeText(this, "Error obteniendo la foto", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showErrorAlert() {
        Log.i("Prueba", "showErrorAlert")
        val builder = AlertDialog.Builder(this)
        builder
            .setTitle("Error cargando los datos")
            .setMessage("¿Qué desea hacer?")
            .setPositiveButton("Recargar",
                DialogInterface.OnClickListener { dialog, id ->
                    viewModel.getUser()
                })
            .setNegativeButton("Volver",
                DialogInterface.OnClickListener { dialog, id ->
                    onBackPressedDispatcher.onBackPressed()
                })
        // Create the AlertDialog object and return it
        builder.create().show()
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