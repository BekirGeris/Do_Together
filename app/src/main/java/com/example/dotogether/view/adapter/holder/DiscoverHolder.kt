package com.example.dotogether.view.adapter.holder

import android.view.View
import com.example.dotogether.databinding.ItemDiscoverBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Discover
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.TargetAdapter
import com.example.dotogether.view.adapter.holderListener.HolderListener

class DiscoverHolder(view: View) : BaseHolder(view) {

    private val binding = ItemDiscoverBinding.bind(view)

    private lateinit var targetAdapter: TargetAdapter

    fun bind(discover: Discover) {
        discover.targets?.let {
            targetAdapter = TargetAdapter(it, object : HolderListener.TargetHolderListener{
                override fun like(binding: ItemTargetBinding, target: Target) {
                    TODO("Not yet implemented")
                }

                override fun join(binding: ItemTargetBinding, target: Target) {
                    TODO("Not yet implemented")
                }

                override fun unLike(binding: ItemTargetBinding, target: Target) {
                    TODO("Not yet implemented")
                }

                override fun unJoin(binding: ItemTargetBinding, target: Target) {
                    TODO("Not yet implemented")
                }
            }, false)
            binding.targetRv.adapter = targetAdapter
        }
        binding.title.text = discover.title
    }
}