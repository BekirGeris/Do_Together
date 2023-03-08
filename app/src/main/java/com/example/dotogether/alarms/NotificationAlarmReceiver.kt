package com.example.dotogether.alarms

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.dotogether.util.helper.RuntimeHelper.TAG

class NotificationAlarmReceiver : HiltBroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        Log.d(TAG, "app repostory: $appRepository")
    }
}