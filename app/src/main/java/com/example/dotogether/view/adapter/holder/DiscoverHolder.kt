package com.example.dotogether.view.adapter.holder

import android.view.View
import com.example.dotogether.databinding.ItemDiscoverBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener

class DiscoverHolder(view: View, private val listener: HolderListener.TargetHolderListener) : BaseHolder(view) {

    private val binding = ItemDiscoverBinding.bind(view)

    lateinit var targetAdapter: TargetAdapter

    fun bind(discover: Discover) {
        discover.targets?.let {
            targetAdapter = TargetAdapter(it, listener, false)
            binding.targetRv.adapter = targetAdapter
        }
        binding.title.text = discover.title
    }
}