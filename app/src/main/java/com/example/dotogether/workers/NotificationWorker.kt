package com.example.dotogether.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Target
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.runBlocking

@HiltWorker
class NotificationWorker @AssistedInject constructor(
    @Assisted val context: Context,
    @Assisted workerParams: WorkerParameters,
    private val appRepository: AppRepository
) : Worker(context, workerParams) {

    private var nextPageNo = "0"
    private val targets = ArrayList<Target>()

    override fun doWork(): Result {
        runBlocking {
            getTarget()
            if (targets.any { it.action_status == "2" }) {
                RuntimeHelper.sendNotification(context, "Hey Orada mısın?", "Yapılmayı bekleyen bazı hedeflerin var!!!")
            }
        }
        return Result.success()
    }

    private suspend fun getTarget() {
        appRepository.remoteRepositoryImpl.getMyJoinedTargets().collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.data?.let { list ->
                        targets.addAll(list)
                    }
                    it.data?.next_page_url?.let { next_page_url ->
                        nextPageNo = next_page_url.last().toString()
                        getNextTarget(nextPageNo)
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }

    private suspend fun getNextTarget(pageNo: String) {
        appRepository.remoteRepositoryImpl.getNextMyJoinedTargets(pageNo).collect {
            when (it) {
                is Resource.Success -> {
                    it.data?.data?.let { list ->
                        targets.addAll(list)
                    }
                    it.data?.next_page_url?.let { next_page_url ->
                        nextPageNo = next_page_url.last().toString()
                        getNextTarget(nextPageNo)
                    }
                }
                is Resource.Error -> {

                }
                is Resource.Loading -> {

                }
            }
        }
    }
}