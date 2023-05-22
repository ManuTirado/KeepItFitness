package com.iesnervion.keepitfitness.ui.tabEntrenar.detalleEntrenamiento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesnervion.keepitfitness.databinding.FragmentDetalleEntrenamientoResumenBinding
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.ui.tabEntrenar.pre_realizar_entrenamiento.recyclerview.PreRealizarEntrenamientoAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetalleEntrenamientoResumenFragment : Fragment() {

    private var _binding: FragmentDetalleEntrenamientoResumenBinding? = null
    private val binding get() = _binding!!

    private val args: DetalleEntrenamientoResumenFragmentArgs by navArgs()

    private lateinit var recyclerAdapter: PreRealizarEntrenamientoAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetalleEntrenamientoResumenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> DetalleEntrenamientoFragment")

        handleLoading(true)
        initListeners()
        val training = args.training
        handleLoading(false)
        initRecyclerView(training.ejercicios)

        requireActivity()
            .onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    val action =
                        DetalleEntrenamientoResumenFragmentDirections.actionDetalleEntrenamientoResumenFragmentToPreRealizarEntrenamientoFragment(
                            null
                        )
                    findNavController().navigate(action)
                }
            })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initListeners() {
        binding.bBack.setOnClickListener {
            val action =
                DetalleEntrenamientoResumenFragmentDirections.actionDetalleEntrenamientoResumenFragmentToPreRealizarEntrenamientoFragment(
                    null
                )
            findNavController().navigate(action)
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
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                pbLoadingTraining.visibility = View.VISIBLE
            } else {
                pbLoadingTraining.visibility = View.GONE
            }
        }
    }
}