package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.ReelsColumnBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.view.adapter.holder.ReelsHolder
import java.util.ArrayList

class ReelsAdapter(private val reelsList: ArrayList<Reels>) : RecyclerView.Adapter<ReelsHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReelsHolder {
        val binding = ReelsColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReelsHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ReelsHolder, position: Int) {
        holder.bind(reelsList[position])
    }

    override fun getItemCount(): Int {
        return reelsList.size
    }
}