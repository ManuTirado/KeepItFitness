package com.iesnervion.keepitfitness.ui.tabEntrenamientos.entrenamientos

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentEntrenamientosBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.ui.tabEntrenamientos.entrenamientos.adapter.EntrenamientosAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EntrenamientosFragment : Fragment() {

    private var _binding: FragmentEntrenamientosBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EntrenamientosViewModel by viewModels()

    private lateinit var recyclerAdapter: EntrenamientosAdapter

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
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.recyclerTraining.layoutManager = manager
        recyclerAdapter = EntrenamientosAdapter(
            trainings
        ) { training -> onItemSelected(training) }
        binding.recyclerTraining.adapter = recyclerAdapter

        //binding.recyclerTraining.addItemDecoration(decoration)
    }

    private fun updateRecyclerView(data: List<Entrenamiento>) {
        recyclerAdapter.updateData(data)
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.loadingTrainsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtiene el listado correctamente
                    if (state.data.isEmpty()) {
                        binding.tvNoTrainings.visibility = View.VISIBLE
                    } else {
                        binding.tvNoTrainings.visibility = View.GONE
                    }
                    initRecyclerView(state.data)
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

        viewModel.deletingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se borra correctamente
                    viewModel.getAllTrains()
                    //updateRecyclerView(state.data)
                    handleLoading(isLoading = false)
                }
                is Resource.Error -> {      // Si ocurre un error al borrar
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
            btnAdd.setOnClickListener {
                val action = EntrenamientosFragmentDirections.actionEntrenamientosFragmentToInsertarEditarEntrenamiento(null)
                findNavController().navigate(action)
            }
        }
    }

    fun onItemSelected(entrenamiento: Entrenamiento) {
        val alertBuilder = MaterialAlertDialogBuilder(requireContext(), com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog_Centered)
        alertBuilder.setTitle("Borrar")
        alertBuilder.setMessage("¿Está seguro de borrar este entrenamiento?")
        alertBuilder.setPositiveButton("Borrar") { dialog, which ->
            viewModel.deleteTraining(entrenamiento.id)
        }
        alertBuilder.setNegativeButton("Cancelar", null)
        alertBuilder.show()
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