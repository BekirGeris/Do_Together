package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemNotificationBinding
import com.example.dotogether.model.Notification
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryParse
import com.example.dotogether.view.adapter.holderListener.HolderListener

class NotificationHolder(view: View, private val listener: HolderListener.NotificationHolderListener) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemNotificationBinding.bind(view)
    private val context = binding.root.context
    private lateinit var notification: Notification

    fun bind(notification: Notification) {
        this.notification = notification
        binding.holderView.setOnClickListener(this)
        binding.notificationImage.setOnClickListener(this)
        binding.notificationTitle.setOnClickListener(this)

        if (notification.is_read == true) {
            binding.lookedView.visibility = View.GONE
        } else {
            binding.lookedView.visibility = View.VISIBLE
        }

        notification.created_at?.let {
            val date = Constants.DATE_FORMAT_3.tryParse(it)
            date?.let { d ->
                binding.time.text =  Constants.DATE_FORMAT_2.format(d)
            }
        }

        binding.notificationTitle.text = notification.title
        binding.notificationMessage.text = notification.description

        notification.img?.let {
            RuntimeHelper.glideForPersonImage(context).load(it).into(binding.notificationImage)
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.holderView, binding.notificationImage, binding.infoLyt, binding.notificationTitle -> {
                if (notification.type.equals("target", ignoreCase = true)) {
                    notification.type_id?.let { goToTargetFragment(view.findNavController(), it) }
                } else if (notification.type.equals("notification", ignoreCase = true)) {
                    notification.type_id?.let { goToProfileFragment(view.findNavController(), it) }
                }
            }
        }
        listener.onClickNotification(binding, notification)
    }
}