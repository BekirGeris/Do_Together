package com.example.dotogether.view.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemDiscoverBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.DiscoverHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener

class DiscoverAdapter(private val discovers: ArrayList<Discover>, private val listener: HolderListener.TargetHolderListener) : BaseAdapter() {

    private lateinit var binding: ItemDiscoverBinding
    private val allHolder = ArrayList<DiscoverHolder>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        binding = ItemDiscoverBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val holder = DiscoverHolder(binding.root, listener)
        allHolder.add(holder)
        return holder
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as DiscoverHolder
        holder.bind(discovers[position])
    }

    override fun getItemCount(): Int {
        return discovers.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun notifyDataChangedForTargets() {
        allHolder.forEach {
            it.targetAdapter.notifyDataSetChanged()
        }
    }
}