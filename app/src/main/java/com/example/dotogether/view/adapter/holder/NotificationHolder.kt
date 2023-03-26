package com.example.dotogether.view.adapter.holder

import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.RelativeSizeSpan
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

        if (notification.type.equals("target", ignoreCase = true)) {
            RuntimeHelper.glideForPersonImage(context).load(notification.img).into(binding.notificationImage)
        } else if (notification.type.equals("user", ignoreCase = true)) {
            RuntimeHelper.glideForPersonImage(context).load(notification.others_img).into(binding.notificationImage)
        }

        notification.description?.let {
            val userName = it.substringBefore(" ")

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    notification.others_id?.let { id -> goToProfileFragment(view.findNavController(), id) }
                }
            }
            val spannableString = SpannableString(it)
            val startIndex = it.indexOf(userName)
            spannableString.setSpan(clickableSpan, startIndex, startIndex + userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            spannableString.setSpan(RelativeSizeSpan(1.1f), startIndex, startIndex + userName.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            binding.notificationMessage.text = spannableString
            binding.notificationMessage.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.holderView, binding.notificationImage, binding.infoLyt, binding.notificationTitle -> {
                if (notification.type.equals("target", ignoreCase = true)) {
                    notification.type_id?.let { goToTargetFragment(view.findNavController(), it) }
                } else if (notification.type.equals("user", ignoreCase = true)) {
                    notification.type_id?.let { goToProfileFragment(view.findNavController(), it) }
                }
            }
        }
        listener.onClickNotification(binding, notification)
    }
}