package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemProfileBinding
import com.example.dotogether.databinding.ItemTargetBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.TargetHolder
import com.example.dotogether.view.adapter.holder.ProfileHolder

class ProfileTargetAdapter(private val targetList: ArrayList<Target>, private val user: User) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var binding: ViewBinding

        return if (viewType == 0) {
            binding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProfileHolder(binding.root)
        } else {
            binding = ItemTargetBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TargetHolder(binding.root)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            0 -> {
                val testHolder = holder as ProfileHolder
                testHolder.bind(user)
            }
            else -> {
                val targetHolder = holder as TargetHolder
                targetHolder.bind(targetList[position - 1])
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