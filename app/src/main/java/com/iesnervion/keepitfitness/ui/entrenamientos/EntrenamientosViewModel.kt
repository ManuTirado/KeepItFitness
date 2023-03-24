package com.iesnervion.keepitfitness.ui.entrenamientos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetAllTrainsUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebaseInsertTrainingUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenamientosViewModel @Inject constructor(
    private val getAllTrainsUseCase: FirebaseGetAllTrainsUseCase,
    private val insertTrainingUseCase: FirebaseInsertTrainingUseCase
): ViewModel() {

    private val _loadingTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingTrainsState

    private val _insertState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val insertState: LiveData<Resource<Boolean>>
        get() = _insertState

    fun getAllTrains() {
        viewModelScope.launch {

            getAllTrainsUseCase().onEach { state ->
                _loadingTrainsState. value = state
            }.launchIn(viewModelScope)
        }
    }

    fun insertTraining(entrenamiento: Entrenamiento) {
        viewModelScope.launch {

            insertTrainingUseCase(entrenamiento).onEach { state ->
                _insertState. value = state
            }.launchIn(viewModelScope)
        }
    }
}