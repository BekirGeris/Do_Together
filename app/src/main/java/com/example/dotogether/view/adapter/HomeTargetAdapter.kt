package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.ReelsTopHolder
import com.example.dotogether.view.adapter.holder.TargetHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener

class HomeTargetAdapter(
    private val targetList: ArrayList<Target>,
    private val reelsList: ArrayList<User>,
    private val targetHolderListener: HolderListener.TargetHolderListener,
    private val reelsHolderListener: HolderListener.ReelsHolderListener
) : BaseAdapter() {

    private lateinit var binding: ViewBinding

    var reelsTopHolder: ReelsTopHolder? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return if (viewType == 0) {
            binding = ItemReelsTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            reelsTopHolder = ReelsTopHolder(binding.root, reelsList, reelsHolderListener)
            reelsTopHolder!!
        } else {
            binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val targetHolder = TargetHolder(binding.root, LayoutInflater.from(parent.context), targetHolderListener)
            targetHolder
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                holder as ReelsTopHolder
                holder.bind()
            }
            else -> {
                holder as TargetHolder
                holder.bind(targetList[position - 1])
            }
        }
    }

    override fun getItemCount(): Int {
        return targetList.size + 1
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}