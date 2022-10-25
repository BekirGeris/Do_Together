package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.MemberColumnBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.MemberHolder
import java.util.ArrayList

class MemberAdapter(private val members: ArrayList<User>) : RecyclerView.Adapter<MemberHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberHolder {
        val binding = MemberColumnBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MemberHolder(binding.root)
    }

    override fun onBindViewHolder(holder: MemberHolder, position: Int) {
        holder.bind(members[position])
    }

    override fun getItemCount(): Int {
        return members.size
    }
}