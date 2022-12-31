package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.TargetHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener
import java.util.ArrayList

class TargetAdapter(
    private val targetList: ArrayList<Target>,
    private val targetHolderListener: HolderListener.TargetHolderListener
) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TargetHolder(binding.root, LayoutInflater.from(parent.context), targetHolderListener)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as TargetHolder
        holder.bind(targetList[position])
    }

    override fun getItemCount(): Int {
        return targetList.size
    }
}