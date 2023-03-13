package com.example.dotogether.service

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Basket
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var appRepository : AppRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")
            scheduleJob()
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification Body: ${it.body}")
        }

        runBlocking {
            val basket = appRepository.localRepositoryImpl.getCurrentBasketSync() ?: Basket()
            basket.totalUnreadCount++
            basket.refreshType = Constants.CHAT
            appRepository.localRepositoryImpl.updateBasket(basket)
        }
    }

    override fun onNewToken(token: String) {
        runBlocking {
            val oldToken = SharedPreferencesUtil.getString(applicationContext, Constants.FIREBASE_TOKEN, "")
            if (oldToken != token) {
                Log.d(TAG, "onNewToken old token $oldToken token : $token")
                SharedPreferencesUtil.setString(applicationContext, Constants.FIREBASE_TOKEN, token)
                val updateUserRequest = UpdateUserRequest(fcm_token = token)
                appRepository.remoteRepositoryImpl.updateUser(updateUserRequest)
            }
        }
    }

    private fun scheduleJob() {
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance(this)
            .beginWith(work)
            .enqueue()
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            Log.d(TAG, "MyWorker")
            return Result.success()
        }
    }
}