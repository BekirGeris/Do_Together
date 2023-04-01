package com.example.dotogether.view.adapter.holder

import android.view.LayoutInflater
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.daimajia.swipe.SwipeLayout
import com.example.dotogether.R
import com.example.dotogether.databinding.BottomSheetSettingBinding
import com.example.dotogether.databinding.ItemMessageLeftBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.holderListener.HolderListener
import com.google.android.material.bottomsheet.BottomSheetDialog

class LeftMessageHolder(
    view: View,
    val layoutInflater: LayoutInflater,
    private val listener: HolderListener.MessageHolderListener
) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMessageLeftBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    private val dialogBinding = BottomSheetSettingBinding.inflate(layoutInflater)
    private val bottomSheetDialog = BottomSheetDialog(context, R.style.BottomSheetDialogTheme)

    init {
        initViews()
    }

    private fun initViews() {
        bottomSheetDialog.setContentView(dialogBinding.root)
        binding.userName.setOnClickListener(this)

        dialogBinding.copy.visibility = View.VISIBLE
        dialogBinding.copy.setOnClickListener(this)

        binding.messageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }
        binding.includeReplyMessageLyt.setOnLongClickListener {
            bottomSheetDialog.show()
            return@setOnLongClickListener true
        }
    }

    fun bind(message: Message, isAgainMessage: Boolean) {
        this.message = message
        if (message.isDateMessage) {
            binding.dateTxt.text = message.message
            binding.messageLyt.visibility = View.GONE
            binding.dateTxt.visibility = View.VISIBLE
            binding.swipeLayout.isSwipeEnabled = false
            binding.replyBtn.visibility = View.GONE
        } else {
            binding.messageLyt.visibility = View.VISIBLE
            binding.dateTxt.visibility = View.GONE
            binding.swipeLayout.isSwipeEnabled = true
            binding.replyBtn.visibility = View.VISIBLE
        }
        binding.messageTime.text = message.messageTime
        binding.messageTxt.text = message.message
        binding.userName.text = message.userName

        binding.userName.visibility = if (isAgainMessage) View.VISIBLE else View.GONE

//        if (isAgainMessage) {
//            binding.userName.setTextColor(ColorGenerator.getColorForKey(message.userName ?: ""))
//        }

        binding.swipeLayout.showMode = SwipeLayout.ShowMode.PullOut

        binding.swipeLayout.addSwipeListener(object : SwipeLayout.SwipeListener {
            override fun onStartOpen(layout: SwipeLayout?) {
            }

            override fun onOpen(layout: SwipeLayout?) {
                binding.swipeLayout.close()
                listener.replyMessage(message, false)
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

        message.replyMessage.let {
            if (it != null) {
                binding.includeReplyMessage.replyMessageUserName.text = if (it.isMe) context.getText(R.string.you) else it.userName
                binding.includeReplyMessage.replyMessage.text = it.message
                binding.includeReplyMessageLyt.visibility = View.VISIBLE
            } else {
                binding.includeReplyMessageLyt.visibility = View.GONE
            }
        }

        binding.includeReplyMessageLyt.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var navController: NavController? = null
        try {
            navController = view.findNavController()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        when(v) {
            binding.userName -> {
                message.userId?.let { goToProfileFragment(navController, it.toInt()) }
            }
            binding.includeReplyMessageLyt -> {
                message.replyMessage?.let { listener.goToMessageHolder(it) }
            }
            dialogBinding.copy -> {
                listener.copyMessage(message)
                bottomSheetDialog.dismiss()
            }
        }
    }
}