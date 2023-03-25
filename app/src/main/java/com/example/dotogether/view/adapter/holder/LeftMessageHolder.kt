package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.dotogether.databinding.ItemMessageLeftBinding
import com.example.dotogether.model.Message
import com.example.dotogether.util.helper.ColorGenerator

class LeftMessageHolder(view: View) : BaseHolder(view), View.OnClickListener {

    private val binding = ItemMessageLeftBinding.bind(view)
    private val context = binding.root.context
    private lateinit var message: Message

    init {
        initViews()
    }

    private fun initViews() {
        binding.userName.setOnClickListener(this)
    }

    fun bind(message: Message, isAgainMessage: Boolean) {
        this.message = message
        if (message.isDateMessage) {
            binding.dateTxt.text = message.message
            binding.messageLyt.visibility = View.GONE
            binding.dateTxt.visibility = View.VISIBLE
        } else {
            binding.messageLyt.visibility = View.VISIBLE
            binding.dateTxt.visibility = View.GONE
        }
        binding.messageTime.text = message.messageTime
        binding.messageTxt.text = message.message
        binding.userName.text = message.userName

        binding.userName.visibility = if (isAgainMessage) View.VISIBLE else View.GONE

        if (isAgainMessage) {
            binding.userName.setTextColor(ColorGenerator.getColorForKey(message.userName ?: ""))
        }
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
        }
    }
}