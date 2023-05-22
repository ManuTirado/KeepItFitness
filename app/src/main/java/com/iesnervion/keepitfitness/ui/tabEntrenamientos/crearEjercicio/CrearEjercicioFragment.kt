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
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentCrearEjercicioBinding
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CrearEjercicioFragment : Fragment() {

    private var _binding: FragmentCrearEjercicioBinding? = null
    private val binding get() = _binding!!

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
                        val name: String = etName.text.toString()
                        val type: String = etType.text.toString()
                        // TODO: - Subir foto a firebase, recuperar el link y asignarlo aquí
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