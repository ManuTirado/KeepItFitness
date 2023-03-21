package com.iesnervion.keepitfitness.ui.password_recovery

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentPasswordRecoveryBinding
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PasswordRecoveryFragment : Fragment() {
    private var _binding: FragmentPasswordRecoveryBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PasswordRecoveryViewModel by viewModels()

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

        initObservers()
        initListeners()
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     */
    private fun initObservers() {
        viewModel.passwordSentState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se ha enviado el email correctamente.
                    handleLoading(isLoading = false)
                    activity?.onBackPressedDispatcher?.onBackPressed()
                    Toast.makeText(
                        requireContext(),
                        "Email enviado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {      // Si ha habido algún error.
                    handleLoading(isLoading = false)
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(isLoading = true)      // Si se está cargando.
                else -> Unit    // Si no se ha enviado nada.
            }
        }
    }

    /**
     * Inicializa los listeners de la vista.
     */
    private fun initListeners() {
        with(binding) {
            // Botón para volver atrás.
            bBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
            // Botón para enviar el email de recuperación de contraseña.
            bRecoverPassword.setOnClickListener {
                handlePasswordRecovery()
            }
        }
    }

    /**
     * Gestiona el envío del email de recuperación de contraseña.
     */
    private fun handlePasswordRecovery() {
        val email = binding.etEmail.text.toString()

        viewModel.sendPasswordRecoveryMsg(email)
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bRecoverPassword.text = ""
                bRecoverPassword.isEnabled = false
                pbRecoverPassword.visibility = View.VISIBLE
            } else {
                pbRecoverPassword.visibility = View.GONE
                bRecoverPassword.text = getString(R.string.login__password_recovery_button)
                bRecoverPassword.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}