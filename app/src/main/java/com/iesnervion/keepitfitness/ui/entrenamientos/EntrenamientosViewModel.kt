package com.iesnervion.keepitfitness.ui.entrenamientos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetAllTrainsUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntrenamientosViewModel @Inject constructor(
    private val getAllTrainsUseCase: FirebaseGetAllTrainsUseCase
): ViewModel() {

    private val _loadingTrainsState: MutableLiveData<Resource<List<Entrenamiento>>> = MutableLiveData()
    val loadingTrainsState: LiveData<Resource<List<Entrenamiento>>>
        get() = _loadingTrainsState

    fun getAllTrains() {
        viewModelScope.launch {

            getAllTrainsUseCase().onEach { state ->
                _loadingTrainsState. value = state
            }.launchIn(viewModelScope)
        }
    }
}