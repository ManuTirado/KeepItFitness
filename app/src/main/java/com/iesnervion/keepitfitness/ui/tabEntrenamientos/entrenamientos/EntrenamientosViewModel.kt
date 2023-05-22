package com.iesnervion.keepitfitness.ui.tabEntrenamientos.entrenamientos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetAllTrainsUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebaseInsertTrainingUseCase
import com.iesnervion.keepitfitness.domain.usecase.GetPersonalizedTrainingsUseCase
import com.iesnervion.keepitfitness.domain.usecase.InsertPersonalizedTrainingUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenamientosViewModel @Inject constructor(
    private val getPersonalizedTrainings: GetPersonalizedTrainingsUseCase,
    private val insertPersonalizedTraining: InsertPersonalizedTrainingUseCase,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _loadingTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingTrainsState

    private val _insertState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val insertState: LiveData<Resource<Boolean>>
        get() = _insertState

    fun getAllTrains() {
        viewModelScope.launch {

            getPersonalizedTrainings(auth.uid ?: "").onEach { state ->
                _loadingTrainsState. value = state
            }.launchIn(viewModelScope)
        }
    }

    fun insertTraining(entrenamiento: Entrenamiento) {
        viewModelScope.launch {

            insertPersonalizedTraining(entrenamiento).onEach { state ->
                _insertState. value = state
            }.launchIn(viewModelScope)
        }
    }
}