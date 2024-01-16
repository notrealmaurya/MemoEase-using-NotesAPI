package com.maurya.memoease.repository

import android.util.Log
import com.maurya.memoease.utils.Constants.TAG
import com.maurya.memoease.api.UserAPI
import com.maurya.memoease.models.UserRequest
import javax.inject.Inject

class UserRepository @Inject constructor(private val userAPI: UserAPI) {

    suspend fun registerUser(userRequest: UserRequest) {
        val response = userAPI.signUp(userRequest)
        Log.d(TAG, response.body().toString())


    }

    suspend fun loginUser(userRequest: UserRequest) {
        val response = userAPI.signIn(userRequest)

        Log.d(TAG, response.body().toString())

    }


}