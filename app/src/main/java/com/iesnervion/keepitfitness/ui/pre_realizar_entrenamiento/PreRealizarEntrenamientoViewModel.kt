package com.iesnervion.keepitfitness.ui.pre_realizar_entrenamiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetTrainingUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PreRealizarEntrenamientoViewModel @Inject constructor(
    private val getTrainUseCase: FirebaseGetTrainingUseCase
) : ViewModel() {

    private val _loadingTrainState: MutableLiveData<Resource<Entrenamiento>> =
        MutableLiveData()
    val loadingTrainState: LiveData<Resource<Entrenamiento>>
        get() = _loadingTrainState

    fun getTrain(id: String) {
        viewModelScope.launch {

            getTrainUseCase(id).onEach { state ->
                _loadingTrainState.value = state
            }.launchIn(viewModelScope)
        }
    }
}