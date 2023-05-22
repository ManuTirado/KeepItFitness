package com.iesnervion.keepitfitness.ui.tabEntrenar.entrenar

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesnervion.keepitfitness.databinding.FragmentEntrenarBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.ui.tabEntrenar.entrenar.recyclerview.EntrenarAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntrenarFragment : Fragment() {

    private var _binding: FragmentEntrenarBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntrenarViewModel by viewModels()
    private val trainings: MutableList<Entrenamiento> = mutableListOf()

    private lateinit var recyclerAdapter: EntrenarAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntrenarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> EntrenarFragment")
        initRecyclerView(trainings)
        initObservers()
    }

    override fun onStart() {
        super.onStart()
        trainings.removeAll(trainings)
        viewModel.getAllTrains()
        viewModel.getAllPersonalizedTrains()
    }

    private fun initRecyclerView(trainings:List<Entrenamiento>) {
        //val manager = GridLayoutManager(this, 2)
        val manager = LinearLayoutManager(requireContext())
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.recyclerTraining.layoutManager = manager
        recyclerAdapter = EntrenarAdapter(
            trainings
        ) { training -> onItemSelected(training) }
        binding.recyclerTraining.adapter = recyclerAdapter

        //binding.recyclerTraining.addItemDecoration(decoration)
    }

    fun onItemSelected(entrenamiento: Entrenamiento) {
        val action =
            EntrenarFragmentDirections.actionEntrenarFragmentToRealizarEntrenamientoActivity(
                entrenamiento
            )
        findNavController().navigate(action)
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.loadingTrainsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el listado correctamente
                    trainings.addAll(state.data)
                    recyclerAdapter.updateData(trainings)
                    //updateRecyclerView(state.data)
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

        viewModel.loadingPersTrainsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el listado correctamente
                    trainings.addAll(state.data)
                    recyclerAdapter.updateData(trainings)
                    //updateRecyclerView(state.data)
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

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoadingTrainings.visibility = View.VISIBLE
            } else {
                pbLoadingTrainings.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}