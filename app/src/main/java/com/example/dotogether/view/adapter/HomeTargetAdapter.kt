package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemReelsTopBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Reels
import com.example.dotogether.model.Target
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.ReelsTopHolder
import com.example.dotogether.view.adapter.holder.TargetHolder

class HomeTargetAdapter(private val targetList: ArrayList<Target>, private val reelsList: ArrayList<Reels>) : BaseAdapter() {

    private lateinit var binding: ViewBinding

    lateinit var reelsTopHolder: ReelsTopHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return if (viewType == 0) {
            binding = ItemReelsTopBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ReelsTopHolder(binding.root, reelsList)
        } else {
            binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TargetHolder(binding.root, LayoutInflater.from(parent.context))
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when(holder.itemViewType) {
            0 -> {
                holder as ReelsTopHolder
                reelsTopHolder = holder
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