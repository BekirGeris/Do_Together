package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.ItemChatBinding
import com.example.dotogether.model.Chat
import com.example.dotogether.view.adapter.holder.ChatHolder
import java.util.ArrayList

class ChatAdapter(private val chats: ArrayList<Chat>) : RecyclerView.Adapter<ChatHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChatHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ChatHolder, position: Int) {
        holder.bind(chats[position])
    }

    override fun getItemCount(): Int {
        return chats.size
    }
}