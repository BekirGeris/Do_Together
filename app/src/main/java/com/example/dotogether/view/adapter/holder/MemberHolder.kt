package com.example.dotogether.view.adapter.holder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.dotogether.databinding.MemberColumnBinding
import com.example.dotogether.model.User

class MemberHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = MemberColumnBinding.bind(view)

    fun bind(user: User) {

    }
}