package com.example.dotogether.service

import android.content.Context
import android.util.Log
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.dotogether.data.repostory.AppRepository
import com.example.dotogether.model.Basket
import com.example.dotogether.model.NotificationData
import com.example.dotogether.model.request.UpdateUserRequest
import com.example.dotogether.util.Constants
import com.example.dotogether.util.SharedPreferencesUtil
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.util.helper.RuntimeHelper.myToString
import com.example.dotogether.view.activity.OthersActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.thread

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    @Inject
    lateinit var appRepository : AppRepository

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "From: ${remoteMessage.from}")
        val data = remoteMessage.data
        val notification = remoteMessage.notification
        val notificationData = NotificationData(data["notification_type"] ?: "", data["type_id"] ?: "")

        if (data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: $data")
//            scheduleJob()
        }

        remoteMessage.notification?.let {
            Log.d(TAG, "Message Notification : ${it.myToString()}")
        }

        thread {
            runBlocking {
                val basket = appRepository.localRepositoryImpl.getCurrentBasketSync() ?: Basket()

                if (basket.viewType != Constants.ViewType.VIEW_CHAT_FRAGMENT.type || basket.viewId == null || notificationData.typeId != basket.viewId) {
                    if (notification?.title != null && notification.body != null) {
                        RuntimeHelper.sendNotification(applicationContext,
                            OthersActivity::class.java,
                            notification.title,
                            notification.body,
                            notificationData.typeId?.toInt() ?: Random().nextInt(),
                            notificationData
                        )
                    }
                }

                when {
                    notificationData.type.equals("Notification", ignoreCase = true) -> {
                        basket.refreshType = Constants.NOTIFICATION
                    }
                    notificationData.type.equals("Target", ignoreCase = true) -> {
                        basket.refreshType = Constants.TARGET
                    }
                    notificationData.type.equals("Chat", ignoreCase = true) -> {
                        basket.refreshType = Constants.CHAT
                    }
                }
                appRepository.localRepositoryImpl.updateBasket(basket)
            }
        }
    }

    override fun onNewToken(token: String) {
        thread {
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