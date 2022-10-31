package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemMessageLeftBinding
import com.example.dotogether.databinding.ItemMessageRightBinding
import com.example.dotogether.model.Message
import com.example.dotogether.view.adapter.holder.LeftMessageHolder
import com.example.dotogether.view.adapter.holder.RightMessageHolder
import java.util.ArrayList

class MessageAdapter(private val messages: ArrayList<Message>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var binding: ViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            0 -> {
                val rightMessageHolder = holder as RightMessageHolder
                rightMessageHolder.bind(messages[position])
            }
            else -> {
                val leftMessageHolder = holder as LeftMessageHolder
                leftMessageHolder.bind(messages[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return messages.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(messages[position].isMe) 1 else 0
    }
}