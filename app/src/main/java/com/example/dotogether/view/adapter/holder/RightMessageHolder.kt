package com.example.dotogether.view.adapter.holder

import android.view.View
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message

class RightMessageHolder(view: View) : BaseHolder(view) {

    private val binding = ItemMessageRightBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    fun bind(message: Message, isGroup: Boolean) {
        this.message = message
        if (message.isUnreadCountMessage) {
            binding.unreadMessage.text = message.message
            binding.messageLyt.visibility = View.GONE
            binding.unreadMessage.visibility = View.VISIBLE
        } else {
            binding.messageLyt.visibility = View.VISIBLE
            binding.unreadMessage.visibility = View.GONE
        }
        binding.messageTime.text = message.messageTime
        binding.messageTxt.text = message.message
        binding.userName.text = message.userName

        binding.userName.visibility = if (isGroup) View.VISIBLE else View.GONE
    }
}