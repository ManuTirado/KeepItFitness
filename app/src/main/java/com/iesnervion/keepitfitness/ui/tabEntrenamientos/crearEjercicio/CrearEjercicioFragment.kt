package com.iesnervion.keepitfitness.ui.tabEntrenamientos.crearEjercicio

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentCrearEjercicioBinding
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.ui.tabProgreso.progreso.ProgresoViewModel
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CrearEjercicioFragment : Fragment() {

    private var _binding: FragmentCrearEjercicioBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CrearEjercicioViewModel by viewModels()


    private var ejercicio: EjercicioEntrenamiento? = null
    private var newImageUri: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCrearEjercicioBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> CrearEjercicioFragment")

        findNavController().previousBackStackEntry?.savedStateHandle?.remove<String>("ejercicio")
        initListeners()
        initObservers()

        with(binding) {
            if (newImageUri == null || newImageUri.toString().isEmpty()) {
                Glide.with(ivPhoto.context).load(ejercicio?.exercise?.photo)
                    .placeholder(R.drawable.ic_dumbbell)
                    .into(ivPhoto)
            }
            etName.setText(ejercicio?.exercise?.name)
            etType.setText(ejercicio?.exercise?.type)
            if (ejercicio != null) {
                etWeight.setText(ejercicio?.weight.toString())
                etReps.setText(ejercicio?.reps.toString())
            }
        }
    }

    /**
     * Inicializa los listeners de la vista.
     */
    private fun initListeners() {
        with(binding) {

            bBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            ivPhoto.setOnClickListener {
                photoPickerLauncher.launch("image/*")
            }

            bSaveChanges.setOnClickListener {
                with(binding) {
                    if (etName.text.isNullOrEmpty() || etWeight.text.isNullOrEmpty() || etReps.text.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), "El nombre, el peso y las repeticiones son obligatorios", Toast.LENGTH_SHORT).show()
                    } else {
                        val imgURI = newImageUri
                        if (imgURI != null) {
                            viewModel.uploadImage(imgURI, etName.text.toString())
                        } else {
                            val name: String = etName.text.toString()
                            val type: String = etType.text.toString()
                            val photo: String = ""
                            val weight: Long = etWeight.text.toString().toLong()
                            val reps: Long = etReps.text.toString().toLong()

                            val exerciseToString = "${name},${type},${photo},${weight},${reps}"
                            findNavController().previousBackStackEntry?.savedStateHandle?.set("ejercicio", exerciseToString)
                            findNavController().popBackStack()
                        }
                    }
                }
            }
        }
    }

    private fun initObservers() {
        viewModel.uploadingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se sube la foto correctamente
                    Toast.makeText(requireContext(), "Foto subida correctamente", Toast.LENGTH_SHORT).show()
                    viewModel.getImageURL(binding.etName.text.toString())
                    handleLoading(isLoading = false)
                }
                is Resource.Error -> {      // Si ocurre un error al subir la foto
                    handleLoading(isLoading = false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(isLoading = true)  // Si está cargando, se muestra el ProgressBar
                else -> Unit    // Si no se hace nada
            }
        }
        viewModel.imageUrlState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene la url correctamente
                    newImageUri = state.data
                    handleLoading(isLoading = false)
                    with(binding) {
                        val name: String = etName.text.toString()
                        val type: String = etType.text.toString()
                        val photo: String = newImageUri.toString()
                        val weight: Long = etWeight.text.toString().toLong()
                        val reps: Long = etReps.text.toString().toLong()

                        val exerciseToString = "${name},${type},${photo},${weight},${reps}"
                        findNavController().previousBackStackEntry?.savedStateHandle?.set("ejercicio", exerciseToString)
                        findNavController().popBackStack()
                    }
                }
                is Resource.Error -> {      // Si ocurre un error al obtener la url
                    handleLoading(isLoading = false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(isLoading = true)  // Si está cargando, se muestra el ProgressBar
                else -> Unit    // Si no se hace nada
            }
        }
    }

    private var photoPickerLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
//                binding.ivPhoto.setImageURI(it)
                Glide.with(binding.ivPhoto.context).load(it)
                    .placeholder(R.drawable.ic_dumbbell)
                    .into(binding.ivPhoto)
                newImageUri = it
            } else {
                Toast.makeText(requireContext(), "Error obteniendo la foto", Toast.LENGTH_SHORT).show()
            }
        }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
//        with(binding) {
//            if (isLoading) {
//                pbLoadingExercises.visibility = View.VISIBLE
//            } else {
//                pbLoadingExercises.visibility = View.GONE
//            }
//        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}