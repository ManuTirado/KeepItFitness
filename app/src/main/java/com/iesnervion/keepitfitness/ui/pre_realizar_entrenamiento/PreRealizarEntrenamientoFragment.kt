package com.iesnervion.keepitfitness.ui.pre_realizar_entrenamiento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentPreRealizarEntrenamientoBinding
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.ui.RealizarEntrenamientoActivityArgs
import com.iesnervion.keepitfitness.ui.pre_realizar_entrenamiento.recyclerview.PreRealizarEntrenamientoAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreRealizarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentPreRealizarEntrenamientoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreRealizarEntrenamientoViewModel by viewModels()

    private val args: RealizarEntrenamientoActivityArgs by navArgs()

    private lateinit var recyclerAdapter: PreRealizarEntrenamientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreRealizarEntrenamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> PreRealizarEntrenamientoFragment")

        initObservers()
        initListeners()

        val id = args.id
        if (id != null) {
            viewModel.getTrain(id)
        } else {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

    }

    private fun initRecyclerView(exercises: List<EjercicioEntrenamiento>) {
        //val manager = GridLayoutManager(this, 2)
        val manager = LinearLayoutManager(requireContext())
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.recyclerTraining.layoutManager = manager
        recyclerAdapter = PreRealizarEntrenamientoAdapter(
            exercises
        ) { exercise -> onItemSelected(exercise) }
        binding.recyclerTraining.adapter = recyclerAdapter

        //binding.recyclerTraining.addItemDecoration(decoration)
    }

    fun onItemSelected(exercise: EjercicioEntrenamiento) {
        Toast.makeText(requireContext(), exercise.exercise.name, Toast.LENGTH_SHORT).show()
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.loadingTrainState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el entrenamiento correctamente
                    initRecyclerView(state.data.ejercicios)
                    binding.tvHeader.text = state.data.id
                    handleLoading(isLoading = false)
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
    }

    private fun initListeners() {
        with(binding) {
            bBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            bStartTraining.setOnClickListener {
                val action = PreRealizarEntrenamientoFragmentDirections.actionPreRealizarEntrenamientoFragmentToRealizarEntrenamientoFragment(args.id ?: "0")
                findNavController().navigate(action)
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
                bStartTraining.text = ""
                bStartTraining.isEnabled = false
                pbLoadingTraining.visibility = View.VISIBLE
            } else {
                pbLoadingTraining.visibility = View.GONE
                bStartTraining.text = getString(R.string.realizar_entrenamiento__start_training_button)
                bStartTraining.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}