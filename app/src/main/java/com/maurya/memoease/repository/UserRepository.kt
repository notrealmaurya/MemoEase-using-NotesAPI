package com.maurya.memoease.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.maurya.memoease.utils.Constants.TAG
import com.maurya.memoease.api.UserAPI
import com.maurya.memoease.models.UserRequest
import com.maurya.memoease.models.UserResponse
import com.maurya.memoease.utils.NetworkResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONObject
import retrofit2.Response
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    private val _userResponseStateFlow = MutableStateFlow<NetworkResult<UserResponse>>(NetworkResult.Loading())

    val userResponseStateFLow: StateFlow<NetworkResult<UserResponse>> get() = _userResponseStateFlow

    suspend fun registerUser(userRequest: UserRequest) {
        _userResponseStateFlow.emit(NetworkResult.Loading())
        val response = userAPI.signUp(userRequest)
        handleResponse(response)
    }

    suspend fun loginUser(userRequest: UserRequest) {
        _userResponseStateFlow.emit(NetworkResult.Loading())
        val response = userAPI.signIn(userRequest)
        handleResponse(response)

    }

    private suspend fun handleResponse(response: Response<UserResponse>) {
        if (response.isSuccessful && response.body() != null) {
            _userResponseStateFlow.emit(NetworkResult.Success(response.body()!!))
        } else if (response.errorBody() != null) {
            val errorObject = JSONObject(response.errorBody()!!.charStream().readText())
            _userResponseStateFlow.emit(NetworkResult.Error(errorObject.getString("message")))
        } else {
            _userResponseStateFlow.emit(NetworkResult.Error("Something went wrong"))
        }
    }


}