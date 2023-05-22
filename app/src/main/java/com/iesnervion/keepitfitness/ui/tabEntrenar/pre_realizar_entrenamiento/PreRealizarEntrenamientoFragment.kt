package com.iesnervion.keepitfitness.ui.tabEntrenar.pre_realizar_entrenamiento

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
import com.iesnervion.keepitfitness.ui.tabEntrenar.pre_realizar_entrenamiento.recyclerview.PreRealizarEntrenamientoAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreRealizarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentPreRealizarEntrenamientoBinding? = null
    private val binding get() = _binding!!

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

        initListeners()

        handleLoading(true)
        val training = args.training
        if (training != null) {
            binding.tvHeader.text = training.id
            initRecyclerView(training.ejercicios)
        } else {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
        handleLoading(false)
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

    private fun initListeners() {
        with(binding) {
            bBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            bStartTraining.setOnClickListener {
                val action =
                    PreRealizarEntrenamientoFragmentDirections.actionPreRealizarEntrenamientoFragmentToRealizarEntrenamientoFragment(args.training)
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