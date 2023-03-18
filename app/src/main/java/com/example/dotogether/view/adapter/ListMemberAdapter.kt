package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemMemberForListBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.ListMemberHolder
import com.example.dotogether.view.adapter.holderListener.HolderListener
import java.util.ArrayList

class ListMemberAdapter(private val users: ArrayList<User>, private val listener: HolderListener.ListMemberHolderListener) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemMemberForListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListMemberHolder(binding.root, listener)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as ListMemberHolder
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}