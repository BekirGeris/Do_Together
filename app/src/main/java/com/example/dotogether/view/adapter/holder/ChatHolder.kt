package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.response.MyChatsResponse
import com.example.dotogether.util.helper.RuntimeHelper

class ChatHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemChatBinding.bind(view)
    private val context = binding.root.context
    private lateinit var chat: MyChatsResponse

    init {
        initViews()
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
    }

    fun bind(chat: MyChatsResponse) {
        this.chat = chat

        chat.otherUser?.let {
            binding.username.text = if (chat.chat_type == "activity") it.target else it.username
            RuntimeHelper.glideForPersonImage(context).load(it.img).into(binding.userImage)
        }
        binding.textView2.text = chat.last_message
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when (v) {
            binding.holderView -> {
                chat.otherUser?.chat_id?.let {
                    goToChatFragment(
                        navController,
                        it,
                        chat.otherUser!!,
                        chat.chat_type == "activity"
                    )
                }
            }
        }
    }
}