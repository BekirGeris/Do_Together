package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.Chat

class ChatHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemChatBinding.bind(view)
    private val context = binding.root.context
    private lateinit var chat: Chat

    init {
        initViews()
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
    }

    fun bind(chat: Chat) {
        this.chat = chat
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.holderView -> {
                chat.chatId?.let { goToChatFragment(navController, it) }
            }
        }
    }
}