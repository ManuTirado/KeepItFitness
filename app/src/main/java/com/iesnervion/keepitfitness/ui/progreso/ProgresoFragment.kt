package com.iesnervion.keepitfitness.ui.progreso

import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.util.Log
import android.util.Pair
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidplot.Region
import com.androidplot.ui.SeriesBundle
import com.androidplot.xy.*
import com.google.firebase.Timestamp
import com.iesnervion.keepitfitness.databinding.FragmentProgresoBinding
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.ui.entrenar.EntrenarFragmentDirections
import com.iesnervion.keepitfitness.ui.entrenar.recyclerview.EntrenarAdapter
import com.iesnervion.keepitfitness.ui.progreso.recyclerview.ProgresoAdapter
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import java.text.DecimalFormat
import java.text.FieldPosition
import java.text.Format
import java.text.ParsePosition
import java.time.DayOfWeek
import java.util.*
import javax.annotation.meta.When
import kotlin.random.Random

@AndroidEntryPoint
class ProgresoFragment : Fragment() {
    private var _binding: FragmentProgresoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProgresoViewModel by viewModels()
    private var selection: Pair<Int, XYSeries>? = null

    private var trainings: List<EntrenamientoRealizado>? = null
    private lateinit var recyclerAdapter: ProgresoAdapter

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

        initObservers()
        initListeners()

        setupGraph(listOf(0f, 0f, 0f, 0f, 0f, 0f, 0f))
        viewModel.getTrainings()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecyclerView(trainings: List<EntrenamientoRealizado>) {
        //val manager = GridLayoutManager(this, 2)
        val manager = LinearLayoutManager(requireContext())
        //val decoration = DividerItemDecoration(requireContext(), manager.orientation)

        binding.recyclerTraining.layoutManager = manager
        recyclerAdapter = ProgresoAdapter(
            trainings
        ) { training -> onItemSelected(training) }
        binding.recyclerTraining.adapter = recyclerAdapter

        //binding.recyclerTraining.addItemDecoration(decoration)
    }

    private fun onItemSelected(entrenamiento: EntrenamientoRealizado) {
        Toast.makeText(
            requireContext(),
            entrenamiento.fecha.toDate().toString(),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun loadTrainings(day: Int) {
        var semana: MutableList<EntrenamientoRealizado> = mutableListOf()

        val trainings = trainings
        if (trainings != null) {
            for (training in trainings) {
                val cal = Calendar.getInstance()
                cal.time = training.fecha.toDate()
                val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)
                if (compareDayOfWeek(dayOfWeek, day)) {
                    semana.add(training)
                }
            }
            initRecyclerView(semana)

            if (semana.isNotEmpty()) {
                val fecha = semana.first().fecha.toDate()
                binding.tvSelectedDate.text = "${fecha}"
            } else {
                binding.tvSelectedDate.text = "No hay entrenamientos"
            }
        } else {
            binding.tvSelectedDate.text = "No hay entrenamientos"
            Toast.makeText(requireContext(), "No trainings", Toast.LENGTH_SHORT).show()
        }
    }

    private fun compareDayOfWeek(dayOfWeek: Int, day: Int): Boolean {
        // Convertimos el entero Day a la convención de DAY_OF_WEEK
        val dayOfWeekFromDay = (day % 7) + 2

        // Comparamos si ambos números enteros representan el mismo día de la semana
        return dayOfWeek == dayOfWeekFromDay
    }

    private fun initListeners() {

    }

    private fun initObservers() {
        viewModel.trainingsState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is Resource.Success -> {    // Si se obtienen los entrenamientos correctamente
                    getWeekTrainings(state.data)
                    handleLoading(isLoading = false)
                }
                is Resource.Error -> {      // Si ocurre un error al obtener el listado
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

    private fun getWeekTrainings(trainings: List<EntrenamientoRealizado>) {
        val mondayAndSunday = getStartAndEndOfWeek()
        val monday = mondayAndSunday.first
        val sunday = mondayAndSunday.second
        val weekTrainings = mutableListOf<EntrenamientoRealizado>()
        for (training in trainings) {
            if (isTimestampBetweenDates(training.fecha, monday, sunday)) {
                weekTrainings.add(training)
            }
        }
        val trainings = weekTrainings.toList()
        this.trainings = trainings
        getExercisedTime(trainings)
    }

    private fun getExercisedTime(list: List<EntrenamientoRealizado>) {
        var semana: MutableList<Float> = mutableListOf(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        for (training in list) {
            val cal = Calendar.getInstance()
            cal.time = training.fecha.toDate()
            val dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)

            when (dayOfWeek) {
                2 -> {  // Lunes
                    val totalTime = semana[0] + convertTimeStringToMinutes(training.time)
                    semana[0] = totalTime
                }
                3 -> {
                    val totalTime = semana[1] + convertTimeStringToMinutes(training.time)
                    semana[1] = totalTime
                }
                4 -> {
                    val totalTime = semana[2] + convertTimeStringToMinutes(training.time)
                    semana[2] = totalTime
                }
                5 -> {
                    val totalTime = semana[3] + convertTimeStringToMinutes(training.time)
                    semana[3] = totalTime
                }
                6 -> {
                    val totalTime = semana[4] + convertTimeStringToMinutes(training.time)
                    semana[4] = totalTime
                }
                7 -> {
                    val totalTime = semana[5] + convertTimeStringToMinutes(training.time)
                    semana[5] = totalTime
                }
                1 -> {  // Domingo
                    val totalTime = semana[6] + convertTimeStringToMinutes(training.time)
                    semana[6] = totalTime
                }
            }
        }
        setupGraph(semana)
    }

    fun convertTimeStringToMinutes(timeString: String): Float {
        val parts = timeString.split(":")
        val minutes = parts[0].toInt()
        val seconds = parts[1].toInt()
        return (minutes.toFloat() + (seconds / 60))
    }

    fun getStartAndEndOfWeek(): Pair<Date, Date> {
        val calendar = Calendar.getInstance()   // Fecha actual
        calendar.firstDayOfWeek =
            Calendar.SUNDAY // Establecer el día de la semana en el primer día de la semana (en este caso, el domingo)
        calendar.time = Date() // Establecer el objeto Calendar en la fecha actual
        val dayOfWeek =
            calendar.get(Calendar.DAY_OF_WEEK) // Obtener el número del día de la semana (1 = domingo, 2 = lunes, ..., 7 = sábado)
        val daysToMonday =
            if (dayOfWeek == Calendar.SUNDAY) 6 else dayOfWeek - 2 // Obtener el número de días para retroceder al lunes de la semana actual
        calendar.add(
            Calendar.DAY_OF_YEAR,
            -daysToMonday
        ) // Obtener el Date del lunes de la semana actual
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 1)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        val mondayDate = calendar.time

        calendar.add(Calendar.DAY_OF_YEAR, 6) // Obtener el Date del domingo de la semana actual
        calendar.set(Calendar.HOUR_OF_DAY, 23)
        calendar.set(Calendar.MINUTE, 59)
        calendar.set(Calendar.SECOND, 59)
        val sundayDate = calendar.time
        return Pair(mondayDate, sundayDate)
    }

    private fun isTimestampBetweenDates(
        timestamp: Timestamp,
        startDate: Date,
        endDate: Date
    ): Boolean {
        val date = timestamp.toDate()
        return date.time in startDate.time..endDate.time
    }

    /**
     * Gestiona la visibilidad del ProgressBar.
     * @param isLoading Booleano que indica si está cargando o no.
     */
    private fun handleLoading(isLoading: Boolean) {
        with(binding) {
            if (isLoading) {
//                bNextExercise.text = ""
//                bNextExercise.isEnabled = false
                pbChart.visibility = View.VISIBLE
            } else {
                pbChart.visibility = View.GONE
//                bNextExercise.text = getString(R.string.home__next_exercise_button)
//                bNextExercise.isEnabled = true
            }
        }
    }

    private fun setupGraph(dataList: List<Float>?) {
        val seriesNumber: List<Float> = if (dataList.isNullOrEmpty()) {
            listOf<Float>(0f, 0f, 0f, 0f, 0f, 0f, 0f)
        } else {
            dataList
        }
        val domain = listOf("L", "M", "X", "J", "V", "S", "D")
        with(binding.plot) {
            this.graph.gridInsets.right = scaleWithDpi(-25f)
            this.plotPaddingRight = scaleWithDpi(10f)
            this.graph.paddingLeft = scaleWithDpi(10f)
            // draw a domain tick for each year:
            this.setDomainStep(StepMode.SUBDIVIDE, domain.size.toDouble())
            // get rid of decimal points in our range labels:
            this.graph.getLineLabelStyle(XYGraphWidget.Edge.LEFT).format = DecimalFormat("0")

            // (Y_VALS_ONLY means use the element index as the x value)
            val series1: XYSeries = SimpleXYSeries(
                seriesNumber,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY,
                "Minutos entrenados"
            )

            //val series1Format = LineAndPointFormatter(this, R.xml.line_point_formatter_with_labels)
            val series1Format = LineAndPointFormatter(
                Color.argb(255, 156, 62, 220),
                Color.argb(255, 150, 0, 251),
                Color.argb(120, 156, 62, 220),
                null
            ) // lineColor, vertexColor, fillColor, PointLabelFormatter

            series1Format.linePaint.strokeWidth = scaleWithDpi(3f)
            series1Format.vertexPaint.strokeWidth = scaleWithDpi(10f)

            // Add some smoothing to the lines:
            series1Format.interpolationParams =
                CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Uniform)

            val scalingSeries1 = ScalingXYSeries(series1, 0.0, ScalingXYSeries.Mode.Y_ONLY)
            this.addSeries(scalingSeries1, series1Format)

            // Calculamos el valor máximo de "y"
            val max = Collections.max(seriesNumber)
            val maxBoundray: Float
            if (max != 0f) {
                maxBoundray = max + max / 10
            } else {
                maxBoundray = 10f
            }
            this.setRangeBoundaries(0, maxBoundray, BoundaryMode.FIXED)
            this.setDomainBoundaries(-1, BoundaryMode.FIXED, 7, BoundaryMode.FIXED)
            this.setDomainStep(StepMode.INCREMENT_BY_VAL, 1.0)

            val animator = ValueAnimator.ofFloat(0f, 1f)
            animator.interpolator = AccelerateDecelerateInterpolator()

            animator.addUpdateListener { valueAnimator ->
                val scale = valueAnimator.animatedFraction
                scalingSeries1.scale = scale.toDouble()
                this.redraw()
            }

            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animator: Animator) {}
                override fun onAnimationEnd(animator: Animator) {
                    // the animation is over, so show point labels:
                    series1Format.pointLabelFormatter.vOffset = scaleWithDpi(-7f)
                    series1Format.pointLabelFormatter.textPaint.textSize = scaleWithDpi(15f)
                    series1Format.pointLabelFormatter.textPaint.color = Color.BLACK
                    this@with.redraw()
                }

                override fun onAnimationCancel(animator: Animator) {}
                override fun onAnimationRepeat(animator: Animator) {}
            })

            // the animation will run for 1 seconds:
            animator.duration = 1000
            animator.start()

            this.setOnTouchListener { view, motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(this, PointF(motionEvent.x, motionEvent.y))
                }
                true
            }

            this.graph.lineLabelInsets.bottom = scaleWithDpi(-5f)

            this.graph.getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).format = object : Format() {
                override fun format(
                    `object`: Any, @NonNull buffer: StringBuffer,
                    @NonNull field: FieldPosition
                ): StringBuffer {
                    val day = (`object` as Number).toInt() % 7
                    when (day) {
                        0 -> buffer.append("L")
                        1 -> buffer.append("M")
                        2 -> buffer.append("X")
                        3 -> buffer.append("J")
                        4 -> buffer.append("V")
                        5 -> buffer.append("S")
                        6 -> buffer.append("D")
                        else -> {}
                    }
                    return buffer
                }

                override fun parseObject(string: String, @NonNull position: ParsePosition): Any? {
                    return null
                }
            }
        }
    }

    private fun onPlotClicked(plot: XYPlot, point: PointF) {
        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        if (plot.containsPoint(point.x, point.y)) {
            val x: Number = plot.getXVal(point)
            val y: Number = plot.getYVal(point)
            selection = null
            var xDistance = 0.0
            var yDistance = 0.0

            // find the closest value to the selection:
            for (sfPair: SeriesBundle<XYSeries, out XYSeriesFormatter<*>?> in plot
                .registry.seriesAndFormatterList) {
                val series = sfPair.series
                for (i in 0 until series.size()) {
                    val thisX = series.getX(i)
                    val thisY = series.getY(i)
                    if (thisX != null && thisY != null) {
                        val thisXDistance = Region.measure(x, thisX).toDouble()
                        val thisYDistance = Region.measure(y, thisY).toDouble()
                        if (selection == null) {
                            selection = Pair<Int, XYSeries>(i, series)
                            xDistance = thisXDistance
                            yDistance = thisYDistance
                        } else if (thisXDistance < xDistance) {
                            selection = Pair<Int, XYSeries>(i, series)
                            xDistance = thisXDistance
                            yDistance = thisYDistance
                        } else if ((thisXDistance == xDistance) && thisYDistance < yDistance && thisY.toDouble() >= y.toDouble()) {
                            selection = Pair<Int, XYSeries>(i, series)
                            xDistance = thisXDistance
                            yDistance = thisYDistance
                        }
                    }
                }
            }
        } else {
            // if the press was outside the graph area, deselect:
            selection = null
        }
        if (selection != null) {
//            val txt: String =
//                "Selected: " + selection?.second?.title +
//                        " Value: " + selection?.second?.getY(selection?.first ?: 0) +
//                        " Domain: " + selection?.firstval
            val txt: String =
                " Domain: " + selection?.first
            Toast.makeText(requireContext(), txt, Toast.LENGTH_SHORT).show()

            loadTrainings(selection?.first ?: 0)
        }
    }

    private fun scaleWithDpi(num: Float): Float {
        val dpi = resources.displayMetrics.xdpi
        return num * (dpi / 160)
    }
}