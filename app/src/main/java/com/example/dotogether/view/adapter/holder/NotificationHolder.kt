package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemNotificationBinding
import com.example.dotogether.model.Notification
import com.example.dotogether.view.adapter.holderListener.HolderListener

class NotificationHolder(view: View, private val listener: HolderListener.NotificationHolderListener) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemNotificationBinding.bind(view)
    private lateinit var notification: Notification

    fun bind(notification: Notification) {
        this.notification = notification
        binding.holderView.setOnClickListener(this)
        binding.notificationImage.setOnClickListener(this)
        binding.notificationTitle.setOnClickListener(this)

        if (notification.isLooked) {
            binding.lookedView.visibility = View.GONE
        } else {
            binding.lookedView.visibility = View.VISIBLE
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.notificationImage, binding.notificationTitle -> {
                goToProfileFragment(view.findNavController(), 4)
            }
            binding.holderView -> {
                goToTargetFragment(view.findNavController(), 14)
            }
        }
        listener.onClickNotification(binding, notification)
    }
}