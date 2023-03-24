package com.iesnervion.keepitfitness.ui.configuration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iesnervion.keepitfitness.domain.model.User
import com.iesnervion.keepitfitness.domain.usecase.FirebaseGetUserUseCase
import com.iesnervion.keepitfitness.domain.usecase.FirebaseUpdateUserUseCase
import com.iesnervion.keepitfitness.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserConfigurationViewModel @Inject constructor(
    private val firebaseGetUserUseCase: FirebaseGetUserUseCase,
    private val firebaseUpdateUserUseCase: FirebaseUpdateUserUseCase
) : ViewModel() {

    private val _userState: MutableLiveData<Resource<User>> = MutableLiveData()
    val userState: LiveData<Resource<User>>
        get() = _userState

    private val _updatingState: MutableLiveData<Resource<Boolean>> = MutableLiveData()
    val updatingState: LiveData<Resource<Boolean>>
        get() = _updatingState

    fun getUser() {
        viewModelScope.launch {
            firebaseGetUserUseCase().onEach { state ->
                _userState.value = state
            }.launchIn(viewModelScope)
        }
    }

    fun updateUser(user: User) {
        viewModelScope.launch {
            firebaseUpdateUserUseCase(user).onEach { state ->
                _updatingState.value = state
            }.launchIn(viewModelScope)
        }
    }
}