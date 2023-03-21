package com.iesnervion.keepitfitness.ui.password_recovery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.usecase.FirebasePasswordRecoveryUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordRecoveryViewModel @Inject constructor(
    private val passwordRecoveryUseCase: FirebasePasswordRecoveryUseCase
) : ViewModel() {

    private val _passwordSentState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val passwordSentState: LiveData<Resource<Boolean>>
        get() = _passwordSentState

    /**
     * Envia un correo de recuperación de contraseña a la dirección de correo electrónico del usuario.
     * @param email Email del usuario.
     */
    fun sendPasswordRecoveryMsg(email: String) {
        viewModelScope.launch {
            passwordRecoveryUseCase(email).onEach {
                _passwordSentState.value = it
            }.launchIn(viewModelScope)
        }
    }
}