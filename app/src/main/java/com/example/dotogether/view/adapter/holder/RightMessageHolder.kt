package com.example.dotogether.view.adapter.holder

import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import com.daimajia.swipe.SwipeLayout
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog

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

        dialogBinding.delete.visibility = View.VISIBLE
        dialogBinding.delete.setOnClickListener(this)
        dialogBinding.copy.visibility = View.VISIBLE
        dialogBinding.copy.setOnClickListener(this)
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
            binding.swipeLayout.isSwipeEnabled = true
            binding.replyBtn.visibility = View.VISIBLE
        }
        binding.messageTime.text = message.messageTime
        binding.messageTxt.text = message.message

        binding.messageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }

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
    }

    override fun onClick(v: View?) {
        when (v) {
            dialogBinding.delete -> {
                listener.deleteMessage(message)
                bottomSheetDialog.dismiss()
            }
            dialogBinding.copy -> {
                listener.copyMessage(message)
                bottomSheetDialog.dismiss()
            }
            else -> {}
        }
    }
}