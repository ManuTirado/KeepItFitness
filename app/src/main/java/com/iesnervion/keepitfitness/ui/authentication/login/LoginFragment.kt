package com.iesnervion.keepitfitness.ui.authentication.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentLoginBinding
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> LoginFragment")

        initObservers()
        initListeners()
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        viewModel.loginState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si el login es correcto, se navega a la pantalla principal
                    handleLoading(isLoading = false)
                    val action =
                        LoginFragmentDirections.actionLoginFragmentToHomeActivity()
                        //LoginFragmentDirections.actionLoginFragmentToHomeFragment(state.data.uid)
                    findNavController().navigate(action)
                }
                is Resource.Error -> {      // Si el login es incorrecto, se muestra un mensaje de error
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
            // Al pulsar el botón de login, se llama a la función handleLogin()
            bLogin.setOnClickListener { handleLogin() }
            // Al pulsar el botón de registro, se navega a la pantalla de registro
            bSignUp.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_signUpFragment) }
            // Al pulsar el botón de recuperación de contraseña, se navega a la pantalla de recuperación de contraseña
            bPasswordRecovery.setOnClickListener { findNavController().navigate(R.id.action_loginFragment_to_passwordRecoveryFragment) }
        }
    }

    /**
     * Gestiona el login. Obtiene los datos de los EditText y llama al método login() del ViewModel.
     */
    private fun handleLogin() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.login(email, password)
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bLogin.text = ""
                bLogin.isEnabled = false
                pbLogin.visibility = View.VISIBLE
            } else {
                pbLogin.visibility = View.GONE
                bLogin.text = getString(R.string.login__login_button)
                bLogin.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}