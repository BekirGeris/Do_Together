package com.example.dotogether.alarms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.annotation.CallSuper
import com.example.dotogether.data.repostory.AppRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class HiltBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var appRepository: AppRepository

    @CallSuper
    override fun onReceive(context: Context?, intent: Intent?) {}
}