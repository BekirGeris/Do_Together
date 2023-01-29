package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemTagBinding
import com.example.dotogether.model.Tag
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.TagHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener

class TagAdapter(private val tags: ArrayList<Tag>, private val tagHolderListener: HolderListener.TagHolderListener) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemTagBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TagHolder(binding.root, tagHolderListener)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as TagHolder
        holder.bind(tags[position])
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}