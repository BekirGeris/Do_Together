package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.bumptech.glide.request.RequestOptions
import com.example.dotogether.R
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.response.MyChatsResponse
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper
import com.example.dotogether.util.helper.RuntimeHelper.tryParse

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

            if (chat.chat_type == "activity") {
                RuntimeHelper.glide(
                    context,
                    RequestOptions()
                        .placeholder(R.drawable.ic_groups)
                        .error(R.drawable.ic_groups)
                ).load(it.img).into(binding.userImage)
            } else {
                RuntimeHelper.glideForPersonImage(context).load(it.img).into(binding.userImage)
            }

            it.updated_at?.let { update_at ->
                val date = Constants.DATE_FORMAT_3.tryParse(update_at)
                date?.let { d ->
                    binding.time.text = Constants.DATE_FORMAT_4.format(d)
                }
            }
        }
        binding.textView2.text = chat.last_message

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
                chat.otherUser?.unread_count = chat.unread_count
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