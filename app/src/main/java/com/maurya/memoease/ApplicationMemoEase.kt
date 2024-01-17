package com.maurya.memoease

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ApplicationMemoEase : Application(), Configuration.Provider {

    override fun onCreate() {
        super.onCreate()

        // Initialize WorkManager
        WorkManager.initialize(this, workManagerConfiguration)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .build()
}
