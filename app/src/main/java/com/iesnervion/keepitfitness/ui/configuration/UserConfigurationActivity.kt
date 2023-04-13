package com.iesnervion.keepitfitness.ui.configuration

import android.content.Intent
import android.graphics.Bitmap
import android.icu.text.SimpleDateFormat
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.ActivityUserConfigurationBinding
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class UserConfigurationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserConfigurationBinding

    private val viewModel: UserConfigurationViewModel by viewModels()

    private lateinit var user: User

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
                    handleUserLoading(isLoading = false)
                    user = state.data
                    displayUserData(user)
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
        viewModel.updatingState.observe(this) { state ->
            when (state) {
                is Resource.Success -> {    // Si se actualiza el usuario correctamente
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
            Glide.with(ivUserPhoto.context).load(user.photo).placeholder(R.drawable.ic_uknown_user)
                .into(binding.ivUserPhoto)
            etUsername.setText(user.username)
            etPhotoUrl.setText(user.photo)
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
                user.photo = etPhotoUrl.text.toString()
                user.username = etUsername.text.toString()
                viewModel.updateUser(user)
            }
            ivUserPhoto.setOnClickListener {
                //selectImage()
                photoPickerLauncher.launch("image/*")
            }
        }
    }

    private var photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                binding.ivUserPhoto.setImageURI(it)
                uploadImage(it)
            } else {
                Toast.makeText(this, "Error obteniendo la foto", Toast.LENGTH_SHORT).show()
            }
        }

    private fun uploadImage(uri: Uri) {
        handleUserLoading(true)

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)

        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")

        storageReference.putFile(uri)
            .addOnCompleteListener {
                handleUserLoading(false)
                if (it.isSuccessful) {
                    Toast.makeText(this, "Foto subida correctamente", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error al subir la foto", Toast.LENGTH_SHORT).show()
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