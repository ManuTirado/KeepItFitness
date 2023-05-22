package com.iesnervion.keepitfitness.ui.tabProgreso.progreso

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.iesnervion.keepitfitness.domain.model.EntrenamientoRealizado
import com.iesnervion.keepitfitness.domain.usecase.GetUserDoneTrainingsUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgresoViewModel @Inject constructor(
    private val getUserDoneTrainings: GetUserDoneTrainingsUseCase,
    private val auth: FirebaseAuth
): ViewModel() {

    private val _trainingsState: MutableLiveData<Resource<List<EntrenamientoRealizado>>> = MutableLiveData()
    val trainingsState: LiveData<Resource<List<EntrenamientoRealizado>>> get() = _trainingsState

    fun getTrainings() {
        val uid = auth.uid
        if (uid != null) {
            viewModelScope.launch {
                getUserDoneTrainings(uid).onEach {state ->
                    _trainingsState.value = state
                }.launchIn(viewModelScope)
            }
        } else {
            _trainingsState.value = Resource.Error("No user id")
        }
    }
}