package com.example.dotogether.view.adapter.holder

import android.view.View
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message

class RightMessageHolder(val view: View) : BaseHolder(view) {

    private val binding = ItemMessageRightBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    init {
        initViews()
    }

    private fun initViews() {

    }

    fun bind(message: Message, isGroup: Boolean) {
        this.message = message
        binding.messageTime.text = message.messageTime
        binding.messageTxt.text = message.message
        binding.userName.text = message.userName

        binding.userName.visibility = if (isGroup) View.VISIBLE else View.GONE
    }
}