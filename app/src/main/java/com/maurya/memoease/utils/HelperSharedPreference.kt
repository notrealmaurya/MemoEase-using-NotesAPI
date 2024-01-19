package com.maurya.memoease.utils

import android.content.Context
import android.content.SharedPreferences
import com.maurya.memoease.utils.Constants.UserTokenSharedPref
import com.maurya.memoease.utils.Constants.sharedPref
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class HelperSharedPreference @Inject constructor(@ApplicationContext context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(sharedPref, Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        with(prefs.edit()) {
            putString(UserTokenSharedPref, token)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString(UserTokenSharedPref, null)
    }

    fun clearToken() {
        with(prefs.edit()) {
            remove(UserTokenSharedPref)
            commit()
        }
    }
}