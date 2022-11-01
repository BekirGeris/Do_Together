package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.TargetHolder
import com.example.dotogether.view.adapter.holder.ProfileHolder

class ProfileTargetAdapter(private val targetList: ArrayList<Target>, private val user: User) : RecyclerView.Adapter<BaseHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        lateinit var binding: ViewBinding

        return if (viewType == 0) {
            binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProfileHolder(binding.root)
        } else {
            binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TargetHolder(binding.root)
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when(holder.itemViewType) {
            0 -> {
                holder as ProfileHolder
                holder.bind(user)
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