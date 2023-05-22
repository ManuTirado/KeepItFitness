package com.iesnervion.keepitfitness.ui.tabEntrenar.entrenar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetAllTrainsUseCase
import com.iesnervion.keepitfitness.domain.usecase.GetPersonalizedTrainingsUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenarViewModel @Inject constructor(
    private val getAllTrainsUseCase: FirebaseGetAllTrainsUseCase,
    private val getPersonalizedTrainings: GetPersonalizedTrainingsUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _loadingTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingTrainsState

    private val _loadingPersTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingPersTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingPersTrainsState

    fun getAllTrains() {
        viewModelScope.launch {

            getAllTrainsUseCase().onEach { state ->
                _loadingTrainsState. value = state
            }.launchIn(viewModelScope)
        }
    }

    fun getAllPersonalizedTrains() {
        viewModelScope.launch {

            getPersonalizedTrainings(auth.uid.toString()).onEach { state ->
                _loadingTrainsState. value = state
            }.launchIn(viewModelScope)
        }
    }
}