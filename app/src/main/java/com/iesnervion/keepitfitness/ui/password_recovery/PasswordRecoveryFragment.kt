package com.iesnervion.keepitfitness.ui.password_recovery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.iesnervion.keepitfitness.databinding.FragmentPasswordRecoveryBinding

class PasswordRecoveryFragment : Fragment() {
    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPasswordRecoveryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> PasswordRecoveryFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}