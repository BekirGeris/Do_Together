package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.Chat
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.ChatHolder
import java.util.ArrayList

class ChatAdapter(private val chats: ArrayList<Chat>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as ChatHolder
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}