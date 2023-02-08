package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemDiscoverBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.DiscoverHolder

class DiscoverAdapter(private val discovers: ArrayList<Discover>) : BaseAdapter() {

    private lateinit var binding: ItemDiscoverBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        binding = ItemDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DiscoverHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as DiscoverHolder
        holder.bind(discovers[position])
    }

    override fun getItemCount(): Int {
        return discovers.size
    }
}