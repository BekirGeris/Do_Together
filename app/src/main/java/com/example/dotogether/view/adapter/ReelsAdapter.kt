package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemReelsBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.ReelsHolder
import java.util.ArrayList

class ReelsAdapter(private val reelsList: ArrayList<Reels>) : BaseAdapter()  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemReelsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReelsHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as ReelsHolder
        holder.bind(reelsList[position])
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }
}