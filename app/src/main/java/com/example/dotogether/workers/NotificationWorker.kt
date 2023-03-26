package com.example.dotogether.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.view.activity.HomeActivity
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appRepository: AppRepository
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        return Result.success()
    }
}