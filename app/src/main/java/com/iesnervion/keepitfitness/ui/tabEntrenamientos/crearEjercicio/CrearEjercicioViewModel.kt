package com.iesnervion.keepitfitness.ui.tabEntrenamientos.crearEjercicio

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.usecase.FirebaseUploadExerciseImageUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebaseUploadImageUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebseGetExerciseImageUrlUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebseGetImageUrlUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CrearEjercicioViewModel @Inject constructor(
    private val firebaseUploadExerciseImageUseCase: FirebaseUploadExerciseImageUseCase,
    private val firebseGetExerciseImageUrlUseCase: FirebseGetExerciseImageUrlUseCase
): ViewModel() {

    private val _uploadingState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val uploadingState: LiveData<Resource<Boolean>>
        get() = _uploadingState

    private val _imageUrlState: MutableLiveData<Resource<Uri>> = MutableLiveData()
    val imageUrlState: LiveData<Resource<Uri>>
        get() = _imageUrlState

    fun uploadImage(uri: Uri, exerciseId: String) {
        viewModelScope.launch {
            firebaseUploadExerciseImageUseCase(uri, exerciseId).onEach { state ->
                _uploadingState.value = state
            }.launchIn(viewModelScope)
        }
    }

    fun getImageURL(exerciseId: String) {
        viewModelScope.launch {
            firebseGetExerciseImageUrlUseCase(exerciseId).onEach { state ->
                _imageUrlState.value = state
            }.launchIn(viewModelScope)
        }
    }
}