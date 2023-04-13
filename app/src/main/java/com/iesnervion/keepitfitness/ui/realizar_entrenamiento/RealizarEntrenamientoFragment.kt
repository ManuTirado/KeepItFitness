package com.iesnervion.keepitfitness.ui.realizar_entrenamiento

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentRealizarEntrenamientoBinding
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealizarEntrenamientoFragment : Fragment() {
    private var _binding: FragmentRealizarEntrenamientoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RealizarEntrenamientoViewModel by viewModels()

    private val args: RealizarEntrenamientoFragmentArgs by navArgs()

    private lateinit var entrenamiento: Entrenamiento
    private var ejerciciosRealizados: MutableList<EjercicioEntrenamiento> = arrayListOf()
    private var indiceEjercicioActual: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRealizarEntrenamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> RealizarEntrenamientoFragment")

        viewModel.getTrain(args.id)
        initObservers()
        initListeners()
    }

    private fun initObservers() {
        viewModel.loadingTrainState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el entrenamiento correctamente
                    entrenamiento = state.data
                    handleLoading(isLoading = false)
                    initChronometer()
                    displayExercise(entrenamiento.ejercicios[indiceEjercicioActual])
                }
                is Resource.Error -> {      // Si ocurre un error al obtener el listado
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
        viewModel.insertTrainingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se inserta el entrenamiento correctamente
                    handleLoading(isLoading = false)
                    Toast.makeText(requireContext(), "Insertado con éxito", Toast.LENGTH_SHORT)
                        .show()
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                is Resource.Error -> {      // Si ocurre un error al insertar el entrenamiento
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

    private fun initChronometer() {
        binding.chronometer.start()
    }

    private fun nextExercise() {
        view?.findFocus()?.clearFocus()
        indiceEjercicioActual++

        if (indiceEjercicioActual < entrenamiento.ejercicios.size) {
            saveExercise()
            displayExercise(entrenamiento.ejercicios[indiceEjercicioActual])
        } else {    // Ha terminado el entrenamiento
            binding.chronometer.stop()
            saveTraining()
        }
    }

    private fun saveTraining() {
        val time: String = binding.chronometer.text.toString()

        val entrenamientoRealizado = EntrenamientoRealizado(
            id = entrenamiento.id,
            desc = entrenamiento.desc,
            time = time,
            ejercicios = entrenamiento.ejercicios
        )

        viewModel.insertUserTraining(entrenamientoRealizado)
    }

    private fun saveExercise() {
        val weight: Long
        val reps: Long

        with(binding) {
            if (etWeight.text.toString().isNullOrEmpty()) {
                weight = tilWeight.hint.toString().toLong()
            } else {
                weight = etWeight.text.toString().toLong()
            }

            if (etReps.text.toString().isNullOrEmpty()) {
                reps = tilReps.hint.toString().toLong()
            } else {
                reps = etReps.text.toString().toLong()
            }
        }
        ejerciciosRealizados.add(
            EjercicioEntrenamiento(
                entrenamiento.ejercicios[indiceEjercicioActual].exercise,
                reps = reps,
                weight = weight
            )
        )

    }

    private fun displayExercise(ejercicio: EjercicioEntrenamiento) {
        with(binding) {
            etWeight.setText("")
            etReps.setText("")

            Glide.with(ivExercisePhoto.context).load(ejercicio.exercise.photo)
                .placeholder(R.drawable.ic_dumbbell)
                .into(ivExercisePhoto)
            tilWeight.hint = ejercicio.weight.toString()
            tilReps.hint = ejercicio.reps.toString()
        }
    }

    private fun initListeners() {
        with(binding) {
            bBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            bNextExercise.setOnClickListener {
                nextExercise()
            }
        }
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bNextExercise.text = ""
                bNextExercise.isEnabled = false
                pbLoadingTraining.visibility = View.VISIBLE
            } else {
                pbLoadingTraining.visibility = View.GONE
                bNextExercise.text = getString(R.string.home__next_exercise_button)
                bNextExercise.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}