package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dotogether.databinding.ItemUserBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.UserHolder
import java.util.ArrayList

class UserAdapter(private val users: ArrayList<User>) : BaseAdapter() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(binding.root)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder as UserHolder
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}