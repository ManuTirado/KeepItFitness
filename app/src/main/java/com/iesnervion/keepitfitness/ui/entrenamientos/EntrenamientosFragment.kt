package com.iesnervion.keepitfitness.ui.entrenamientos

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
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentEntrenamientosBinding
import com.iesnervion.keepitfitness.databinding.FragmentLoginBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.ui.entrenamientos.adapter.TrainingAdapter
import com.iesnervion.keepitfitness.ui.login.LoginFragmentDirections
import com.iesnervion.keepitfitness.ui.login.LoginViewModel
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntrenamientosFragment : Fragment() {

    private var _binding: FragmentEntrenamientosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntrenamientosViewModel by viewModels()

    private lateinit var recyclerAdapter: TrainingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEntrenamientosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> EntrenamientosFragment")

        initRecyclerView(emptyList())
        initObservers()
        initListeners()
    }

    override fun onStart() {
        super.onStart()
        viewModel.getAllTrains()
    }

    private fun initRecyclerView(trainings:List<Entrenamiento>) {
        //val manager = GridLayoutManager(this, 2)
        val manager = LinearLayoutManager(requireContext())
        //val decoration = DividerItemDecoration(this, manager.orientation)

        binding.recyclerTraining.layoutManager = manager
        recyclerAdapter = TrainingAdapter(
            trainings
        ) { training -> onItemSelected(training) }
        binding.recyclerTraining.adapter = recyclerAdapter

        //binding.recyclerSuperHero.addItemDecoration(decoration)
    }

    private fun updateRecyclerView(data: List<Entrenamiento>) {
        recyclerAdapter.updateData(data)
        Log.i("Navigation","Function 'updateRecyclerView' executed")
    }

    fun onItemSelected(entrenamiento: Entrenamiento) {
        Toast.makeText(requireContext(), entrenamiento.id, Toast.LENGTH_SHORT).show()
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.loadingTrainsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el listado correctamente
                    updateRecyclerView(state.data)
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