package com.example.dotogether.alarms

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.dotogether.R
import com.example.dotogether.model.NotificationData
import com.example.dotogether.model.Target
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.TAG
import com.example.dotogether.util.Resource
import com.example.dotogether.util.helper.RuntimeHelper.tryParse
import com.example.dotogether.view.activity.HomeActivity
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.thread

class NotificationAlarmReceiver : HiltBroadcastReceiver() {

    private val targets = ArrayList<Target>()
    private var context: Context? = null

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        this.context = context

        thread {
            runBlocking {
                appRepository.remoteRepositoryImpl.getMyJoinedTargets().collect { resource ->
                    when (resource) {
                        is Resource.Success -> {
                            resource.data?.let { response ->
                                response.data?.let {
                                    targets.clear()
                                    targets.addAll(it)
                                }
                                response.next_page_url?.let {
                                    getNextAllTargets(it)
                                }
                            }
                        }
                        else -> {}
                    }
                }

                sendNotification()
                this@NotificationAlarmReceiver.context?.let {
                    RuntimeHelper.setAlarmManager(it)
                }
            }
        }
    }

    private suspend fun getNextAllTargets(nextPage: String) {
        appRepository.remoteRepositoryImpl.getNextMyJoinedTargets(nextPage).collect {resource ->
            when (resource) {
                is Resource.Success -> {
                    resource.data?.let { response ->
                        response.data?.let {
                            targets.addAll(it)
                        }
                        response.next_page_url?.let {
                            getNextAllTargets(it.last().toString())
                        }
                    }
                }
                else -> {}
            }
        }
    }

    private fun sendNotification() {
        Log.d(TAG, "targets size: ${targets.size}")
        var notificationCount = 1
        targets.forEach { target ->
            if (notificationCount <= 5 && RuntimeHelper.isDoItBTNOpen(target)) {
                context?.let { context ->
                    val notificationData = NotificationData(Constants.TARGET, target.id.toString())
                    RuntimeHelper.sendNotification( context,
                        HomeActivity::class.java,
                        target.target,
                        context.getString(R.string.target_do_it_notification_message),
                        target.id ?: 1,
                        notificationData)
                    notificationCount++
                }
            }
        }
    }
}