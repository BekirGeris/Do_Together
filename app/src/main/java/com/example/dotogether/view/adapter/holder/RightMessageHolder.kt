package com.example.dotogether.view.adapter.holder

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import com.daimajia.swipe.SwipeLayout
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message
import com.example.dotogether.util.Constants
import com.example.dotogether.util.helper.RuntimeHelper.convertToLocalTimezone
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.sql.Date

class RightMessageHolder(
    view: View,
    val layoutInflater: LayoutInflater,
    private val listener: HolderListener.MessageHolderListener
) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMessageRightBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    init {
        bottomSheetDialog.setContentView(dialogBinding.root)
        binding.includeReplyMessageLyt.setOnClickListener(this)

        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.delete.setOnClickListener(this)
        dialogBinding.copy.visibility = View.VISIBLE
        dialogBinding.copy.setOnClickListener(this)

        Linkify.addLinks(binding.messageTxt, Linkify.WEB_URLS)

        binding.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

        binding.swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout?) {
            }

            override fun onOpen(layout: SwipeLayout?) {
                binding.swipeLayout.close()
                listener.replyMessage(message, true)
                binding.swipeLayout.isSwipeEnabled = false
            }

            override fun onStartClose(layout: SwipeLayout?) {
            }

            override fun onClose(layout: SwipeLayout?) {
                binding.swipeLayout.isSwipeEnabled = true
            }

            override fun onUpdate(layout: SwipeLayout?, leftOffset: Int, topOffset: Int) {
            }

            override fun onHandRelease(layout: SwipeLayout?, xvel: Float, yvel: Float) {
            }
        })

        binding.messageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }
        binding.includeReplyMessageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }
    }

    fun bind(message: Message, isGroup: Boolean) {
        this.message = message
        if (message.isUnreadCountMessage) {
            binding.unreadMessage.text = message.message
            binding.messageLyt.visibility = View.GONE
            binding.unreadMessage.visibility = View.VISIBLE
            binding.swipeLayout.isSwipeEnabled = false
            binding.replyBtn.visibility = View.GONE
        } else {
            binding.messageLyt.visibility = View.VISIBLE
            binding.unreadMessage.visibility = View.GONE
            binding.swipeLayout.isSwipeEnabled = message.message != Constants.DELETE_MESSAGE_FIREBASE_KEY
            binding.replyBtn.visibility = View.VISIBLE
        }
        val date = Date(message.messageTime ?: 0)
        date.convertToLocalTimezone()
        binding.messageTime.text = Constants.DATE_FORMAT_4.format(date)
        binding.messageTxt.text = if (message.message == Constants.DELETE_MESSAGE_FIREBASE_KEY) context.getString(R.string.delete_firebase_message) else message.message

        message.replyMessage.let {
            if (it != null) {
                binding.includeReplyMessage.replyMessageUserName.text = if (it.isMe) context.getText(R.string.you) else it.userName
                binding.includeReplyMessage.replyMessage.text = if (it.message == Constants.DELETE_MESSAGE_FIREBASE_KEY) context.getString(R.string.delete_firebase_message) else it.message
                binding.includeReplyMessageLyt.visibility = View.VISIBLE
            } else {
                binding.includeReplyMessageLyt.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        when (v) {
            binding.includeReplyMessageLyt -> {
                message.replyMessage?.let { listener.goToMessageHolder(it) }
            }
            dialogBinding.delete -> {
                listener.deleteMessage(message)
                bottomSheetDialog.dismiss()
            }
            dialogBinding.copy -> {
                if (message.message != Constants.DELETE_MESSAGE_FIREBASE_KEY) {
                    listener.copyMessage(message)
                }
                bottomSheetDialog.dismiss()
            }
            else -> {}
        }
    }
}