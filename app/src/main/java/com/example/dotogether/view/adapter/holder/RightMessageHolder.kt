package com.example.dotogether.view.adapter.holder

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class RightMessageHolder(
    view: View,
    val layoutInflater: LayoutInflater,
    private val listener: HolderListener.RightMessageHolderListener
) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMessageRightBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    init {
        bottomSheetDialog.setContentView(dialogBinding.root)

        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.delete.setOnClickListener(this)
    }

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

        binding.messageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }

        Linkify.addLinks(binding.messageTxt, Linkify.WEB_URLS)
    }

    override fun onClick(v: View?) {
        when (v) {
            dialogBinding.delete -> {
                listener.deleteMessage(message)
                bottomSheetDialog.dismiss()
            }
            else -> {}
        }
    }
}