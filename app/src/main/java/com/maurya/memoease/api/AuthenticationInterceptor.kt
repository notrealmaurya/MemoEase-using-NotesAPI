package com.maurya.memoease.api

import com.maurya.memoease.utils.HelperSharedPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthenticationInterceptor @Inject constructor() : Interceptor{

    @Inject
    lateinit var sharedPreferenceHelper: HelperSharedPreference
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add authentication header to the original request
        val authToken = sharedPreferenceHelper.getToken()
        val requestWithAuth = originalRequest.newBuilder()
            .header("Authorization", "Bearer $authToken")
            .build()


        return chain.proceed(requestWithAuth)
    }

}