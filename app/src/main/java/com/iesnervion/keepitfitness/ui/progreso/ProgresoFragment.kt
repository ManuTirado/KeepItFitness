package com.iesnervion.keepitfitness.ui.progreso

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentProgresoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProgresoFragment : Fragment() {
    private var _binding: FragmentProgresoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProgresoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> ProgresoFragment")

        Glide.with(binding.ivEnConstruccion.context).load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR01arqmRBvvfszUUBUtM51vaFGjOyvaOCpa5uicV6miMyp1RbCCsw83uPoBDDjAL_2kNw&usqp=CAU").placeholder(R.drawable.ic_uknown_user)
            .into(binding.ivEnConstruccion)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}