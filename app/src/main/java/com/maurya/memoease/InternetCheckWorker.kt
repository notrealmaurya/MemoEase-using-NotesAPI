package com.maurya.memoease

import android.content.Context
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.maurya.memoease.utils.checkInternet
import java.util.concurrent.TimeUnit


class InternetCheckWorker(context: Context, workerParams: WorkerParameters) :
    Worker(context, workerParams) {

    override fun doWork(): Result {
        // Check internet connectivity here
        if (checkInternet(applicationContext)) {
            // Internet is available, you can perform your tasks

            // Schedule periodic work
            schedulePeriodicWork()

            return Result.success()
        } else {
            // No internet connectivity
            return Result.retry()
        }
    }


    private fun schedulePeriodicWork() {
        // Schedule periodic work
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest = PeriodicWorkRequestBuilder<InternetCheckWorker>(
            repeatInterval = 5, // Repeat every 15 minutes
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance(applicationContext).enqueue(periodicWorkRequest)
    }
}
