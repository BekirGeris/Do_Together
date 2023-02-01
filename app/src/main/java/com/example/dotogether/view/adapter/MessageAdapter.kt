package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemMessageLeftBinding
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.LeftMessageHolder
import com.example.dotogether.view.adapter.holder.RightMessageHolder

class MessageAdapter(private val messages: ArrayList<Message>, private val isGroup: Boolean) : BaseAdapter() {

    private lateinit var binding: ViewBinding
    private var isAgainMessage = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when(viewType) {
            0 -> {
                binding = ItemMessageRightBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                RightMessageHolder(binding.root)
            }
            else -> {
                binding = ItemMessageLeftBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                LeftMessageHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        isAgainMessage = if (isGroup && position != messages.size - 1) {
            messages[position].userName != messages[position + 1].userName
        } else isGroup

        when(holder.itemViewType) {
            0 -> {
                holder as RightMessageHolder
                holder.bind(messages[position], isAgainMessage)
            }
            else -> {
                holder as LeftMessageHolder
                holder.bind(messages[position], isAgainMessage)
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].isMe) 0 else 1
    }
}