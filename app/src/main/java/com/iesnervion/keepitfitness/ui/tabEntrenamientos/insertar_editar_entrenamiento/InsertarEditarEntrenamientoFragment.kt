package com.iesnervion.keepitfitness.ui.tabEntrenamientos.insertar_editar_entrenamiento

import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesnervion.keepitfitness.databinding.FragmentInsertarEditarEntrenamientoBinding
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.ui.tabEntrenamientos.insertar_editar_entrenamiento.recyclerview.InsertarEditarAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertarEditarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentInsertarEditarEntrenamientoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerAdapter: InsertarEditarAdapter
    private var exercises: MutableList<EjercicioEntrenamiento> = mutableListOf()


    private val args: InsertarEditarEntrenamientoFragmentArgs by navArgs()

    private val viewModel: InsertarEditarEntrenamientoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentInsertarEditarEntrenamientoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("Navigation", "ViewCreated -> InsertarEditarEntrenamientoFragment")

        handleButtonEnable(exercises.isNotEmpty())
        initObservers()
        initListeners()

        initRecyclerView()
    }

    private fun handleButtonEnable(enable: Boolean) {
        binding.bSaveTraining.isEnabled = enable
    }

    private fun initRecyclerView() {
        //val manager = GridLayoutManager(this, 2)
        val manager = LinearLayoutManager(requireContext())
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.recyclerExercises.layoutManager = manager
        recyclerAdapter = InsertarEditarAdapter(
            exercises
        ) { exercise -> onItemSelected(exercise) }
        binding.recyclerExercises.adapter = recyclerAdapter

        //binding.recyclerTraining.addItemDecoration(decoration)
    }

    fun onItemSelected(exercise: EjercicioEntrenamiento) {
        Toast.makeText(requireContext(), exercise.exercise.name, Toast.LENGTH_SHORT).show()
    }

    private fun parseStringToExercise(str: String): EjercicioEntrenamiento {
        var strArr = str.split(",")
        val ejercicio = EjercicioEntrenamiento(exercise = Ejercicio(name = strArr[0], type = strArr[1], photo = strArr[2]), weight = strArr[3].toLong(), reps = strArr[4].toLong())
        return ejercicio
    }

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("ejercicio")?.observe(viewLifecycleOwner) { result ->
            exercises.add(parseStringToExercise(result))
            handleButtonEnable(exercises.isNotEmpty())
        }

        viewModel.uploadingTrainingState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se sube el entrenamiento correctamente
                    handleLoading(isLoading = false)
                    requireActivity().onBackPressedDispatcher.onBackPressed()
                }
                is Resource.Error -> {      // Si ocurre un error al subir el entrenamiento
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
            bBack.setOnClickListener {
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

            bSaveTraining.setOnClickListener {
                showSaveAlert(requireContext())
            }

            bAddExercise.setOnClickListener {
                val action =
                    InsertarEditarEntrenamientoFragmentDirections.actionInsertarEditarEntrenamientoFragmentToCrearEjercicioFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun showSaveAlert(context: Context) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Guardar entrenamiento")
        builder.setCancelable(false)

        val container = LinearLayout(context)
        container.orientation = LinearLayout.VERTICAL
        container.setPadding(20, 20, 20, 10)

        val tvName = TextView(context)
        tvName.text = "Nombre"
        container.addView(tvName)
        val etName = EditText(context)
        etName.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(etName)

        val tvDesc = TextView(context)
        tvDesc.text = "Descripción"
        container.addView(tvDesc)
        val etDesc = EditText(context)
        etDesc.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(etDesc)

        val tvTime = TextView(context)
        tvTime.text = "Tiempo estimado (min)"
        container.addView(tvTime)
        val etTime = EditText(context)
        etTime.inputType = InputType.TYPE_CLASS_NUMBER
        container.addView(etTime)

        builder.setView(container)
        // Set up the buttons
        builder.setPositiveButton("Guardar") { dialog, which ->

            val name = etName.text.toString()
            val desc = etDesc.text.toString()
            val time = if (etTime.text.isNullOrEmpty()) {
                "00:00"
            } else {
                "${etTime.text}:00"
            }
            if (!name.isNullOrEmpty()) {
                val entrenamiento = Entrenamiento(name, desc, time, exercises)
                viewModel.insertTraining(entrenamiento)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Debe introducir un nombre válido",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
                bSaveTraining.isEnabled = false
                pbLoading.visibility = View.VISIBLE
                pbLoadingExercises.visibility = View.VISIBLE
            } else {
                pbLoadingExercises.visibility = View.GONE
                pbLoading.visibility = View.GONE
                bSaveTraining.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

//    private fun prueba(): MutableList<EjercicioEntrenamiento> {
//        var ejercicios: MutableList<EjercicioEntrenamiento> = mutableListOf()
//        for (i in 1..10) {
//            val ejercicio = EjercicioEntrenamiento(exercise =  Ejercicio(id = "$i", photo = "photo", name = "ejercicio $i", type = "tipo"), reps = ranNumber(), weight = ranNumber())
//            ejercicios.add(ejercicio)
//        }
//        return ejercicios
//    }

//    private fun ranNumber(): Long {
//        return (Random.nextLong()*100)
//    }