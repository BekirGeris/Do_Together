package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.TargetRowBinding
import com.example.dotogether.model.Target
import com.example.dotogether.model.User
import com.example.dotogether.view.adapter.MemberAdapter
import java.util.ArrayList
import kotlin.coroutines.coroutineContext

class TargetHolder(view: View) : RecyclerView.ViewHolder(view){

    private val binding = TargetRowBinding.bind(view)

    private var memberAdapter: MemberAdapter
    private val members = ArrayList<User>()

    init {
        for (i in 1..100) {
            members.add(User())
        }

        memberAdapter = MemberAdapter(members)
    }

    fun bind(target: Target) {
        binding.membersRv.layoutManager = LinearLayoutManager(binding.root.context, LinearLayoutManager.HORIZONTAL, false)
        binding.membersRv.adapter = memberAdapter
    }
}