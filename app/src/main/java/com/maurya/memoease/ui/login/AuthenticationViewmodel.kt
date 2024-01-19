package com.maurya.memoease.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maurya.memoease.models.UserRequest
import com.maurya.memoease.models.UserResponse
import com.maurya.memoease.repository.UserRepository
import com.maurya.memoease.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthenticationViewmodel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    val userResponseStateFlow: StateFlow<NetworkResult<UserResponse>> get() = userRepository.userResponseStateFLow
    fun registerUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.registerUser(userRequest)
        }
    }

    fun loginUser(userRequest: UserRequest) {
        viewModelScope.launch {
            userRepository.loginUser(userRequest)
        }
    }

}