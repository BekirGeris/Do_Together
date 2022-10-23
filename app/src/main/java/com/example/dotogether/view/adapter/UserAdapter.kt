package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.UserRowBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.UserHolder
import java.util.ArrayList

class UserAdapter(val users: ArrayList<User>) : RecyclerView.Adapter<UserHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
        val bindin = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserHolder(bindin.root)
    }

    override fun onBindViewHolder(holder: UserHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int {
        return users.size
    }
}