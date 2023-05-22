package com.iesnervion.keepitfitness.ui.tabEntrenar.realizar_entrenamiento

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.usecase.FirebaseInsertUserTrainingUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealizarEntrenamientoViewModel @Inject constructor(
    private val insertUserTrainingUseCase: FirebaseInsertUserTrainingUseCase
) : ViewModel() {

    private val _insertTrainingState: MutableLiveData<Resource<Boolean>> =
        MutableLiveData()
    val insertTrainingState: LiveData<Resource<Boolean>>
        get() = _insertTrainingState

    fun insertUserTraining(entrenamiento: EntrenamientoRealizado) {
        viewModelScope.launch {

            insertUserTrainingUseCase(entrenamiento).onEach { state ->
                _insertTrainingState.value = state
            }.launchIn(viewModelScope)
        }
    }
}