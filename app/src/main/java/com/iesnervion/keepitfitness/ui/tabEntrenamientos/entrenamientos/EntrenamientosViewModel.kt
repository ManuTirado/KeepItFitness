package com.iesnervion.keepitfitness.ui.tabEntrenamientos.entrenamientos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.*
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenamientosViewModel @Inject constructor(
    private val getPersonalizedTrainings: GetPersonalizedTrainingsUseCase,
    private val deletePersonalizedTraining: DeletePersonalizedTrainingUseCase,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _loadingTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingTrainsState

    private val _deletingState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val deletingState: LiveData<Resource<Boolean>>
        get() = _deletingState

    fun getAllTrains() {
        viewModelScope.launch {

            getPersonalizedTrainings(auth.uid ?: "").onEach { state ->
                _loadingTrainsState.value = state
            }.launchIn(viewModelScope)
        }
    }

    fun deleteTraining(trainingId: String) {
        viewModelScope.launch {

            deletePersonalizedTraining(auth.uid ?: "", trainingId) .onEach { state ->
                _deletingState.value = state
            }.launchIn(viewModelScope)
        }
    }
}