package com.iesnervion.keepitfitness.ui.insertar_editar_entrenamiento

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.iesnervion.keepitfitness.databinding.FragmentInsertarEditarEntrenamientoBinding
import com.iesnervion.keepitfitness.domain.model.Ejercicio
import com.iesnervion.keepitfitness.domain.model.EjercicioEntrenamiento
import com.iesnervion.keepitfitness.ui.insertar_editar_entrenamiento.recyclerview.InsertarEditarAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertarEditarEntrenamientoFragment : Fragment() {

    private var _binding: FragmentInsertarEditarEntrenamientoBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerAdapter: InsertarEditarAdapter
    private var exercises: MutableList<EjercicioEntrenamiento> = mutableListOf()

//    private val viewModel: EntrenamientosViewModel by viewModels()

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

        initObservers()
        initListeners()

        initRecyclerView()
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

    /**
     * Inicializa los observadores de los datos de la vista.
     * Se observa el estado del login del viewmodel y se gestiona en función de su valor.
     */
    private fun initObservers() {

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

            }

            bAddExercise.setOnClickListener {
                showAlert(requireContext())
            }
        }
    }

    private fun showAlert(context: Context) {

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Crear ejercicio")
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

        val tvType = TextView(context)
        tvType.text = "Tipo"
        container.addView(tvType)
        val etType = EditText(context)
        etType.inputType = InputType.TYPE_CLASS_TEXT
        container.addView(etType)

        val tvPhoto = TextView(context)
        tvPhoto.text = "Foto"
        container.addView(tvPhoto)
        val etPhoto = EditText(context)
        etPhoto.inputType = InputType.TYPE_TEXT_VARIATION_URI
        container.addView(etPhoto)

        val tvWeight = TextView(context)
        tvWeight.text = "Peso"
        container.addView(tvWeight)
        val etWeight = EditText(context)
        etWeight.inputType = InputType.TYPE_CLASS_NUMBER
        container.addView(etWeight)

        val tvReps = TextView(context)
        tvReps.text = "Repeticiones"
        container.addView(tvReps)
        val etReps = EditText(context)
        etReps.inputType = InputType.TYPE_CLASS_NUMBER
        container.addView(etReps)

        builder.setView(container)
        // Set up the buttons
        builder.setPositiveButton("OK") { dialog, which ->

            val name = etName.text.toString()
            val type = etType.text.toString()
            val photo = etPhoto.text.toString()
            val weight = etWeight.text.toString()
            val reps = etReps.text.toString()
            // TODO: Checkear nulos y vacíos
            val ejercicio = EjercicioEntrenamiento(exercise = Ejercicio(id= "0", photo = photo, name = name, type = type), reps = reps.toLong(), weight = weight.toLong())
            exercises.add(ejercicio)
        }
        builder.setNegativeButton("Cancel") { dialog, which ->
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
                pbLoadingExercises.visibility = View.VISIBLE
            } else {
                pbLoadingExercises.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

