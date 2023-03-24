package com.iesnervion.keepitfitness.ui.realizar_entrenamiento

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentPreRealizarEntrenamientoBinding
import com.iesnervion.keepitfitness.databinding.FragmentRealizarEntrenamientoBinding
import com.iesnervion.keepitfitness.ui.RealizarEntrenamientoActivityArgs
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RealizarEntrenamientoFragment : Fragment() {
    private var _binding: FragmentRealizarEntrenamientoBinding? = null
    private val binding get() = _binding!!

    //private val viewModel:  by viewModels()

    private val args: RealizarEntrenamientoFragmentArgs by navArgs()

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
        Log.i("Navigation", "ViewCreated -> FragmentRealizarEntrenamientoBinding")

        binding.tvPrueba.text = args.id
        //initObservers()
        //initListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}