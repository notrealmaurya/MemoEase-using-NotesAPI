package com.maurya.memoease

import android.content.Context
import com.maurya.memoease.utils.Constants.UserTokenSharedPref
import com.maurya.memoease.utils.Constants.sharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceHelper @Inject constructor(@ApplicationContext context: Context) {

    private var prefs= context.getSharedPreferences(sharedPref,Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        with(prefs.edit()) {
            putString(UserTokenSharedPref, token)
            apply()
        }
    }
    fun getToken(): String? {
        return prefs.getString(UserTokenSharedPref, null)
    }

}