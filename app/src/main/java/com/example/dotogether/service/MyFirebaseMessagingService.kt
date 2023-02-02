package com.example.dotogether.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
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
import com.example.dotogether.view.activity.HomeActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MyFirebaseMessagingService() : FirebaseMessagingService() {

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
            var basket = appRepository.localRepositoryImpl.getCurrentBasketSync()
            if (basket == null) {
                basket = Basket()
                basket.totalUnreadCount++
                appRepository.localRepositoryImpl.insertBasket(basket)
            } else {
                //todo totalUnreadCount alanı bildirim içerisinden alınacak.
                basket.totalUnreadCount++
                appRepository.localRepositoryImpl.updateBasket(basket)
            }
        }
    }

    override fun onNewToken(token: String) {
        runBlocking {
            val oldToken = SharedPreferencesUtil.getString(applicationContext, Constants.FIREBASE_TOKEN, "")
            if (oldToken != token) {
                Log.d(TAG, "onNewToken old token $oldToken token : $token")
                SharedPreferencesUtil.setString(applicationContext, Constants.FIREBASE_TOKEN, token)
                val updateUserRequest = UpdateUserRequest()
                updateUserRequest.fcm_token = token
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

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, PendingIntent.FLAG_IMMUTABLE)

        val channelId = "fcm_default_channel"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("FCM Message")
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    internal class MyWorker(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
        override fun doWork(): Result {
            Log.d(TAG, "MyWorker")
            return Result.success()
        }
    }
}