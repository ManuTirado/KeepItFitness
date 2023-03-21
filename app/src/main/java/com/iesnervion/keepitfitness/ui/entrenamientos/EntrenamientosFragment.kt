package com.iesnervion.keepitfitness.ui.entrenamientos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentEntrenamientosBinding
import com.iesnervion.keepitfitness.databinding.FragmentLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EntrenamientosFragment : Fragment() {

    private var _binding: FragmentEntrenamientosBinding? = null
    private val binding get() = _binding!!

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}