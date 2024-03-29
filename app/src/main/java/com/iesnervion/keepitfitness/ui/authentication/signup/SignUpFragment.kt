package com.iesnervion.keepitfitness.ui.authentication.signup

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.iesnervion.keepitfitness.R
import com.iesnervion.keepitfitness.databinding.FragmentSignUpBinding
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SignUpViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> SignUpFragment")

        initObservers()
        initListeners()
    }

    /**
     * Inicializa los observadores de los datos de la vista
     */
    private fun initObservers() {
        viewModel.signUpState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se ha registrado correctamente
                    handleLoading(false)
                    activity?.onBackPressedDispatcher?.onBackPressed()
                    Toast.makeText(
                        requireContext(),
                        "Sign up Success",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Error -> {      // Si ha habido un error
                    handleLoading(false)
                    Toast.makeText(
                        requireContext(),
                        "Sign up Error: ${state.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is Resource.Loading -> handleLoading(true)      // Si está cargando
                else -> Unit    // Si no se ha hecho nada
            }
        }
    }

    /**
     * Inicializa los listeners de la vista
     */
    private fun initListeners() {
        with(binding) {
            // Botón de registro de usuario
            bSignUp.setOnClickListener {
                handleSignUp()
            }
            // Botón de volver atrás
            bBack.setOnClickListener {
                activity?.onBackPressedDispatcher?.onBackPressed()
            }
        }
    }

    /**
     * Maneja el registro de un usuario.
     * Obtiene los datos de los EditText y los envía al ViewModel para que se registre.
     */
    private fun handleSignUp() {
        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        viewModel.signUp(email, password)
    }

    /**
     * Maneja la visibilidad del ProgressBar
     * @param isLoading Booleano que indica si se está cargando o no
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bSignUp.text = ""
                bSignUp.isEnabled = false
                pbSignUp.visibility = View.VISIBLE
            } else {
                pbSignUp.visibility = View.GONE
                bSignUp.text=getString(R.string.login__signup_button)
                bSignUp.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}