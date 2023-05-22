package com.iesnervion.keepitfitness.ui.tabEntrenamientos.insertar_editar_entrenamiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.Entrenamiento
import com.iesnervion.keepitfitness.domain.usecase.InsertPersonalizedTrainingUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class InsertarEditarEntrenamientoViewModel @Inject constructor(
    private val insertPersonalizedTraining: InsertPersonalizedTrainingUseCase
): ViewModel() {

    private val _uploadingTrainingState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val uploadingTrainingState: LiveData<Resource<Boolean>>
        get() = _uploadingTrainingState

    fun insertTraining(entrenamiento: Entrenamiento) {
        viewModelScope.launch {

            insertPersonalizedTraining(entrenamiento).onEach { state ->
                _uploadingTrainingState. value = state
            }.launchIn(viewModelScope)
        }
    }
}