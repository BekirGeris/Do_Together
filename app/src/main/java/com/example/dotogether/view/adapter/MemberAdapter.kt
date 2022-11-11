package com.example.dotogether.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.dotogether.databinding.ItemBigMemberBinding
import com.example.dotogether.databinding.ItemMemberBinding
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.holder.BaseHolder
import com.example.dotogether.view.adapter.holder.BigMemberHolder
import com.example.dotogether.view.adapter.holder.MemberHolder
import java.util.ArrayList

class MemberAdapter(private val members: ArrayList<User>, private val isBig: Boolean) : BaseAdapter()  {

    private lateinit var binding: ViewBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        return when(isBig) {
            true -> {
                binding = ItemBigMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BigMemberHolder(binding.root)
            }
            else -> {
                binding = ItemMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MemberHolder(binding.root)
            }
        }
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        when(isBig) {
            true -> {
                holder as BigMemberHolder
                holder.bind(members[position])
            }
            else -> {
                holder as MemberHolder
                holder.bind(members[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return members.size
    }
}