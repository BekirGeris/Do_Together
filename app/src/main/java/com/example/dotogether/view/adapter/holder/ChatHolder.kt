package com.example.dotogether.view.adapter.holder

import android.annotation.SuppressLint
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.response.ChatResponse
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryParse

class ChatHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemChatBinding.bind(view)
    private val context = binding.root.context
    private lateinit var chat: ChatResponse

    init {
        initViews()
    }

    private fun initViews() {
        binding.holderView.setOnClickListener(this)
    }

    @SuppressLint("SetTextI18n")
    fun bind(chat: ChatResponse) {
        this.chat = chat

        chat.otherUser?.let {
            val title = if (chat.chat_type == Constants.ACTIVITY) it.target else it.username
            title?.let { t ->
                binding.titleTxt.text = if (t.length > 20) t.substring(0, 20) + "..." else t
            }

            var lastMessage = chat.last_message
            lastMessage?.let { msg ->
                lastMessage = if (msg.length > 55) msg.substring(0, 55) + "..." else msg
            }
            binding.message.text = lastMessage

            if (chat.chat_type == Constants.ACTIVITY) {
                RuntimeHelper.glide(
                    context,
                    RequestOptions()
                        .placeholder(R.drawable.target_background)
                        .error(R.drawable.target_background)
                ).load(it.img).into(binding.userImage)
            } else {
                RuntimeHelper.glideForPersonImage(context).load(it.img).into(binding.userImage)
            }

            it.updated_at?.let { update_at ->
                val date = Constants.DATE_FORMAT_3.tryParse(update_at)
                date?.let { d ->
                    binding.time.text = Constants.DATE_FORMAT_2.format(d)
                }
            }
        }

        if (chat.unread_count != 0) {
            binding.unreadCount.text = chat.unread_count.toString()
            binding.unreadCountLyt.visibility = View.VISIBLE
        } else {
            binding.unreadCountLyt.visibility = View.GONE
        }
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
                        chat.chat_type == Constants.ACTIVITY
                    )
                }
            }
        }
    }
}